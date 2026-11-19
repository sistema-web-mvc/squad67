package com.squad67.sistema_web_mvc.exception;

/**
 * Lançada pelo service quando os dados enviados quebram uma regra da aplicação
 * (por exemplo, turma que não pertence ao campus escolhido).
 * Guarda o campo do formulário a que o erro se refere, para o controller
 * mostrar a mensagem junto dele.
 */
public class RegraDeNegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String campo;

	public RegraDeNegocioException(String campo, String mensagem) {
		super(mensagem);
		this.campo = campo;
	}

	public String getCampo() {
		return campo;
	}

}
