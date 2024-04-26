package com.app.twitter.service

import com.app.twitter.domain.Comment

interface CommentService {

    Comment createComment(Comment comment)

    List<Comment> getCommentsByPostId(String postId)

    void deleteAllByPostId(String postId)
}
