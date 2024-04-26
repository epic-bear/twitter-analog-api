package com.app.twitter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Document(collection = "comments")
class Comment {
    @Id
    private String id

    @NotBlank(message = "Comment should not be blank")
    private String content

    private String authorId

    @NotNull
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

    String getPostId() {
        postId
    }

    void setPostId(String postId) {
        this.postId = postId
    }
}
