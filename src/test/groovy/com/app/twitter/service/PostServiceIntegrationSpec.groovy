package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.domain.Post
import com.app.twitter.domain.User
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
class PostServiceIntegrationSpec extends Specification {
    @Autowired
    PostService postService

    @Autowired
    PostRepository postRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    CommentRepository commentRepository
    User user
    Post post

    def setup() {
        user = new User(id: "testUser", username: "testUser", password: "testPassword")
        post = new Post(id: "testPost", content: "test post", authorId: user.id)
    }

    def "should create a post"() {
        given:
        setup()
        userRepository.save(user)

        when:
        Post createdPost = postService.createPost(post)

        then:
        createdPost.id != null
        createdPost.content == "test post"
        createdPost.authorId == "testUser"
        userRepository.findById("testUser").orElse(null).posts.contains(createdPost.id)

        cleanup:
        userRepository.deleteAll()
        postRepository.deleteAll()
    }

    def "should update a post"() {
        given:
        setup()
        userRepository.save(user)
        Post savedPost = postRepository.save(post)

        when:
        Post updatedPost = new Post(
                id: savedPost.id,
                content: "Updated post content",
                comments: ["Comment 1", "Comment 2"],
                likes: [user.id]
        )
        Post result = postService.updatePost(savedPost.id, updatedPost)

        then:
        result != null
        result.id == savedPost.id
        result.content == "Updated post content"
        result.comments == ["Comment 1", "Comment 2"]
        result.likes == [user.id]

        cleanup:
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "should delete a post by id"() {
        given:
        setup()
        Comment comment = commentRepository.save(new Comment(id: "testComment",
                postId: "testPost",
                authorId: "testUser"))
        post.likes = [user.id]
        post.comments = [comment.id]
        user.likedPosts = [post.id]
        userRepository.save(user)

        Post savedPost = postService.createPost(post)

        when:
        postService.deletePostById(savedPost.id)

        then:
        Post deletedPost = postRepository.findById(savedPost.id).orElse(null)
        deletedPost == null

        and:
        User updatedUser = userRepository.findById("testUser").orElse(null)
        updatedUser.posts.isEmpty()
        updatedUser.likedPosts.isEmpty()

        and:
        !commentRepository.existsById(comment.id)

        cleanup:
        postRepository.deleteAll()
        userRepository.deleteAll()
        commentRepository.deleteAll()
    }

    def "should like post"() {
        given:
        setup()
        user = userRepository.save(user)
        Post savedPost = postRepository.save(post)

        when:
        postService.toggleLikePost(savedPost.id, user.id)
        Post updatedPost = postRepository.findById(savedPost.id).orElse(null)
        User updatedUser = userRepository.findById(user.id).orElse(null)

        then:
        updatedPost != null
        updatedPost.likes == [user.id]
        updatedUser.likedPosts == [updatedPost.id]

        cleanup:
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "should unlike post"() {
        given:
        setup()
        user.likedPosts = [post.id]
        user = userRepository.save(user)
        post.likes = ["testUser"]
        Post savedPost = postRepository.save(post)

        when:
        postService.toggleLikePost(savedPost.id, user.id)
        Post updatedPost = postRepository.findById(savedPost.id).orElse(null)
        User updatedUser = userRepository.findById(user.id).orElse(null)

        then:
        updatedPost != null
        updatedPost.likes == []
        updatedUser.likedPosts == []

        cleanup:
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "should get comments for a post"() {
        given:
        setup()
        Comment comment1 = new Comment(id: "comment1",
                content: "Comment 1",
                postId: post.id,
                authorId: user.id)
        Comment comment2 = new Comment(id: "comment2",
                content: "Comment 2",
                postId: post.id,
                authorId: user.id)
        Comment savedComment1 = commentRepository.save(comment1)
        Comment savedComment2 = commentRepository.save(comment2)
        post.comments = ["comment1", "comment2"]
        Post savedPost = postRepository.save(post)
        userRepository.save(user)
        when:
        List<Comment> comments = postService.getComments(savedPost.id)

        then:
        comments.size() == 2
        comments[0].id == savedComment1.id
        comments[1].id == savedComment2.id

        cleanup:
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "should add and remove likes from multiple users"() {
        given:
        setup()
        user = userRepository.save(user)
        Post savedPost = postRepository.save(post)

        def users = (1..4).collect { new User(id: "user$it", username: "user$it", password: "password$it") }
        userRepository.saveAll(users)

        when:
        //4 users liked post
        users.each { user ->
            postService.toggleLikePost(savedPost.id, user.id)
        }

        //2 users unliked post
        users.take(2).each { user ->
            postService.toggleLikePost(savedPost.id, user.id)
        }

        Post updatedPost = postRepository.findById(savedPost.id).orElse(null)

        then:
        updatedPost.likes.size() == 2

        cleanup:
        postRepository.deleteAll()
        userRepository.deleteAll()
    }
}
