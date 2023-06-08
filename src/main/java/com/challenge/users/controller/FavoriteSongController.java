package com.challenge.users.controller;

import com.challenge.users.domain.FavoriteSong;
import com.challenge.users.domain.dto.FavoriteSongRequest;
import com.challenge.users.repository.FavoriteSongRepository;
import com.challenge.users.security.domain.User;
import com.challenge.users.security.repository.UserRepository;
import com.challenge.users.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavoriteSongController {

    private final SpotifyService spotifyService;
    private final FavoriteSongRepository favoriteSongRepository;
    private final UserRepository userRepository;

    private String clientId = System.getenv("CLIENT_ID");

    private String clientSecret = System.getenv("CLIENT_SECRET_KEY");

    @GetMapping("/search")
    public ResponseEntity<?> searchTracks(@RequestParam("query") String query) {
        try {
            String accessToken = spotifyService.getSpotifyAccessToken(clientId, clientSecret);

            String url = "https://api.spotify.com/v1/search?type=track&q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search/id")
    public ResponseEntity<?> searchTracksById(@RequestParam("trackId") String trackId) {
        try {
            String accessToken = spotifyService.getSpotifyAccessToken(clientId, clientSecret);

            String url = "https://api.spotify.com/v1/tracks/" + URLEncoder.encode(trackId, StandardCharsets.UTF_8);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException  e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/favorite-songs/{userId}")
    public List<FavoriteSong> getFavoriteSongsByUserId(@PathVariable Integer userId) {
        return spotifyService.getFavoriteSongsByUserId(userId);
    }

    @PostMapping("/add-favorite")
    public ResponseEntity<?> addFavoriteSong(@RequestBody FavoriteSongRequest request) {
        FavoriteSongRequest newRequest = FavoriteSongRequest.builder()
                .userId(request.getUserId())
                .trackId(request.getTrackId())
                .build();
        try {
            Optional<User> optionalUser = userRepository.findById(request.getUserId());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            if (spotifyService.favoriteSongExistsByUserIdAndTrackId(request.getUserId(), newRequest.getTrackId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Song already exists in favorites");
            }

            String accessToken = spotifyService.getSpotifyAccessToken(clientId, clientSecret);
            String url = "https://api.spotify.com/v1/tracks/" + newRequest.getTrackId();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String responseBody = response.getBody();

            FavoriteSong favoriteSong = spotifyService.parseFavoriteSong(responseBody, newRequest.getUserId());


            favoriteSongRepository.save(favoriteSong);

            return ResponseEntity.ok(favoriteSong);
        } catch (HttpClientErrorException  e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
