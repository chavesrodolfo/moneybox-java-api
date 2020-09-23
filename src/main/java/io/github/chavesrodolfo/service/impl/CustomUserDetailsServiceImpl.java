package io.github.chavesrodolfo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import io.github.chavesrodolfo.repository.UserRepository;
import io.github.chavesrodolfo.exceptions.InvalidPasswordPatternException;
import io.github.chavesrodolfo.model.representations.CustomUserDetails;
import io.github.chavesrodolfo.model.User;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
        log.debug("Username {} located", username);
        return user.map(CustomUserDetails::new).get();
    }

	public void changePassword(String username, String oldPassword, String newPassword) throws UsernameNotFoundException, BadCredentialsException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
        log.debug("Username {} located", username);

        verifyPasswordMatches(oldPassword, user);

        verifyPasswordFormat(newPassword);

        user.get().setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(4)));
        userRepository.save(user.get());
        log.info("Password updated for username {}", username);
	}

    /**
     * Verify if the oldPassword matches with user password in database
     * 
     * @param oldPassword
     * @param user
     */
    private void verifyPasswordMatches(String oldPassword, Optional<User> user) {
        if (BCrypt.checkpw(oldPassword, user.get().getPassword())) {
            log.debug("Password match for username {}", user.get().getUsername());
        }
        else {
            String errorMessage = String.format("Password does not match for username %s", user.get().getUsername());
            log.debug(errorMessage);
            throw new BadCredentialsException(errorMessage);
        }
    }

    /**
     * Password format verification with the following rules for regex.
     * 
     * (?=.*[0-9]) a digit must occur at least once
     * (?=.*[a-z]) a lower case letter must occur at least once
     * (?=.*[A-Z]) an upper case letter must occur at least once
     * (?=.*[@#$%^&+=]) a special character must occur at least once
     * (?=\\S+$) no whitespace allowed in the entire string
     * .{8,} at least 8 characters
     * @param password
     */
    private void verifyPasswordFormat(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        if(!password.matches(pattern)) {
            throw new InvalidPasswordPatternException();
        }
        
    }

}