package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.domain.Post
import com.app.twitter.domain.User
import com.app.twitter.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class PostServiceImpl implements PostService {
    private final PostRepository postRepository
    private final UserService userService
    private final CommentService commentService

    @Autowired
    PostServiceImpl(PostRepository postRepository, @Lazy UserService userService, CommentService commentService) {
        this.postRepository = postRepository
        this.userService = userService
        this.commentService = commentService
    }

    @Override
    Post createPost(Post post) {
        Post savedPost = postRepository.save(post)
        userService.addPost(post)
        savedPost
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
    Post updatePost(String id, Post updatedPost) {
        Post post = getPostById(id)
        post.content = updatedPost.content ?: post.content
        post.comments = updatedPost.comments ?: post.comments
        post.usersWhoLiked = updatedPost.usersWhoLiked ?: post.usersWhoLiked
        postRepository.save(post)
    }

    @Override
    void deletePostById(String postId) {
        Post post = getPostById(postId)
        List<User> usersWhoLiked = userService.getAllUsersWhoLikedPost(postId)
        usersWhoLiked.each { user ->
            user.likedPosts.remove(postId)
            userService.updateUser(user.id, user)
        }
        commentService.deleteAllById(post.comments)
        postRepository.deleteById(postId)
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

    @Override
    void addComment(Comment comment) {
        Post post = getPostById(comment.postId)
        post.comments.add(comment.id)
        updatePost(post.id, post)
    }

    @Override
    List<Comment> getComments(String postId) {
        commentService.getCommentsByPostId(postId)
    }

    @Override
    void deleteAllById(List<String> postIds) {
        postIds.each { postId ->
            deletePostById(postId)
        }
    }
}
