package com.app.twitter.controller

import com.app.twitter.domain.Comment
import com.app.twitter.service.CommentService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController {
    private static final String COMMENT_PATH = '/api/comment'

    private final CommentService commentService

    CommentController(CommentService commentService) {
        this.commentService = commentService
    }

    @PostMapping(COMMENT_PATH)
    Comment createComment(@RequestBody Comment comment) {
        commentService.createComment(comment)
    }
}
