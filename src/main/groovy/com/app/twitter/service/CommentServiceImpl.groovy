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
    List<Comment> getCommentsByPostId(String postId) {
        commentRepository.findAllByPostId(postId)
    }

    @Override
    void deleteAllById(List<String> comments) {
        commentRepository.deleteAllById(comments)
    }
}
