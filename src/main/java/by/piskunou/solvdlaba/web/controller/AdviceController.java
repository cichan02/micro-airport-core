package by.piskunou.solvdlaba.web.controller;

import by.piskunou.solvdlaba.domain.exception.ResourceAlreadyExistsException;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.web.dto.ErrorResponseDTO;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler({ResourceNotExistsException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleNotFoundException(Exception e) {
        return new ErrorResponseDTO(e.getMessage());
    }

    @ExceptionHandler({ResourceAlreadyExistsException.class, IllegalArgumentException.class, ValidationException.class,
                       JWTVerificationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleBadRequestException(Exception e) {
        return new ErrorResponseDTO(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handleAccessDeniedException(Exception e) {
        return new ErrorResponseDTO(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponseDTO> handleBindException(MethodArgumentNotValidException e) {
        List<ErrorResponseDTO> responses = new ArrayList<>();
        responses.addAll(e.getGlobalErrors()
                          .stream()
                          .map(globalError -> new ErrorResponseDTO(globalError.getObjectName(), globalError.getDefaultMessage()))
                          .toList());
        responses.addAll(e.getFieldErrors()
                          .stream()
                          .map(fieldError -> new ErrorResponseDTO(fieldError.getObjectName() + "."
                                + fieldError.getField(), fieldError.getDefaultMessage()))
                          .toList());
        return responses;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleException(Exception e) {
        e.printStackTrace();
        return new ErrorResponseDTO("Internal Server Error. Please try later");
    }

}
