package com.app.twitter.dto

import com.app.twitter.domain.Comment

class FeedDTO {
    private String postId
    private String content
    private String authorId
    private List<Comment> comments
    private List<String> usersWhoLiked
}
