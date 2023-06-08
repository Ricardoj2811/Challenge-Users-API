package com.challenge.users.test;

import com.challenge.users.controller.FavoriteSongController;
import com.challenge.users.domain.FavoriteSong;
import com.challenge.users.repository.FavoriteSongRepository;
import com.challenge.users.security.domain.User;
import com.challenge.users.security.repository.UserRepository;
import com.challenge.users.service.SpotifyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FavoriteSongControllerTest {
    @Mock
    private SpotifyService spotifyService;

    @Mock
    private FavoriteSongRepository favoriteSongRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FavoriteSongController favoriteSongController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFavoriteSongsByUserId() {
        Integer userId = 1;
        List<FavoriteSong> favoriteSongs = new ArrayList<>();
        favoriteSongs.add(new FavoriteSong());
        favoriteSongs.add(new FavoriteSong());

        when(spotifyService.getFavoriteSongsByUserId(userId)).thenReturn(favoriteSongs);

        List<FavoriteSong> result = favoriteSongController.getFavoriteSongsByUserId(userId);

        assertEquals(favoriteSongs, result);
    }
}
