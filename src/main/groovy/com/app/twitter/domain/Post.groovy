package com.app.twitter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Document(collection = "posts")
class Post {
    @Id
    private String id

    @NotBlank(message = "Post should not be blank")
    private String content

    @NotNull
    private String authorId
    private List<String> comments = []
    private List<String> likes = []

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

    void setAuthorId(String authorId) {
        this.authorId = authorId
    }

    List<String> getComments() {
        comments
    }

    void setComments(List<String> comments) {
        this.comments = comments
    }

    List<String> getLikes() {
        likes
    }

    void setLikes(List<String> likes) {
        this.likes = likes
    }
}
