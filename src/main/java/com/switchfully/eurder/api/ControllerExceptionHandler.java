package com.switchfully.eurder.api;

import com.switchfully.eurder.domain.exceptions.ItemAlreadyExistsException;
import com.switchfully.eurder.domain.exceptions.UnauthorizedException;
import com.switchfully.eurder.domain.exceptions.UnknownUserException;
import com.switchfully.eurder.domain.exceptions.WrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    protected void illegalArgumentException(IllegalArgumentException ex, HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
    @ExceptionHandler(UnauthorizedException.class)

    protected void unauthorizedException(UnauthorizedException ex, HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
    @ExceptionHandler(UnknownUserException.class)
    protected void unknownUserException(UnknownUserException ex, HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
    @ExceptionHandler(WrongPasswordException.class)
    protected void wrongPasswordException(WrongPasswordException ex, HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    protected void itemAlreadyExistsException(ItemAlreadyExistsException ex, HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}


