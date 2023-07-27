package com.app.twitter.repository

import com.app.twitter.domain.Post
import org.springframework.data.mongodb.repository.MongoRepository

interface PostRepository extends MongoRepository<Post, UUID> {

}