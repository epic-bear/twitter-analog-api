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

    void setId(String id) {
        this.id = id
    }

    String getPassword() {
        password
    }

    void setPassword(String password) {
        this.password = password
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

    List<String> getSubscribers() {
        subscribers
    }

    void setSubscribers(List<String> subscribers) {
        this.subscribers = subscribers
    }

    List<String> getPosts() {
        return posts
    }

    void setPosts(List<String> posts) {
        this.posts = posts
    }

    List<String> getLikedPosts() {
        return likedPosts
    }

    void setLikedPosts(List<String> likedPosts) {
        this.likedPosts = likedPosts
    }
}
