package com.app.twitter.domain

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "comments")
class Comment {
    @Id
    private UUID id
    private String content
    private User author
}
