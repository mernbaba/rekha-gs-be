package com.rekha.ecommerce.exception;

import java.util.Map;
import java.util.TreeMap;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.dto.ResponseStatus;
import com.rekha.ecommerce.enumeration.ResponseCode;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(CloudBaseException.class)
	public ResponseEntity<ResponseObject<String>> processCustomException(CloudBaseException exception) {
		// Create a response object, you can create a custom response class if needed
		ResponseObject<String> response = new ResponseObject<>();
		response.setStatusFromEnum(ResponseCode.valueOf(exception.getMessage()));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseObject<String>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ResponseObject<String> response = new ResponseObject<>();
        response.setStatusFromEnum(ResponseCode.BAD_REQUEST);  // Create a specific code for unsupported method
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseObject<String>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        ResponseObject<String> response = new ResponseObject<>();
//        LOG.error("-- handleMissingServletRequestParameterException() - Handled MissingServletRequestParameter exception - {}", ex);
        response.setStatusFromEnum(ResponseCode.MISSING_REQUEST_PARAMETER);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject<Object>> processMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

    	
    	Map<String, String> message = new TreeMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message1 = error.getDefaultMessage();
            message.put("message", message1);
        });

        ResponseStatus status = new ResponseStatus(message);
        
        ResponseObject<Object> response = new ResponseObject<>(message);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseObject<String>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        ResponseObject<String> response = new ResponseObject<>();
        response.setStatusFromEnum(ResponseCode.BAD_REQUEST);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseObject<String>> handleSQLIntegrityConstraintViolationException(
    		ConstraintViolationException ex) {
        ResponseObject<String> response = new ResponseObject<>();
        response.setStatusFromEnum(ResponseCode.DUPLICATE_ENTRY);
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseObject<String>> handleDataIntegrityViolationException(
    		DataIntegrityViolationException ex) {
        ResponseObject<String> response = new ResponseObject<>();
        response.setStatusFromEnum(ResponseCode.DUPLICATE_ENTRY);
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }
    
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseObject<String>> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex) {
        ResponseObject<String> response = new ResponseObject<>();
        response.setStatusFromEnum(ResponseCode.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseObject<String>> handleException(Exception ex) {
        ResponseObject<String> response = new ResponseObject<>();
        response.setStatusFromEnum(ResponseCode.UNKNOWN_ERROR_OCCURRED);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
