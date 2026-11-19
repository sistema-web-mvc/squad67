package com.squad67.sistema_web_mvc.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Dados do formulário de aviso (criar e editar).
 *
 * É uma classe comum, com getters e setters, porque o th:field do Thymeleaf
 * e o @ModelAttribute do Spring leem e gravam os campos por esses métodos.
 *
 * Os campos "...Id" guardam o id escolhido nos selects da tela.
 * turmaId, espacoId e expiraEm são opcionais.
 */
public class AvisoForm {

	private Long id; // null = aviso novo
	private TipoAviso tipo;
	private String titulo;
	private String descricao;
	private Long campusId;
	private Long turmaId;
	private Long espacoId;
	private Long autorId;

	// <input type="datetime-local"> envia no formato 2026-11-25T18:30
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime expiraEm;

	public AvisoForm() {
		this.tipo = TipoAviso.COMUNICADO; // valor sugerido na tela de novo aviso
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public TipoAviso getTipo() { return tipo; }
	public void setTipo(TipoAviso tipo) { this.tipo = tipo; }

	public String getTitulo() { return titulo; }
	public void setTitulo(String titulo) { this.titulo = titulo; }

	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }

	public Long getCampusId() { return campusId; }
	public void setCampusId(Long campusId) { this.campusId = campusId; }

	public Long getTurmaId() { return turmaId; }
	public void setTurmaId(Long turmaId) { this.turmaId = turmaId; }

	public Long getEspacoId() { return espacoId; }
	public void setEspacoId(Long espacoId) { this.espacoId = espacoId; }

	public Long getAutorId() { return autorId; }
	public void setAutorId(Long autorId) { this.autorId = autorId; }

	public LocalDateTime getExpiraEm() { return expiraEm; }
	public void setExpiraEm(LocalDateTime expiraEm) { this.expiraEm = expiraEm; }

}
