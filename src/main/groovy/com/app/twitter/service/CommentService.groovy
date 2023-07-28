package com.app.twitter.service

import com.app.twitter.domain.Comment

interface CommentService {

    Comment createComment(Comment comment);

    Comment getCommentById(String id);

    void updateComment(String id, Comment updatedComment);

    void deleteCommentById(String id);
}
