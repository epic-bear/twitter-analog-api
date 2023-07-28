package com.app.twitter.service

import com.app.twitter.domain.Post
import com.app.twitter.domain.User
import com.app.twitter.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostServiceImpl implements PostService {
    private final PostRepository postRepository
    private final UserService userService

    @Autowired
    PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository
        this.userService = userService
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
            throw new Exception('Post not found')
        }
    }

    @Override
    void updatePost(String id, Post updatedPost) {
        Post post = getPostById()
        post.content = updatedPost.content
        postRepository.save(post)
    }

    @Override
    void deletePostById(String id) {
        postRepository.deleteById(id)
    }

    @Override
    void toggleLikePost(String postId, String userId) {
        Post post = getPostById(postId)
        User user = userService.getUserById(userId)
        if (post.usersWhoLiked.contains(userId)) {
            post.usersWhoLiked.remove(userId)
            user.likedPosts.remove()
        } else {
            post.usersWhoLiked.add(userId)
            user.likedPosts.add(postId)
        }
        updatePost(postId, post)
        userService.updateUser(userId, user)
    }
}
