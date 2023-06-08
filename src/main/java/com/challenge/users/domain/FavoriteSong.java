package com.challenge.users.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "favoriteSongs")
public class FavoriteSong {
    @Id
    private String id;
    private Integer userId;
    private String trackId;
    private String trackName;
    private String artistName;
}
