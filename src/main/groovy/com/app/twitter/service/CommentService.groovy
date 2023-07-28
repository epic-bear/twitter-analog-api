package com.app.twitter.service

import com.app.twitter.domain.Comment

interface CommentService {

    Comment createComment(Comment comment);

    Comment getCommentById(String id);

    Comment updateComment(String id, Comment updatedComment);

    void deleteCommentById(String id);

    List<Comment> getCommentsByPostId(String postId);

    void deleteAllById(List<String> comments);
}
