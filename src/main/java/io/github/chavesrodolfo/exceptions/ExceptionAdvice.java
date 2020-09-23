package io.github.chavesrodolfo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.chavesrodolfo.model.representations.MessageResponse;

@ControllerAdvice
public class ExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidPasswordPatternException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
    MessageResponse invalidPasswordPatternHandler(InvalidPasswordPatternException e) {
        return new MessageResponse(HttpStatus.PRECONDITION_REQUIRED.toString(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    MessageResponse userNotFoundHandler(UserNotFoundException e) {
        return new MessageResponse(HttpStatus.NOT_FOUND.toString(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(TargetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    MessageResponse targetNotFoundHandler(TargetNotFoundException e) {
        return new MessageResponse(HttpStatus.NOT_FOUND.toString(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(StatementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    MessageResponse statementNotFoundHandler(StatementNotFoundException e) {
        return new MessageResponse(HttpStatus.NOT_FOUND.toString(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    MessageResponse invalidArgumentsHandler(InvalidDataException e) {
        return new MessageResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
    }
}