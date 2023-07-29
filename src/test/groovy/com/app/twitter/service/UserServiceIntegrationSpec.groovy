package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.domain.Post
import com.app.twitter.domain.User
import com.app.twitter.dto.UserDTO
import com.app.twitter.repository.CommentRepository
import com.app.twitter.repository.PostRepository
import com.app.twitter.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureDataMongo
class UserServiceIntegrationSpec extends Specification {
    @Autowired
    UserService userService

    @Autowired
    UserRepository userRepository

    @Autowired
    PostRepository postRepository

    @Autowired
    CommentRepository commentRepository
    User user

    def setup() {
        user = new User(username: "test", password: "testPassword")
    }

    def "should create a user"() {
        given:
        setup()

        when:
        User createdUser = userService.createUser(user)

        then:
        createdUser.username == "test"
        createdUser.password == "testPassword"

        cleanup:
        userService.deleteUserById(createdUser.id)
    }

    def "should update a user"() {
        given:
        setup()
        User createdUser = userRepository.save(user)

        when:
        createdUser.username = "updatedUsername"
        createdUser.password = "updatedPassword"
        User updatedUser = userService.updateUser(createdUser.id, createdUser)

        then:
        updatedUser.username == "updatedUsername"
        updatedUser.password == "updatedPassword"

        cleanup:
        userService.deleteUserById(createdUser.id)
    }

    def "should delete a user and related data"() {
        given:
        setup()
        user.posts = ["post1", "post2"]
        user.subscribers = ["subscriber"]
        User createdUser = userRepository.save(user)
        postRepository.save(new Post(id: "post1", content: "Post 1"))
        postRepository.save(new Post(id: "post2", content: "Post 2"))
        User subscriber = new User(id: "subscriber", subscriptions: [createdUser.id])
        userRepository.save(subscriber)

        when:
        userService.deleteUserById(createdUser.id)

        then:
        !userRepository.existsById(createdUser.id)

        and:
        !postRepository.existsById('post1')
        !postRepository.existsById('post2')

        and:
        userService.getUserById("subscriber").subscriptions == []

        cleanup:
        userRepository.deleteAll()
        postRepository.deleteAll()
    }

    def "should subscribe user"() {
        given:
        setup()
        User user2 = new User(username: "user2", password: "password2")
        user = userRepository.save(user)
        user2 = userRepository.save(user2)

        when:
        userService.toggleSubscription(user.id, user2.id)

        then:
        User updatedUser1 = userRepository.findById(user.id).orElse(null)
        User updatedUser2 = userRepository.findById(user2.id).orElse(null)
        updatedUser1.subscriptions.contains(user2.id)
        updatedUser2.subscribers.contains(user.id)

        cleanup:
        userRepository.deleteAll()
    }

    def "should unsubscribe user"() {
        given:
        setup()
        user.id = "user1"
        user.subscriptions = ["user2"]
        User user2 = new User(id: "user2", username: "user2", password: "password2")
        user2.subscribers = ["user1"]
        user = userRepository.save(user)
        user2 = userRepository.save(user2)

        when:
        userService.toggleSubscription(user.id, user2.id)

        then:
        User updatedUser1 = userRepository.findById(user.id).orElse(null)
        User updatedUser2 = userRepository.findById(user2.id).orElse(null)
        !updatedUser1.subscriptions.contains(user2.id)
        !updatedUser2.subscribers.contains(user.id)

        cleanup:
        userRepository.deleteAll()
    }

    def "should get user feed"() {
        given:
        setup()
        user.id = "user1"
        user.likedPosts = ["post2"]
        user.posts = ["post1"]
        user.subscriptions = ["user2"]
        userRepository.save(new User(id: "user2",
                username: "user2",
                password: "password2",
                likedPosts: ["post1"],
                posts: ["post2"]))
        postRepository.save(new Post(id: "post1",
                content: "Post 1",
                authorId: "user1",
                usersWhoLiked: ["user2"],
                comments: ["comment1"]))
        postRepository.save(new Post(id: "post2",
                content: "Post 2",
                authorId: "user2",
                usersWhoLiked: ["user1"],
                comments: ["comment2"]))
        commentRepository.save(new Comment(id: "comment1",
                content: "comment1",
                postId: "post1",
                authorId: "user2"))
        commentRepository.save(new Comment(id: "comment2",
                content: "comment2",
                postId: "post2",
                authorId: "user1"))
        user = userRepository.save(user)

        when:
        UserDTO userFeed = userService.getUserFeed(user.id)

        then:
        userFeed.ownerId == user.id
        userFeed.feed.size() == 2
        userFeed.feed[0].postId == "post1"
        userFeed.feed[0].content == "Post 1"
        userFeed.feed[0].authorId == "user1"
        userFeed.feed[0].comments.size() == 1
        userFeed.feed[0].comments[0].id == "comment1"
        userFeed.feed[0].comments[0].content == "comment1"
        userFeed.feed[0].comments[0].authorId == "user2"
        userFeed.feed[0].comments[0].postId == "post1"
        userFeed.feed[0].usersWhoLiked == ["user2"]
        userFeed.feed[1].postId == "post2"
        userFeed.feed[1].content == "Post 2"
        userFeed.feed[1].authorId == "user2"
        userFeed.feed[1].comments.size() == 1
        userFeed.feed[1].comments[0].id == "comment2"
        userFeed.feed[1].comments[0].content == "comment2"
        userFeed.feed[1].comments[0].authorId == "user1"
        userFeed.feed[1].comments[0].postId == "post2"
        userFeed.feed[1].usersWhoLiked == ["user1"]

        cleanup:
        userRepository.deleteAll()
        postRepository.deleteAll()
        commentRepository.deleteAll()
    }
}