package com.app.twitter.controller

import com.app.twitter.domain.Comment
import com.app.twitter.domain.Post
import com.app.twitter.service.PostService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@RestController
class PostController {
    private static final String POST_PATH = '/api/post'
    private static final String POST_PATH_ID = '/api/post/{id}'
    private static final String POST_PATH_LIKE_TOGGLE = '/api/post/{id}/like/{userId}'
    private static final String POST_PATH_GET_COMMENTS = '/api/post/{id}/comments'

    private final PostService postService

    @Autowired
    PostController(PostService postService) {
        this.postService = postService
    }

    @PostMapping(POST_PATH)
    Post createPost(@RequestBody @Valid Post post) {
        postService.createPost(post)
    }

    @PutMapping(POST_PATH)
    Post updatePost(@RequestBody @Valid Post post) {
        postService.updatePost(post)
    }

    @DeleteMapping(POST_PATH_ID)
    ResponseEntity deletePostById(@PathVariable String id) {
        postService.deletePostById(id)
        new ResponseEntity<>(HttpStatus.OK)
    }

    @PutMapping(POST_PATH_LIKE_TOGGLE)
    ResponseEntity toggleLikeForPost(@PathVariable String id, @PathVariable String userId) {
        postService.toggleLikeForPost(id, userId)
        new ResponseEntity<>(HttpStatus.OK)
    }

    @GetMapping(POST_PATH_GET_COMMENTS)
    List<Comment> getCommentsForPost(@PathVariable String id) {
        postService.getComments(id)
    }
}
