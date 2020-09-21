package io.github.chavesrodolfo.exceptions;

public class TargetNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public TargetNotFoundException(String uuid) {
        super(String.format("Could not find target %s", uuid)); 
    }
}