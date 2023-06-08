package com.challenge.users.test;

import com.challenge.users.security.controller.AuthenticationController;
import com.challenge.users.security.domain.dto.AuthenticationRequest;
import com.challenge.users.security.domain.dto.AuthenticationResponse;
import com.challenge.users.security.domain.dto.RegisterRequest;
import com.challenge.users.security.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        ResponseEntity<AuthenticationResponse> expectedResponse = ResponseEntity.ok(authenticationResponse);

        when(authenticationService.register(registerRequest)).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> actualResponse = authenticationController.register(registerRequest);

        verify(authenticationService, times(1)).register(registerRequest);

        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        assertEquals(expectedResponse.getBody(), actualResponse.getBody());
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("ricardoj2811@gmail.com", "1234");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        ResponseEntity<AuthenticationResponse> expectedResponse = ResponseEntity.ok(authenticationResponse);

        when(authenticationService.authenticate(authenticationRequest)).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> actualResponse = authenticationController.authenticate(authenticationRequest);

        verify(authenticationService, times(1)).authenticate(authenticationRequest);

        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        assertEquals(expectedResponse.getBody(), actualResponse.getBody());
    }
}
