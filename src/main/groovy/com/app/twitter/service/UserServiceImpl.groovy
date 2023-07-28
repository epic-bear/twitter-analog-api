package com.app.twitter.service

import com.app.twitter.domain.User
import com.app.twitter.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl implements UserService {
    private final UserRepository userRepository

    @Autowired
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository
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
    List<User> getAllUsers() {
        userRepository.findAll()
    }

    @Override
    void updateUser(String id, User updatedUser) {
        User user = getUserById(id)
        user.username = updatedUser.username ?: user.username
        user.password = updatedUser.password ?: user.password
        user.posts = updatedUser.posts ?: user.posts
        user.likedPosts = updatedUser.likedPosts ?: user.likedPosts
        user.subscribers = updatedUser.subscribers ?: user.subscribers
        user.password = updatedUser.subscriptions ?: user.subscriptions
        userRepository.save(updatedUser)
    }

    @Override
    void deleteUserById(String id) {
        userRepository.deleteById(id)
    }

    @Override
    void toggleSubscription(String userId, String targetUserId) {
        User user = getUserById(userId)
        User targetUser = getUserById(targetUserId)

        if (user.subscriptions.contains(targetUserId)) {
            user.subscriptions.remove(targetUserId)
            targetUser.subscribers.remove(userId)
        } else {
            user.subscriptions.add(targetUserId)
            targetUser.subscribers.add(userId)
        }

        updateUser(userId, user)
        updateUser(targetUserId, targetUser)
    }
}
