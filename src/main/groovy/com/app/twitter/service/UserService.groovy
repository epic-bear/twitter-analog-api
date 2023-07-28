package com.app.twitter.service

import com.app.twitter.domain.Post
import com.app.twitter.domain.User
import com.app.twitter.dto.UserDTO

interface UserService {

    User createUser(User user);

    User getUserById(String id);

    User updateUser(String id, User updatedUser);

    void deleteUserById(String id);

    void toggleSubscription(String userId, String targetUserId);

    void addPost(Post post);

    UserDTO getUserFeed(String userId);

    List<User> getAllUsersWhoLikedPost(String postId)
}
