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
    void updateUser(String id, User user) {
        Optional<User> optionalUser = userRepository.findById(id)
        if (optionalUser.isPresent()) {
            User updatedUser = optionalUser.get()
            updatedUser.username = user.username
            updatedUser.password = user.password
            userRepository.save(updatedUser)
        }
    }

    @Override
    void deleteUserById(String id) {
        userRepository.deleteById(id)
    }
}
