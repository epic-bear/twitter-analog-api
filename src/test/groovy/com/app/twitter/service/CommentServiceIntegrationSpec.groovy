package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.domain.Post
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
class CommentServiceIntegrationSpec extends Specification {
    @Autowired
    CommentService commentService

    @Autowired
    PostService postService

    @Autowired
    CommentRepository commentRepository

    @Autowired
    PostRepository postRepository

    @Autowired
    UserRepository userRepository

    def "should create a comment for a post"() {
        given:
        Post post = new Post(id: "testPost", content: "Test post", authorId: "testUser")
        Post savedPost = postRepository.save(post)
        Comment comment = new Comment(content: "Test comment", postId: post.id, authorId: "testUser")

        when:
        Comment createdComment = commentService.createComment(comment)

        then:
        createdComment.id != null
        createdComment.content == "Test comment"
        createdComment.postId == savedPost.id
        createdComment.authorId == comment.authorId
        postService.getPostById(savedPost.id).comments.contains(createdComment.id)

        cleanup:
        commentRepository.deleteAll()
        postRepository.deleteAll()
    }

    def "should update a comment"() {
        given:
        Comment comment = new Comment(content: "Original comment")
        Comment savedComment = commentRepository.save(comment)
        Comment updatedComment = new Comment(id: savedComment.id, content: "Updated comment")

        when:
        Comment result = commentService.updateComment(savedComment.id, updatedComment)

        then:
        result != null
        result.id == savedComment.id
        result.content == "Updated comment"

        cleanup:
        commentRepository.deleteAll()
    }

    def "should delete a comment"() {
        given:
        Comment comment = new Comment(id: "testComment", content: "Test comment")
        Post post = new Post(id: "testPost", content: "Test post", comments: [comment.id])
        comment.postId = post.id
        Comment savedComment = commentRepository.save(comment)
        Post savedPost = postRepository.save(post)

        when:
        commentService.deleteCommentById(savedComment.id)

        then:
        !commentRepository.existsById(savedComment.id)

        and:
        Post updatedPost = postService.getPostById(savedPost.id)
        updatedPost.comments.isEmpty()

        cleanup:
        commentRepository.deleteAll()
        postRepository.deleteAll()
    }
}
