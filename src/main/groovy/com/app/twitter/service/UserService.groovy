package com.app.twitter.service

import com.app.twitter.domain.Post
import com.app.twitter.domain.User

interface UserService {

    User createUser(User user);

    User getUserById(String id);

    List<User> getAllUsers();

    void updateUser(String id, User updatedUser);

    void deleteUserById(String id);

    void toggleSubscription(String userId, String targetUserId);

    void addPost(Post post);
}
