package com.app.twitter.domain

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "posts")
class Post {
   @Id
   private UUID id
   private String content
   private User author
   private List<Comment> comments = []
   private List<User> favorites = []
}
