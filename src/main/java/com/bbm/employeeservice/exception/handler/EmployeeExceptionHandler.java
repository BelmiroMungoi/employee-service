package com.bbm.employeeservice.exception.handler;

import com.bbm.employeeservice.exception.BusinessException;
import com.bbm.employeeservice.exception.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class EmployeeExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<ErrorResponse.Campo> campos = new ArrayList<>();

        for (ObjectError objectError: ex.getBindingResult().getAllErrors()) {
            String name = ((FieldError) objectError).getField();

            String msg = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
            campos.add(new ErrorResponse.Campo(name, msg));
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(status.value() + " ==> " + status.is4xxClientError());
        errorResponse.setTitle("Um ou mais campos estão inválidos! Preencha devidamente o formulário e TENTE NOVAMENTE!!");
        errorResponse.setTime(OffsetDateTime.now());
        errorResponse.setCampos(campos);
        return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse response = new ErrorResponse();
        response.setStatus(status.value() + " ==> " + status.getReasonPhrase());
        response.setTitle(ex.getMessage());
        response.setTime(OffsetDateTime.now());

        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse response = new ErrorResponse();
        response.setStatus(status.value() + " ==> " + status.getReasonPhrase());
        response.setTitle(ex.getMessage());
        response.setTime(OffsetDateTime.now());

        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class })
    protected ResponseEntity<Object> handleExceptionDataIntegrity(Exception ex) {

        String msg = "";

        if (ex instanceof DataIntegrityViolationException) {
            msg = ((DataIntegrityViolationException) ex).getCause().getMessage();

        } else if (ex instanceof ConstraintViolationException) {
            msg = ((ConstraintViolationException) ex).getCause().getMessage();

        } else if (ex instanceof SQLException) {
            msg = ((SQLException) ex).getCause().getMessage();

        } else {
            msg = ex.getMessage();
        }

        ErrorResponse response = new ErrorResponse();
        response.setStatus(
                HttpStatus.INTERNAL_SERVER_ERROR + " ==> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        response.setTitle(msg);
        response.setTime(OffsetDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
