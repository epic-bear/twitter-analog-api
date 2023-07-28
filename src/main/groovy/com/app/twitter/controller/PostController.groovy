package com.app.twitter.controller

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

@RestController
class PostController {
    private static final String POST_PATH = '/api/post'
    private static final String POST_PATH_ID = '/api/post/{id}'

    private final PostService postService

    @Autowired
    PostController(PostService postService) {
        this.postService = postService
    }

    @PostMapping(POST_PATH)
    ResponseEntity createPost(@RequestBody Post post) {
        postService.createPost(post)
        new ResponseEntity<>(HttpStatus.CREATED)
    }

    @GetMapping(POST_PATH_ID)
    Post getPostById(@PathVariable String id) {
        postService.getPostById(id)
    }

    @PutMapping(POST_PATH_ID)
    ResponseEntity updatePost(@PathVariable String id, @RequestBody Post updatedPost) {
        postService.updatePost(id, updatedPost)
        new ResponseEntity<>(HttpStatus.OK)
    }

    @DeleteMapping(POST_PATH_ID)
    ResponseEntity<Void> deletePostById(@PathVariable String id) {
        postService.deletePostById(id)
        new ResponseEntity<>(HttpStatus.OK)
    }

}