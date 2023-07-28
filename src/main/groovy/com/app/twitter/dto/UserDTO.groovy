package com.app.twitter.dto

class UserDTO {
    private String ownerId
    private List<FeedDTO> feed = []

    String getOwnerId() {
        return ownerId
    }

    void setOwnerId(String ownerId) {
        this.ownerId = ownerId
    }

    List<FeedDTO> getFeed() {
        return feed
    }

    void setFeed(List<FeedDTO> feed) {
        this.feed = feed
    }
}
