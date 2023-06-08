package com.challenge.users.repository;

import com.challenge.users.domain.FavoriteSong;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteSongRepository extends MongoRepository<FavoriteSong, String> {
    boolean existsByUserIdAndTrackId(Integer userId, String trackId);

    List<FavoriteSong> findByUserId(Integer userId);
}
