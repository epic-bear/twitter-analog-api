package com.app.twitter.controller

import com.app.twitter.domain.User
import com.app.twitter.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    public static final String USER_PATH = '/api/user'
    public static final String USER_PATH_ID = '/api/user/{id}'

    private final UserService userService

    @Autowired
    UserController(UserService userService) {
        this.userService = userService
    }

    @PostMapping(USER_PATH)
    ResponseEntity createUser(@RequestBody User user) {
        userService.createUser(user)
        new ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping(USER_PATH_ID)
    User getUserById(@PathVariable String id) {
        userService.getUserById(id)
    }

    @GetMapping(USER_PATH)
    List<User> getAllUsers() {
        userService.getAllUsers()
    }

    @PutMapping(USER_PATH_ID)
    User updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        userService.updateUser(id, updatedUser)
    }

    @DeleteMapping(USER_PATH_ID)
    void deleteUserById(@PathVariable String id) {
        userService.deleteUserById(id)
    }
}
