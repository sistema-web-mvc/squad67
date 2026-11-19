package com.squad67.sistema_web_mvc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.squad67.sistema_web_mvc.exception.RecursoNaoEncontradoException;
import com.squad67.sistema_web_mvc.exception.RegraDeNegocioException;
import com.squad67.sistema_web_mvc.model.Aviso;
import com.squad67.sistema_web_mvc.model.AvisoForm;
import com.squad67.sistema_web_mvc.model.Opcao;
import com.squad67.sistema_web_mvc.model.TipoAviso;
import com.squad67.sistema_web_mvc.repository.AvisoRepository;
import com.squad67.sistema_web_mvc.repository.ReferenciaRepository;

/**
 * Regras da aplicação para avisos. Não sabe nada de HTTP nem de SQL:
 * usa os repositories para ler e gravar e entrega objetos prontos ao controller.
 */
@Service
public class AvisoService {

	private final AvisoRepository repository;
	private final ReferenciaRepository referencias;

	public AvisoService(AvisoRepository repository, ReferenciaRepository referencias) {
		this.repository = repository;
		this.referencias = referencias;
	}

	// ---------------------------------------------------------------
	// Consultas
	// ---------------------------------------------------------------

	/** Avisos que ainda estão valendo; tipo null traz todos. */
	public List<Aviso> listarVigentes(TipoAviso tipo) {
		return repository.listarVigentes(tipo);
	}

	/** Detalhe de um aviso; lança 404 se não existir. */
	public Aviso buscarPorId(Long id) {
		return repository.buscarPorId(id).orElseThrow(() -> naoEncontrado(id));
	}

	/** Dados para preencher o formulário de edição; lança 404 se não existir. */
	public AvisoForm buscarFormPorId(Long id) {
		return repository.buscarFormPorId(id).orElseThrow(() -> naoEncontrado(id));
	}

	// ---------------------------------------------------------------
	// Criar, editar e excluir
	// ---------------------------------------------------------------

	/** Cria o aviso e devolve o id gerado. Lança RegraDeNegocioException se os dados forem incoerentes. */
	public Long criar(AvisoForm form) {
		prepararEValidar(form);
		return repository.inserir(form);
	}

	/** Atualiza o aviso. Lança 404 se ele não existir. */
	public void atualizar(Long id, AvisoForm form) {
		prepararEValidar(form);
		if (repository.atualizar(id, form) == 0) {
			throw naoEncontrado(id);
		}
	}

	/** Exclui o aviso (e suas confirmações e notificações). Lança 404 se ele não existir. */
	public void excluir(Long id) {
		if (repository.excluir(id) == 0) {
			throw naoEncontrado(id);
		}
	}

	// ---------------------------------------------------------------
	// Listas para os selects do formulário
	// ---------------------------------------------------------------

	public List<Opcao> listarCampi() {
		return referencias.listarCampi();
	}

	public List<Opcao> listarTurmas() {
		return referencias.listarTurmas();
	}

	public List<Opcao> listarEspacos() {
		return referencias.listarEspacos();
	}

	public List<Opcao> listarAutores() {
		return referencias.listarAutores();
	}

	// ---------------------------------------------------------------
	// Detalhes internos
	// ---------------------------------------------------------------

	/** Limpa os textos e confere as regras que dependem do banco. */
	private void prepararEValidar(AvisoForm form) {
		form.setTitulo(form.getTitulo().trim());
		String descricao = form.getDescricao();
		form.setDescricao(descricao == null || descricao.isBlank() ? null : descricao.trim());

		if (!referencias.existeCampus(form.getCampusId())) {
			throw new RegraDeNegocioException("campusId", "Esse campus não existe.");
		}
		if (!referencias.existeAutor(form.getAutorId())) {
			throw new RegraDeNegocioException("autorId", "Essa pessoa não pode publicar avisos.");
		}
		if (form.getTurmaId() != null && !referencias.turmaPertenceAoCampus(form.getTurmaId(), form.getCampusId())) {
			throw new RegraDeNegocioException("turmaId", "Essa turma não pertence ao campus escolhido.");
		}
		if (form.getEspacoId() != null && !referencias.espacoPertenceAoCampus(form.getEspacoId(), form.getCampusId())) {
			throw new RegraDeNegocioException("espacoId", "Esse local não pertence ao campus escolhido.");
		}
	}

	private RecursoNaoEncontradoException naoEncontrado(Long id) {
		return new RecursoNaoEncontradoException("Aviso " + id + " não encontrado");
	}

}
