package io.github.chavesrodolfo.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public UserNotFoundException(String uuid) {
        super(String.format("Could not find user %s", uuid)); 
    }
}