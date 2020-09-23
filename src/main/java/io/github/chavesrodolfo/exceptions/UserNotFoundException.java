package io.github.chavesrodolfo.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public UserNotFoundException(String user) {
        super(String.format("Could not find user %s", user)); 
    }
}