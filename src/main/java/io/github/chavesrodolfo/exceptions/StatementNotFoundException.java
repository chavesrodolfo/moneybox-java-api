package io.github.chavesrodolfo.exceptions;

public class StatementNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public StatementNotFoundException(String uuid) {
        super(String.format("Could not find statement %s", uuid)); 
    }
}