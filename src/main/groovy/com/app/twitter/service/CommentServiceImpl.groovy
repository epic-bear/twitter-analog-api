package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.repository.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository
    private final PostService postService

    @Autowired
    CommentServiceImpl(CommentRepository commentRepository, @Lazy PostService postService) {
        this.commentRepository = commentRepository
        this.postService = postService
    }

    @Override
    Comment createComment(Comment comment) {
        Comment savedComment = commentRepository.save(comment)
        postService.addComment(savedComment)
        savedComment
    }

    @Override
    Comment getCommentById(String id) {
        Optional<Comment> optionalComment = commentRepository.findById(id)
        if (optionalComment.isPresent()) {
            optionalComment.get()
        } else {
            throw new Exception('Comment not found')
        }
    }

    @Override
    Comment updateComment(String id, Comment updatedComment) {
        Comment comment = getCommentById(id)
        comment.content = updatedComment.content ?: comment.content
        commentRepository.save(updatedComment)
    }

    @Override
    void deleteCommentById(String id) {
        commentRepository.deleteById(id)
    }

    @Override
    List<Comment> getCommentsByPostId(String postId) {
        commentRepository.findAllByPostId(postId)
    }
}
