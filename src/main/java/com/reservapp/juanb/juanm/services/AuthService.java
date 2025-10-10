package com.reservapp.juanb.juanm.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.dto.LoginRequestDTO;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService){
        this.authenticationManager=authenticationManager;
        this. userDetailsService=userDetailsService;
        this.jwtService=jwtService;
    }

    public String login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.cedula(), request.contrasena()));
        
        UserDetails user = userDetailsService.loadUserByUsername(request.cedula());
        return jwtService.generateToken(user);
    }
}
