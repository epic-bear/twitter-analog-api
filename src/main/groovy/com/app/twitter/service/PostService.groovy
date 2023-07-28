package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.domain.Post

interface PostService {

    Post createPost(Post post);

    Post getPostById(String id);

    void updatePost(String id, Post updatedPost);

    void deletePostById(String id);

    void toggleLikePost(String id, String userId);

    void addComment(Comment comment);

    List<Comment> getComments(String postId);
}