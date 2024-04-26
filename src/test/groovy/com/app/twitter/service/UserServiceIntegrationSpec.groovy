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
        user = new User(username: "test")
    }

    def "should create a user"() {
        given:
        setup()

        when:
        User createdUser = userService.createUser(user)

        then:
        createdUser.username == "test"

        cleanup:
        userService.deleteUserById(createdUser.id)
    }

    def "should update a user"() {
        given:
        setup()
        User createdUser = userRepository.save(user)

        when:
        createdUser.username = "updatedUsername"
        User updatedUser = userService.updateUser(createdUser)

        then:
        updatedUser.username == "updatedUsername"

        cleanup:
        userService.deleteUserById(createdUser.id)
    }

    def "should delete a user and related data"() {
        given:
        setup()
        user.id = "testUser"
        user.posts = ["post1", "post2"]
        User createdUser = userRepository.save(user)
        postRepository.save(new Post(id: "post1", content: "Post 1", authorId: user.id))
        postRepository.save(new Post(id: "post2", content: "Post 2", authorId: user.id))

        when:
        userService.deleteUserById(createdUser.id)

        then:
        !userRepository.existsById(createdUser.id)

        and:
        !postRepository.existsById('post1')
        !postRepository.existsById('post2')

        cleanup:
        userRepository.deleteAll()
        postRepository.deleteAll()
    }

    def "should subscribe user"() {
        given:
        setup()
        User user2 = new User(username: "user2")
        user = userRepository.save(user)
        user2 = userRepository.save(user2)

        when:
        userService.toggleSubscription(user.id, user2.id)

        then:
        User updatedUser1 = userRepository.findById(user.id).orElse(null)
        updatedUser1.subscriptions.contains(user2.id)

        cleanup:
        userRepository.deleteAll()
    }

    def "should unsubscribe user"() {
        given:
        setup()
        user.id = "user1"
        user.subscriptions = ["user2"]
        User user2 = new User(id: "user2", username: "user2")
        user = userRepository.save(user)
        user2 = userRepository.save(user2)

        when:
        userService.toggleSubscription(user.id, user2.id)

        then:
        User updatedUser1 = userRepository.findById(user.id).orElse(null)
        !updatedUser1.subscriptions.contains(user2.id)

        cleanup:
        userRepository.deleteAll()
    }

    def "should get user feed"() {
        given:
        setup()
        user.id = "user1"
        user.posts = ["post1"]
        user.subscriptions = ["user2"]
        userRepository.save(new User(id: "user2",
                username: "user2",
                posts: ["post2"]))
        postRepository.save(new Post(id: "post1",
                content: "Post 1",
                authorId: "user1",
                likes: ["user2"],
                comments: ["comment1"]))
        postRepository.save(new Post(id: "post2",
                content: "Post 2",
                authorId: "user2",
                likes: ["user1"],
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
        userFeed.userId == user.id
        userFeed.feed.size() == 2
        userFeed.feed[0].postId == "post1"
        userFeed.feed[0].content == "Post 1"
        userFeed.feed[0].authorId == "user1"
        userFeed.feed[0].comments.size() == 1
        userFeed.feed[0].comments[0].id == "comment1"
        userFeed.feed[0].comments[0].content == "comment1"
        userFeed.feed[0].comments[0].authorId == "user2"
        userFeed.feed[0].comments[0].postId == "post1"
        userFeed.feed[0].likes == ["user2"]
        userFeed.feed[1].postId == "post2"
        userFeed.feed[1].content == "Post 2"
        userFeed.feed[1].authorId == "user2"
        userFeed.feed[1].comments.size() == 1
        userFeed.feed[1].comments[0].id == "comment2"
        userFeed.feed[1].comments[0].content == "comment2"
        userFeed.feed[1].comments[0].authorId == "user1"
        userFeed.feed[1].comments[0].postId == "post2"
        userFeed.feed[1].likes == ["user1"]

        cleanup:
        userRepository.deleteAll()
        postRepository.deleteAll()
        commentRepository.deleteAll()
    }
}