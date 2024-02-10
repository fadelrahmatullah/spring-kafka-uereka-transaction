package com.core.app.handler;

import com.core.app.constants.ResponseStatus;
import com.core.app.exception.BusinessException;
import com.core.app.exception.ValidationException;
import com.core.app.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @Autowired
    ObjectMapper objectMapper;

    final String ERROR_CODE = "000";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.debug("RestResponseEntityExceptionHandler.handleRuntimeException...");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        Response<Object> response = new Response<>(ResponseStatus.ERROR, dateFormat.format(new Date()));
        response.setMessageCode(ERROR_CODE + "01");
        response.setMessage(ex.getMessage());

        log.error(response.getMessageCode(), ex);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex2) {
            body = ex2.getMessage();

            log.error("Generate response got JsonProcessingException...", ex2);
        }

        return handleExceptionInternal(ex, body, header, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleExceptionGlobal(Exception ex, WebRequest request) {
        log.debug("RestResponseEntityExceptionHandler.handleException...");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        Response<Object> response = new Response<>(ResponseStatus.ERROR, dateFormat.format(new Date()));
            response.setMessageCode(ERROR_CODE + "02");
            response.setMessage(ex.getMessage());
            response.setData(null);
       
        log.error(response.getMessageCode(), ex);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex2) {
            body = ex2.getMessage();

            log.error("Generate response got JsonProcessingException...", ex2);
        }

        return handleExceptionInternal(ex, body, header, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {CannotGetJdbcConnectionException.class})
    protected ResponseEntity<Object> handleExceptionConnectDb(CannotGetJdbcConnectionException ex, WebRequest request) {
        log.debug("DataIntegrityViolationException.handleException...");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        Response<Object> response = new Response<>(ResponseStatus.ERROR, dateFormat.format(new Date()));
            response.setMessageCode(ERROR_CODE + "03");
            response.setMessage(ex.getMessage());
            response.setData(null);
       
        log.error(response.getMessageCode(), ex);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex2) {
            body = ex2.getMessage();

            log.error("Generate response got JsonProcessingException...", ex2);
        }

        return handleExceptionInternal(ex, body, header, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {SQLException.class})
    protected ResponseEntity<Object> handleExceptionSQL(SQLException ex, WebRequest request) {
        log.debug("DataIntegrityViolationException.handleException...");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        Response<Object> response = new Response<>(ResponseStatus.ERROR, dateFormat.format(new Date()));
            response.setMessageCode(ERROR_CODE + "04");
            response.setMessage(ex.getMessage());
            response.setData(null);
       
        log.error(response.getMessageCode(), ex);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex2) {
            body = ex2.getMessage();

            log.error("Generate response got JsonProcessingException...", ex2);
        }

        return handleExceptionInternal(ex, body, header, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    protected ResponseEntity<Object> handleBindException(BindException ex, WebRequest request) {
        log.debug("RestResponseEntityExceptionHandler.handleBindException...");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        Response<Object> response = new Response<>(ResponseStatus.ERROR, dateFormat.format(new Date()));
        
        if (ex.getBindingResult().getAllErrors().size() > 0) {

            response.setMessageCode(ERROR_CODE + "04");
            response.setMessage( String.join(", ", ex.getBindingResult()
            .getAllErrors().stream()
            .map(t -> t.getDefaultMessage()).collect(Collectors.toList())));
            String body = null;
            try {
                body = objectMapper.writeValueAsString(response);
            } catch (JsonProcessingException ex2) {
                body = ex2.getMessage();

                log.error("Generate response got JsonProcessingException...", ex2);
            }
            
             return handleExceptionInternal(ex, body, header, HttpStatus.BAD_REQUEST, request);
        }else{

            response.setMessageCode(ERROR_CODE + "05");
            response.setMessage(ex.getMessage());
            response.setData(null);

            String body = null;
            try {
                body = objectMapper.writeValueAsString(response);
            } catch (JsonProcessingException ex2) {
                body = ex2.getMessage();

                log.error("Generate response got JsonProcessingException...", ex2);
            }
            return handleExceptionInternal(ex, body, header, HttpStatus.BAD_REQUEST, request);
        }
       
    }


    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        log.debug("RestResponseEntityExceptionHandler.handleValidationException...");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        Response<Object> response = new Response<>(ResponseStatus.ERROR, dateFormat.format(new Date()));
        response.setMessageCode(ex.getMessageCode());
        response.setMessage(String.join(", ", ex.getVarValues()));

        log.error(response.getMessageCode(), ex);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex2) {
            body = ex2.getMessage();
        }

        return handleExceptionInternal(ex, body, header, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        log.debug("RestResponseEntityExceptionHandler.handleBusinessException...");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        Response<Object> response = new Response<>(ResponseStatus.ERROR, dateFormat.format(new Date()));
        response.setMessageCode(ex.getMessageCode());
        response.setMessage(String.join(", ", ex.getVarValues()));

        log.error(response.getMessageCode(), ex);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex2) {
            body = ex2.getMessage();
        }

        return handleExceptionInternal(ex, body, header, HttpStatus.NOT_ACCEPTABLE, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @NotNull Object body, HttpHeaders headers
            , HttpStatus status, WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<>(body, headers, status);
    }

}