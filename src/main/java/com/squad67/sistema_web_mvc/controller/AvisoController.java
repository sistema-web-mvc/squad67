package com.squad67.sistema_web_mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.squad67.sistema_web_mvc.model.TipoAviso;
import com.squad67.sistema_web_mvc.service.AvisoService;

/**
 * Camada web: recebe a requisição, chama o service, coloca o resultado no
 * Model e escolhe a página. Não tem SQL nem regra de negócio.
 */
@Controller
@RequestMapping("/avisos")
public class AvisoController {

	private final AvisoService service;

	public AvisoController(AvisoService service) {
		this.service = service;
	}

	// GET /avisos  ou  GET /avisos?tipo=SALA_TROCADA
	// Um valor de tipo que não existe faz o Spring responder 400 sozinho.
	@GetMapping
	public String listar(@RequestParam(name = "tipo", required = false) TipoAviso tipo, Model model) {
		model.addAttribute("avisos", service.listarVigentes(tipo));
		model.addAttribute("tipos", TipoAviso.values());
		model.addAttribute("tipoSelecionado", tipo);
		return "avisos/lista";
	}

	// GET /avisos/3  (404 se não existir)
	@GetMapping("/{id}")
	public String detalhe(@PathVariable("id") Long id, Model model) {
		model.addAttribute("aviso", service.buscarPorId(id));
		return "avisos/detalhe";
	}

}
