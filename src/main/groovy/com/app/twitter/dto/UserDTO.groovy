package com.app.twitter.dto

class UserDTO {
    private String userId
    private List<FeedDTO> feed = []

    String getOwnerId() {
        userId
    }

    void setOwnerId(String ownerId) {
        this.userId = ownerId
    }

    List<FeedDTO> getFeed() {
        feed
    }

    void setFeed(List<FeedDTO> feed) {
        this.feed = feed
    }
}
