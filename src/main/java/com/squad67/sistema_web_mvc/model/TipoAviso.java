package com.squad67.sistema_web_mvc.model;

/**
 * Tipos de aviso. Os nomes precisam ser iguais aos valores do CHECK
 * ck_aviso_tipo do schema.sql.
 */
public enum TipoAviso {

	AULA_CANCELADA("Aula cancelada"),
	SALA_TROCADA("Sala trocada"),
	AULA_VAGA("Aula vaga"),
	ATRASO("Atraso"),
	COMUNICADO("Comunicado");

	private final String rotulo;

	TipoAviso(String rotulo) {
		this.rotulo = rotulo;
	}

	/** Texto mostrado ao usuário (usado nas telas como ${tipo.rotulo}). */
	public String getRotulo() {
		return rotulo;
	}

}
