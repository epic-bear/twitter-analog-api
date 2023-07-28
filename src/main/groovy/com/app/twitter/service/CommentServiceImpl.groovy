package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.repository.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository

    @Autowired
    CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository
    }

    @Override
    Comment createComment(Comment comment) {
        commentRepository.save(comment)
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
    void updateComment(String id, Comment updatedComment) {
        Comment comment = getCommentById(id)
        comment.content = updatedComment.content ?: comment.content
        commentRepository.save(updatedComment)
    }

    @Override
    void deleteCommentById(String id) {
        commentRepository.deleteById(id)
    }
}
