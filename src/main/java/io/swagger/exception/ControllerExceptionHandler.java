package io.swagger.exception;

import io.swagger.controller.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
@SuppressWarnings({"unchecked","rawtypes"})
public class ControllerExceptionHandler
{
    //Response for non-readable http messages, mostly thrown by invalid json objects in posts.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final ErrorMessage handleUnreadableHttpMessage(HttpMessageNotReadableException exception, WebRequest request){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                exception.getMostSpecificCause().toString());
    }

    //This exception is returned by the spring boot validator, and displays the validation messages concatenated as one string.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ErrorMessage handleAllSpringBootExceptions(MethodArgumentNotValidException exception, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : exception.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                details.toString());
    }

    //Thrown during e.g converting string to number. (Invalid parsing)
    @ExceptionHandler(java.lang.NumberFormatException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage numberFormatExceptionHandler(Exception ex, WebRequest request){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                (ex.getMessage().isEmpty()) ? "Numbers/amount are entered in incorrect form, enters amounts like 10.00 or 10." : ex.getMessage()
        );
    }

    //Catches the ApiException
    @ExceptionHandler(ApiException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage apiExceptionHandler(Exception ex, WebRequest request){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage());
    }

    //General exception handling for IllegalArgumentException.
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final ErrorMessage handleIllegalArgumentException(IllegalArgumentException exception, WebRequest request){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                exception.getMessage());
    }

    //General exception handling for NullPointers.
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final ErrorMessage handleUnreadableHttpMessage(NullPointerException exception, WebRequest request){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage badRequestException(BadRequestException ex, WebRequest request)
    {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage unauthorizedException(ConstraintViolationException ex, WebRequest request)
    {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage());
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage unauthorizedException(MethodArgumentTypeMismatchException ex, WebRequest request)
    {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Invalid :" + ex.getParameter().getParameterName());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage unauthorizedException(UnauthorizedException ex, WebRequest request)
    {
        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage forbiddenException(ForbiddenException ex, WebRequest request)
    {
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage());
    }



    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage conflictException(ConflictException ex, WebRequest request)
    {
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request)
    {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage());
    }

    @ExceptionHandler(UnProcessableEntityException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage unProcessableEntityException(Exception ex, WebRequest request)
    {
        return new ErrorMessage(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                new Date(),
                ex.getMessage());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request)
    {
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage());
    }
}