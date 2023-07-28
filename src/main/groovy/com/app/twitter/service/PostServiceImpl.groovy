package com.app.twitter.service

import com.app.twitter.domain.Post
import com.app.twitter.domain.User
import com.app.twitter.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostServiceImpl implements PostService {

    private final PostRepository postRepository

    @Autowired
    PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository
    }

    @Override
    Post createPost(Post post) {
        postRepository.save(post)
    }

    @Override
    Post getPostById(String id) {
        Optional<Post> optionalPost = postRepository.findById(id)
        if (optionalPost.isPresent()) {
            optionalPost.get()
        } else {
            throw new Exception("Post not found")
        }
    }

    @Override
    void updatePost(String id, Post post) {
        Optional<Post> optionalPost = postRepository.findById(id)
        if (optionalPost.isPresent()) {
            Post updatedPost = optionalPost.get()
            updatedPost.content = post.content
            postRepository.save(updatedPost)
        }
    }

    @Override
    void deletePostById(String id) {
        postRepository.deleteById(id)
    }
}
