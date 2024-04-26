package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.domain.Post
import com.app.twitter.domain.User
import com.app.twitter.dto.FeedDTO
import com.app.twitter.dto.UserDTO
import com.app.twitter.exception.UsernameExistsException
import com.app.twitter.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl implements UserService {
    private final UserRepository userRepository
    private final PostService postService
    private final CommentService commentService

    @Autowired
    UserServiceImpl(UserRepository userRepository, CommentService commentService, PostService postService) {
        this.userRepository = userRepository
        this.commentService = commentService
        this.postService = postService
    }

    @Override
    User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameExistsException("Username ${user.getUsername()} already exists")
        }
        userRepository.save(user)
    }

    @Override
    User getUserById(String id) {
        userRepository.findById(id).orElseThrow()
    }

    @Override
    User updateUser(String id, User updatedUser) {
        User user = getUserById(id)
        user.username = updatedUser.username ?: user.username
        user.posts = updatedUser.posts != null ? updatedUser.posts : user.posts
        user.subscriptions = updatedUser.subscriptions != null ? updatedUser.subscriptions : user.subscriptions
        userRepository.save(user)
    }

    @Override
    void deleteUserById(String userId) {
        User user = getUserById(userId)
        List<String> postIds = user.posts
        postService.deleteAllById(postIds)
        userRepository.deleteById(userId)
    }

    @Override
    void toggleSubscription(String userId, String targetUserId) {
        User user = getUserById(userId)
        if (user.subscriptions.contains(targetUserId)) {
            user.subscriptions.remove(targetUserId)
        } else {
            user.subscriptions.add(targetUserId)
        }
        updateUser(userId, user)
    }

    @Override
    void addPost(Post post) {
        User user = getUserById(post.authorId)
        user.posts.add(post.id)
        updateUser(user.id, user)
    }

    @Override
    UserDTO getUserFeed(String userId) {
        User user = getUserById(userId)
        List<FeedDTO> feed = user.getPosts().collect { postId ->
            fetchPostData(postId)
        }
        user.getSubscriptions().each { subscriptionUserId ->
            User subscriptionUser = getUserById(subscriptionUserId)
            subscriptionUser.getPosts().each { postId ->
                feed << fetchPostData(postId)
            }
        }
        new UserDTO(userId: user.getId(), feed: feed)
    }

    private FeedDTO fetchPostData(String postId) {
        Post post = postService.getPostById(postId)
        List<Comment> comments = commentService.getCommentsByPostId(postId)
        List<String> usersWhoLiked = post.likes
        new FeedDTO(
                postId: post.getId(),
                content: post.getContent(),
                authorId: post.getAuthorId(),
                comments: comments,
                likes: usersWhoLiked
        )
    }
}
