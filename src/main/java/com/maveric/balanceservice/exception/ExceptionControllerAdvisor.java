package com.maveric.balanceservice.exception;

import com.maveric.balanceservice.dto.ErrorDto;
import com.maveric.balanceservice.enums.Constants;
import feign.FeignException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletResponse;

import static com.maveric.balanceservice.enums.Constants.BAD_REQUEST_CODE;

import java.util.HashMap;
import java.util.Map;

import static com.maveric.balanceservice.enums.Constants.*;

@ControllerAdvice
@RestControllerAdvice
public class ExceptionControllerAdvisor {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExceptionControllerAdvisor.class);
    String exceptionString="";


    @ExceptionHandler(BalanceIdNotFoundException.class)
    public static final ErrorDto handleBalanceIdNotFoundException(BalanceIdNotFoundException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(BALANCE_NOT_FOUND_CODE);
        errorDto.setMessage(exception.getMessage());
        return errorDto;
    }

    @ExceptionHandler(CustomerIDNotFoundExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorDto handleCustomerIDNotFoundExistsException(CustomerIDNotFoundExistsException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(BALANCE_NOT_FOUND_CODE);
        errorDto.setMessage(exception.getMessage());
        return errorDto;
    }

    @ExceptionHandler(BalanceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static final ErrorDto handleBalanceNotFoundException(BalanceNotFoundException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(BALANCE_NOT_FOUND_CODE);
        errorDto.setMessage(exception.getMessage());
        return errorDto;

    }
    @ExceptionHandler(BalanceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ErrorDto handleBalanceAlreadyExistException(BalanceAlreadyExistException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(BAD_REQUEST_CODE);
        errorDto.setMessage(exception.getMessage());
        exceptionString = exception.getMessage();
        log.error("{}->{}",BAD_REQUEST_CODE,exceptionString);
        return errorDto;
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleFormatException(HttpMessageNotReadableException e)
    {
        ErrorDto error = getError(Constants.CURRENCY_ERROR, String.valueOf(HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    private ErrorDto getError(String message , String code){
        ErrorDto error = new ErrorDto();
        error.setCode(code);
        error.setMessage(message);
        return error;}


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ErrorDto errorDto = new ErrorDto();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        errorDto.setCode(BAD_REQUEST_CODE);
        errorDto.setMessage(BAD_REQUEST_MESSAGE);
        errorDto.setErrors(errors);
        return errorDto;
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorDto> missingPathVariableException(HttpServerErrorException e) {
        ErrorDto error = getError(e.getMessage(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorDto> handleFeignExceptionNotFound(FeignException e, HttpServletResponse response) {
        String message = e.contentUTF8();
        String decode = (String) e.contentUTF8().subSequence(message.lastIndexOf(":\""), message.length()-2);
        ErrorDto error = getError(decode.replace(":\"", ""), String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}