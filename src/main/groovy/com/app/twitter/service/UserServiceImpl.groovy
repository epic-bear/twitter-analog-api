package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.domain.Post
import com.app.twitter.domain.User
import com.app.twitter.dto.FeedDTO
import com.app.twitter.dto.UserDTO
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
            throw new Exception("Username ${user.getUsername()} already exists")
        }
        userRepository.save(user)
    }

    @Override
    User getUserById(String id) {
        Optional<User> optionalUser = userRepository.findById(id)
        if (optionalUser.isPresent()) {
            optionalUser.get()
        } else {
            throw new Exception('User not found')
        }
    }

    @Override
    User updateUser(String id, User updatedUser) {
        User user = getUserById(id)
        user.username = updatedUser.username ?: user.username
        user.password = updatedUser.password ?: user.password
        user.posts = updatedUser.posts != null ? updatedUser.posts : user.posts
        user.likedPosts = updatedUser.likedPosts != null ? updatedUser.likedPosts : user.likedPosts
        user.subscribers = updatedUser.subscribers != null ? updatedUser.subscribers : user.subscribers
        user.subscriptions = updatedUser.subscriptions != null ? updatedUser.subscriptions : user.subscriptions
        userRepository.save(user)
    }

    @Override
    void deleteUserById(String userId) {
        User user = getUserById(userId)
        List<String> postIds = user.posts
        postService.deleteAllById(postIds)
        List<String> subscribers = user.subscribers
        subscribers.each { subscriberId ->
            User subscriber = getUserById(subscriberId)
            subscriber.subscriptions.remove(userId);
            userRepository.save(subscriber)
        }
        userRepository.deleteById(userId)
    }

    @Override
    void toggleSubscription(String userId, String targetUserId) {
        User user = getUserById(userId)
        User targetUser = getUserById(targetUserId)
        if (user.subscriptions && user.subscriptions.contains(targetUserId)) {
            user.subscriptions.remove(targetUserId)
            targetUser.subscribers.remove(userId)
        } else {
            if (!user.subscriptions) {
                user.subscriptions = []
            }
            user.subscriptions.add(targetUserId)
            if (!targetUser.subscribers) {
                targetUser.subscribers = []
            }
            targetUser.subscribers.add(userId)
        }
        updateUser(userId, user)
        updateUser(targetUserId, targetUser)
    }

    @Override
    void addPost(Post post) {
        User user = getUserById(post.authorId)
        if(!user.posts) {
            user.posts =  []
        }
        user.posts.add(post.id)
        updateUser(user.id, user)
    }

    @Override
    UserDTO getUserFeed(String userId) {
        User user = getUserById(userId)
        List<FeedDTO> feed = user.getPosts().collect { postId ->
            fetchPostData(postId)
        }
        user.getSubscriptions().each { subscribedUserId ->
            User subscribedUser = getUserById(subscribedUserId)
            subscribedUser.getPosts().each { postId ->
                feed << fetchPostData(postId)
            }
        }
        new UserDTO(ownerId: user.getId(), feed: feed)
    }

    @Override
    List<User> getAllUsersWhoLikedPost(String postId) {
        return userRepository.findAllUsersByLikedPost(postId)
    }

    private FeedDTO fetchPostData(String postId) {
        Post post = postService.getPostById(postId)
        List<Comment> comments = commentService.getCommentsByPostId(postId)
        List<String> usersWhoLiked = post.usersWhoLiked
        new FeedDTO(
                postId: post.getId(),
                content: post.getContent(),
                authorId: post.getAuthorId(),
                comments: comments,
                usersWhoLiked: usersWhoLiked
        )
    }
}
