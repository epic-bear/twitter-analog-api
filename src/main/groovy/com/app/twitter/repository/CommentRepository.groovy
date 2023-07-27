package com.app.twitter.repository

import com.app.twitter.domain.Comment
import org.springframework.data.mongodb.repository.MongoRepository

interface CommentRepository extends MongoRepository<Comment, UUID> {

}