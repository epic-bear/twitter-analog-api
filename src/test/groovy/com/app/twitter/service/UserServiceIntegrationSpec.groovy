package com.app.twitter.service

import com.app.twitter.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureDataMongo
class UserServiceIntegrationSpec extends Specification {

    @Autowired
    UserService userService

    def "should create a user"() {
        given:
        User user = new User(username: "test", password: "testPassword")

        when:
        User createdUser = userService.createUser(user)

        then:
        createdUser.username == "test"
        createdUser.password == "testPassword"

        cleanup:
        userService.deleteUserById(createdUser.id)
    }
}