package com.beto.osworks.api.exception;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.beto.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.beto.osworks.domain.exception.Negocioexception;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	private ResponseEntity<Object> hadlerEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex,
			WebRequest request) {
		var status = HttpStatus.NOT_FOUND;

		var problema = new MensageError();
		problema.setStatus(status.value());
		problema.setTitulo(ex.getMessage());
		problema.setDataHora(OffsetDateTime.now());
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(Negocioexception.class)
	private ResponseEntity<Object> hadlerNegocio(Negocioexception ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;

		var problema = new MensageError();
		problema.setStatus(status.value());
		problema.setTitulo(ex.getMessage());
		problema.setDataHora(OffsetDateTime.now());

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String defaultMensageError, defaultNameError;

		var campos = new ArrayList<MensageError.Campo>();
		for (ObjectError erro : ex.getBindingResult().getAllErrors()) {
			defaultMensageError = messageSource.getMessage(erro, LocaleContextHolder.getLocale());
			defaultNameError = ((FieldError) erro).getField();

			campos.add(new MensageError.Campo(defaultNameError, defaultMensageError));
		}

		var problema = new MensageError();

		problema.setStatus(status.value());
		problema.setTitulo("Um ou mais campos invalidos! Faça a correção e tente novamente");
		problema.setDataHora(OffsetDateTime.now());
		problema.setCampos(campos);

		return super.handleExceptionInternal(ex, problema, headers, status, request);
	}
}
