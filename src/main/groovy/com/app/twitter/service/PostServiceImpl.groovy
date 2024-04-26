package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.domain.Post
import com.app.twitter.domain.User
import com.app.twitter.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostServiceImpl implements PostService {
    private final PostRepository postRepository
    private final UserService userService
    private final CommentService commentService

    @Autowired
    PostServiceImpl(PostRepository postRepository, @Lazy UserService userService, CommentService commentService) {
        this.postRepository = postRepository
        this.userService = userService
        this.commentService = commentService
    }

    @Override
    Post createPost(Post post) {
        Post savedPost = postRepository.save(post)
        userService.addPost(post)
        savedPost
    }

    @Override
    Post getPostById(String id) {
       postRepository.findById(id).orElseThrow()
    }

    @Override
    Post updatePost(Post updatedPost) {
        Post post = getPostById(updatedPost.id)
        post.content = updatedPost.content ?: post.content
        postRepository.save(post)
    }

    @Override
    void deletePostById(String postId) {
        Post post = getPostById(postId)
        User author = userService.getUserById(post.authorId)
        author.posts.remove(postId)
        userService.updateUser(author)
        if (post.comments) {
            commentService.deleteAllById(post.comments)
        }
        postRepository.deleteById(postId)
    }

    @Override
    void toggleLikeForPost(String postId, String userId) {
        Post post = getPostById(postId)
        if (post.likes.contains(userId)) {
            post.likes.remove(userId)
        } else {
            post.likes.add(userId)
        }
        postRepository.save(post)
    }

    @Override
    void addComment(Comment comment) {
        Post post = getPostById(comment.postId)
        post.comments.add(comment.id)
        postRepository.save(post)
    }

    @Override
    List<Comment> getComments(String postId) {
        commentService.getCommentsByPostId(postId)
    }

    @Override
    void deleteAllById(List<String> postIds) {
        postIds.each { postId ->
            deletePostById(postId)
        }
    }
}
