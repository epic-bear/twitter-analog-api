package com.app.twitter.dto

class UserDTO {
    private String ownerId
    private List<FeedDTO> feed = []

    String getOwnerId() {
        ownerId
    }

    void setOwnerId(String ownerId) {
        this.ownerId = ownerId
    }

    List<FeedDTO> getFeed() {
        feed
    }

    void setFeed(List<FeedDTO> feed) {
        this.feed = feed
    }
}
