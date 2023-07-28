package com.app.twitter.dto

import com.app.twitter.domain.Comment

class FeedDTO {
    private String postId
    private String content
    private String authorId
    private List<Comment> comments
    private List<String> usersWhoLiked

    String getPostId() {
        return postId
    }

    void setPostId(String postId) {
        this.postId = postId
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

    List<Comment> getComments() {
        return comments
    }

    void setComments(List<Comment> comments) {
        this.comments = comments
    }

    List<String> getUsersWhoLiked() {
        return usersWhoLiked
    }

    void setUsersWhoLiked(List<String> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked
    }
}
