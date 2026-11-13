package com.squad67.sistema_web_mvc.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Aviso já pronto para exibição: além dos dados da tabela aviso, traz os
 * nomes vindos dos JOINs (campus, turma, autor, espaço) e a contagem de
 * confirmações.
 *
 * turma e espaco podem ser null (aviso do campus inteiro / sem local).
 * expiraEm pode ser null (aviso sem prazo).
 */
public record Aviso(
		Long id,
		TipoAviso tipo,
		String titulo,
		String descricao,
		LocalDateTime criadoEm,
		LocalDateTime expiraEm,
		String campus,
		String turma,
		String autor,
		String espaco,
		long confirmacoes) {

	private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	/** Para quem é o aviso: a turma, ou "Todo o campus" quando não há turma. */
	public String escopo() {
		return turma != null ? turma : "Todo o campus";
	}

	public boolean expirado() {
		return expiraEm != null && expiraEm.isBefore(LocalDateTime.now());
	}

	public String criadoEmFormatado() {
		return criadoEm.format(FORMATO);
	}

	/** Texto vazio quando o aviso não tem prazo. */
	public String expiraEmFormatado() {
		return expiraEm == null ? null : expiraEm.format(FORMATO);
	}

}
