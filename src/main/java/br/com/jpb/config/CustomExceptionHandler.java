package br.com.jpb.config;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.jpb.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
																  HttpHeaders headers,
																  HttpStatus status, WebRequest request) {

		log.warn("Unexpected HttpMessageNotReadableException catch", ex);
		return handle(HttpStatus.BAD_REQUEST, Collections.emptyList());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = ex
				.getBindingResult()
				.getAllErrors()
				.stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.toList());
		return handle(HttpStatus.BAD_REQUEST, errors);
	}

	private ResponseEntity<Object> handle(HttpStatus status, List<String> errors) {
		return ResponseEntity
				.status(status)
				.body(errors);
	}

	@ExceptionHandler(value = {BaseException.class})
	protected ResponseEntity<Object> handleValidationException(BaseException ex, WebRequest request) {
		return handle(ex.getStatus(), ex.getErrors());
	}

	@ExceptionHandler(value = {ConstraintViolationException.class})
	protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
																		WebRequest request) {
		List<String> errors = ex
				.getConstraintViolations()
				.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());
		return handle(HttpStatus.BAD_REQUEST, errors);
	}
}
