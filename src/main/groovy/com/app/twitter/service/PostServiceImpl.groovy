package com.app.twitter.service

import com.app.twitter.domain.Comment
import com.app.twitter.domain.Post
import com.app.twitter.domain.User
import com.app.twitter.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
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
    Post updatePost(String id, Post updatedPost) {
        Post post = getPostById(id)
        post.content = updatedPost.content ?: post.content
        post.comments = updatedPost.comments != null ? updatedPost.comments : post.comments
        post.likes = updatedPost.likes != null ? updatedPost.likes : post.likes
        postRepository.save(post)
    }

    @Override
    void deletePostById(String postId) {
        Post post = getPostById(postId)
        User author = userService.getUserById(post.authorId)
        author.posts.remove(postId)
        userService.updateUser(author.id, author)
        if (post.likes) {
            List<User> usersWhoLiked = userService.getAllUsersWhoLikedPost(postId)
            usersWhoLiked.each { user ->
                user.likedPosts.remove(postId)
                userService.updateUser(user.id, user)
            }
        }
        if (post.comments) {
            commentService.deleteAllById(post.comments)
        }
        postRepository.deleteById(postId)
    }

    @Override
    void toggleLikeForPost(String postId, String userId) {
        Post post = getPostById(postId);
        User user = userService.getUserById(userId);

        if (post.likes.contains(userId)) {
            post.likes.remove(userId);
        } else {
            post.likes.add(userId);
        }

        if (user.likedPosts != null && user.likedPosts.contains(postId)) {
            user.likedPosts.remove(postId);
        } else {
            user.likedPosts.add(postId);
        }

        updatePost(postId, post);
        userService.updateUser(userId, user);
    }

    @Override
    void addComment(Comment comment) {
        Post post = getPostById(comment.postId)
        post.comments.add(comment.id)
        updatePost(post.id, post)
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
