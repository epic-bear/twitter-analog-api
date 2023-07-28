package com.app.twitter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "comments")
class Comment {
    @Id
    private String id
    private String content
    private String authorId
    private String postId

    String getId() {
        id
    }

    void setId(String id) {
        this.id = id
    }

    String getContent() {
        content
    }

    void setContent(String content) {
        this.content = content
    }

    String getAuthorId() {
        authorId
    }

    void setAuthor(String authorId) {
        this.authorId = authorId
    }

    String getPostId() {
        postId
    }

    void setPostId(String postId) {
        this.postId = postId
    }
}
