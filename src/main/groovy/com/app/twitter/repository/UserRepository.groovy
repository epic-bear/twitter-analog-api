package com.app.twitter.repository

import com.app.twitter.domain.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository extends MongoRepository<User, UUID> {

}