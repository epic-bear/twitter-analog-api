package com.app.twitter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class User {
    @Id
    private String id
    private String password

    @Indexed(unique = true)
    private String username
    private List<String> subscriptions = []
    private List<String> subscribers = []
    private List<String> posts = []
    private List<String> likedPosts = []

    String getId() {
        id
    }

    String getPassword() {
        password
    }

    List<String> getSubscribers() {
        return subscribers
    }

    void setSubscribers(List<String> subscribers) {
        this.subscribers = subscribers
    }

    String getUsername() {
        username
    }

    List<String> getSubscriptions() {
        subscriptions
    }

    List<String> getPosts() {
        posts
    }

    List<String> getLikedPosts() {
        likedPosts
    }

    void setId(String id) {
        this.id = id
    }

    void setPassword(String password) {
        this.password = password
    }

    void setUsername(String username) {
        this.username = username
    }
}
