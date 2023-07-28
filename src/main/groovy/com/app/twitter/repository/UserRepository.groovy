package com.app.twitter.repository

import com.app.twitter.domain.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UserRepository extends MongoRepository<User, String> {
    boolean existsByUsername(String username)

    @Query("{ 'likedPosts' : ?0 }")
    List<User> findAllUsersByLikedPost(String postId);
}