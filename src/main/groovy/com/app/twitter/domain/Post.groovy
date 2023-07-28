package com.app.twitter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "posts")
class Post {
    @Id
    private String id
    private String content
    private String authorId
    private List<String> comments = []
    private List<String> usersWhoLiked = []

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    String getContent() {
        return content
    }

    void setContent(String content) {
        this.content = content
    }

    String getAuthorId() {
        return authorId
    }

    void setAuthorId(String authorId) {
        this.authorId = authorId
    }

    List<String> getComments() {
        return comments
    }

    void setComments(List<String> comments) {
        this.comments = comments
    }

    List<String> getUsersWhoLiked() {
        return usersWhoLiked
    }

    void setUsersWhoLiked(List<String> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked
    }
}
