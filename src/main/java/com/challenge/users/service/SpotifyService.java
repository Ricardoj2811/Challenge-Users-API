package com.challenge.users.service;

import com.challenge.users.domain.FavoriteSong;
import com.challenge.users.domain.dto.SpotifyAuthResponse;
import com.challenge.users.repository.FavoriteSongRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotifyService {

    private final FavoriteSongRepository favoriteSongRepository;

    public String getSpotifyAccessToken(String clientId, String clientSecret) {
        try {
            String url = "https://accounts.spotify.com/api/token";
            String authString = clientId + ":" + clientSecret;
            String base64AuthString = Base64.getEncoder().encodeToString(authString.getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Basic " + base64AuthString);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SpotifyAuthResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, SpotifyAuthResponse.class);

            SpotifyAuthResponse authResponse = response.getBody();
            if (authResponse != null) {
                return authResponse.getAccessToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public FavoriteSong parseFavoriteSong(String responseBody, Integer userId) {
        FavoriteSong favoriteSong = null;

        try {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject responseJson = parser.parse(responseBody).getAsJsonObject();
            String trackId = responseJson.get("id").getAsString();;
            String trackName = responseJson.get("name").getAsString();
            String artistName = responseJson.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();

            favoriteSong = new FavoriteSong();
            favoriteSong.setUserId(userId);
            favoriteSong.setTrackId(trackId);
            favoriteSong.setTrackName(trackName);
            favoriteSong.setArtistName(artistName);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return favoriteSong;
    }

    public List<FavoriteSong> getFavoriteSongsByUserId(Integer userId) {
        return favoriteSongRepository.findByUserId(userId);
    }

    public boolean favoriteSongExistsByUserIdAndTrackId(Integer userId, String trackId) {
        return favoriteSongRepository.existsByUserIdAndTrackId(userId, trackId);
    }
}
