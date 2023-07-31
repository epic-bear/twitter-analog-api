package com.app.twitter.dto

import com.app.twitter.domain.Comment

class FeedDTO {
    private String postId
    private String content
    private String authorId
    private List<Comment> comments
    private List<String> likes

    String getPostId() {
        postId
    }

    void setPostId(String postId) {
        this.postId = postId
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

    List<Comment> getComments() {
        comments
    }

    void setComments(List<Comment> comments) {
        this.comments = comments
    }

    List<String> getLikes() {
        likes
    }

    void setLikes(List<String> likes) {
        this.likes = likes
    }
}
