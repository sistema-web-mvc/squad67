package com.squad67.sistema_web_mvc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.squad67.sistema_web_mvc.exception.RecursoNaoEncontradoException;
import com.squad67.sistema_web_mvc.model.Aviso;
import com.squad67.sistema_web_mvc.model.TipoAviso;
import com.squad67.sistema_web_mvc.repository.AvisoRepository;

/**
 * Regras da aplicação para avisos. Não sabe nada de HTTP nem de SQL:
 * usa o repository para ler o banco e entrega objetos prontos ao controller.
 */
@Service
public class AvisoService {

	private final AvisoRepository repository;

	public AvisoService(AvisoRepository repository) {
		this.repository = repository;
	}

	/** Avisos que ainda estão valendo; tipo null traz todos. */
	public List<Aviso> listarVigentes(TipoAviso tipo) {
		return repository.listarVigentes(tipo);
	}

	/** Detalhe de um aviso; lança 404 se não existir. */
	public Aviso buscarPorId(Long id) {
		return repository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Aviso " + id + " não encontrado"));
	}

}
