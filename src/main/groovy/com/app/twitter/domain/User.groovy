package com.app.twitter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

import javax.validation.constraints.NotBlank

@Document(collection = "users")
class User {
    @Id
    private String id

    @Indexed(unique = true)
    @NotBlank(message = "Username should not be blank")
    private String username
    private List<String> subscriptions = []
    private List<String> posts = []

    String getId() {
        id
    }

    void setId(String id) {
        this.id = id
    }

    String getUsername() {
        username
    }

    void setUsername(String username) {
        this.username = username
    }

    List<String> getSubscriptions() {
        subscriptions
    }

    void setSubscriptions(List<String> subscriptions) {
        this.subscriptions = subscriptions
    }

    List<String> getPosts() {
        posts
    }

    void setPosts(List<String> posts) {
        this.posts = posts
    }
}
