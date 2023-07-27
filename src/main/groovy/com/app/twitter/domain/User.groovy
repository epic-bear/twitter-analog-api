package com.app.twitter.domain

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "users")
class User {
    @Id
    private UUID id
    private String password

    @Indexed(unique = true)
    private String username
    private List<User> subscriptions = []
    private List<Post> posts = []
    private List<Post> favorites = []
}
