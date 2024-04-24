## API Guide

### User endpoints:
##### POST /api/user - Create a new user.
##### PUT /api/user/{id} - Update a user.
##### DELETE /api/user/{id} - Delete a user.
##### POST /api/user/{id}/subscription/{targetUserId} - Subscribe/Unsubscribe. 
##### GET /api/user/{id}/feed - Get user's feed. 
##### GET /api/user/subscription/{targetUserId}/feed - Get another user's feed.

### Post endpoints:
##### POST /api/post - Create a new post. 
##### PUT /api/post/{id} - Update a post. 
##### DELETE /api/post/{id} - Delete a post.
##### PUT /api/post/{id}/like/{userId} - Like/Unlike a post. 
##### GET /api/post/{id}/comments - Get comments for a post. 

### Comment endpoints:
##### POST /api/comment - Create a new comment. 

