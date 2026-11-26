package com.squad67.sistema_web_mvc.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

	@Positive(message = "O identificador do aviso deve ser positivo.")
	private Long id; // null = aviso novo
	@NotNull(message = "Escolha o tipo do aviso.")
	private TipoAviso tipo;
	@NotBlank(message = "Informe o título.")
	@Size(max = 150, message = "O título pode ter no máximo 150 caracteres.")
	private String titulo;
	@Size(max = 1000, message = "A descrição pode ter no máximo 1000 caracteres.")
	private String descricao;
	@NotNull(message = "Escolha o campus.")
	@Positive(message = "Escolha um campus válido.")
	private Long campusId;
	@Positive(message = "Escolha uma turma válida.")
	private Long turmaId;
	@Positive(message = "Escolha um local válido.")
	private Long espacoId;
	@NotNull(message = "Escolha quem está publicando.")
	@Positive(message = "Escolha uma pessoa válida.")
	private Long autorId;

	// <input type="datetime-local"> envia no formato 2026-11-25T18:30
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Future(message = "A validade precisa ser uma data futura.")
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
