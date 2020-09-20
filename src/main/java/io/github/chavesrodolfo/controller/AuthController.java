package io.github.chavesrodolfo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.chavesrodolfo.model.representations.AuthenticationRequest;
import io.github.chavesrodolfo.model.representations.AuthenticationResponse;
import io.github.chavesrodolfo.model.representations.MessageResponse;
import io.github.chavesrodolfo.service.impl.CustomUserDetailsServiceImpl;
import io.github.chavesrodolfo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e){
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected Error to authenticate.", e);
            throw e;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt);
    }

    @PutMapping("/password")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public MessageResponse changePassword(Principal principal, @RequestBody PasswordChanger passwordChanger) throws Exception {
        userDetailsService.changePassword(principal.getName(), passwordChanger.oldPassword, passwordChanger.newPassword);
        return new MessageResponse("success", "OK");
    }

    static class PasswordChanger {
        public String oldPassword;
        public String newPassword;
    }


}