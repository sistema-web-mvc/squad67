package com.squad67.sistema_web_mvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.squad67.sistema_web_mvc.exception.RecursoNaoEncontradoException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String recursoNaoEncontrado() {
		return "error/404";
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String erroGeral(Model model) {
		model.addAttribute("mensagemErro", "Ocorreu um erro inesperado. Tente novamente mais tarde.");
		return "error/error";
	}
}