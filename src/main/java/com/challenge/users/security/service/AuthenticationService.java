package com.challenge.users.security.service;

import com.challenge.users.security.domain.dto.AuthenticationRequest;
import com.challenge.users.security.domain.dto.AuthenticationResponse;
import com.challenge.users.security.domain.dto.RegisterRequest;

public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequest request);
    public AuthenticationResponse authenticate(AuthenticationRequest request);
}
