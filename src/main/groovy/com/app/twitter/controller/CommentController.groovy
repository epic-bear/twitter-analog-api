package com.app.twitter.controller

import com.app.twitter.domain.Comment
import com.app.twitter.service.CommentService
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController {
    private static final String COMMENT_PATH = '/api/comment'
    private static final String COMMENT_PATH_ID = '/api/comment/{id}'

    private final CommentService commentService

    CommentController(CommentService commentService) {
        this.commentService = commentService
    }

    @PostMapping(COMMENT_PATH)
    Comment createComment(@RequestBody Comment comment) {
        commentService.createComment(comment)
    }

    @PutMapping(COMMENT_PATH_ID)
    Comment updateComment(@PathVariable String id, @RequestBody Comment updatedComment) {
        commentService.updateComment(id, updatedComment)
    }

    @DeleteMapping(COMMENT_PATH_ID)
    ResponseEntity deleteCommentById(@PathVariable String id) {
        commentService.deleteCommentById(id)
        new ResponseEntity<>(HttpStatus.OK)
    }
}
