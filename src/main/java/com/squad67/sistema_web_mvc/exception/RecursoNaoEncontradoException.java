package com.squad67.sistema_web_mvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Lançada quando um registro pedido não existe. O Spring responde 404. */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RecursoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

}
