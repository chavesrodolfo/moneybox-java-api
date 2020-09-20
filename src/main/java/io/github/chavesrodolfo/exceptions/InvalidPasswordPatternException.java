package io.github.chavesrodolfo.exceptions;

public class InvalidPasswordPatternException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public InvalidPasswordPatternException() {
        super("Could not change the password. You must use numbers, letters, and special characters with minimum size of 8"); 
    }
}