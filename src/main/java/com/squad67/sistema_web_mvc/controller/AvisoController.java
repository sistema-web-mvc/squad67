package com.squad67.sistema_web_mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.squad67.sistema_web_mvc.exception.RegraDeNegocioException;
import com.squad67.sistema_web_mvc.model.AvisoForm;
import com.squad67.sistema_web_mvc.model.TipoAviso;
import com.squad67.sistema_web_mvc.service.AvisoService;
import com.squad67.sistema_web_mvc.validator.AvisoFormValidator;

/**
 * Camada web: recebe a requisição, chama o service, coloca o resultado no
 * Model e escolhe a página. Não tem SQL nem regra de negócio.
 *
 * Fluxo dos formulários (GET -> POST -> redirect -> GET):
 *   1. GET  /avisos/novo           mostra o formulário vazio
 *   2. POST /avisos                valida e grava; se der certo, REDIRECIONA para o detalhe
 *                                  (assim, apertar F5 não envia o formulário de novo)
 *   3. GET  /avisos/{id}           mostra o aviso e a mensagem de sucesso (flash)
 * Editar e excluir seguem o mesmo ciclo.
 */
@Controller
@RequestMapping("/avisos")
public class AvisoController {

	private final AvisoService service;
	private final AvisoFormValidator validator;

	public AvisoController(AvisoService service, AvisoFormValidator validator) {
		this.service = service;
		this.validator = validator;
	}

	// O id nunca vem do formulário: vem da URL (evita editar outro aviso por engano).
	@InitBinder("avisoForm")
	public void configurarBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
	}

	// ---------------------------------------------------------------
	// Listar e ver detalhe
	// ---------------------------------------------------------------

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

	// ---------------------------------------------------------------
	// Criar
	// ---------------------------------------------------------------

	// GET /avisos/novo
	@GetMapping("/novo")
	public String novo(Model model) {
		model.addAttribute("avisoForm", new AvisoForm());
		carregarOpcoes(model);
		return "avisos/form";
	}

	// POST /avisos
	@PostMapping
	public String criar(@ModelAttribute("avisoForm") AvisoForm form, BindingResult resultado,
			Model model, RedirectAttributes flash) {
		validator.validate(form, resultado);
		if (!resultado.hasErrors()) {
			try {
				Long id = service.criar(form);
				flash.addFlashAttribute("mensagem", "Aviso criado com sucesso.");
				return "redirect:/avisos/" + id;
			} catch (RegraDeNegocioException e) {
				resultado.rejectValue(e.getCampo(), "regra", e.getMessage());
			}
		}
		carregarOpcoes(model);
		return "avisos/form";
	}

	// ---------------------------------------------------------------
	// Editar
	// ---------------------------------------------------------------

	// GET /avisos/3/editar
	@GetMapping("/{id}/editar")
	public String editar(@PathVariable("id") Long id, Model model) {
		model.addAttribute("avisoForm", service.buscarFormPorId(id));
		carregarOpcoes(model);
		return "avisos/form";
	}

	// POST /avisos/3/editar
	@PostMapping("/{id}/editar")
	public String atualizar(@PathVariable("id") Long id, @ModelAttribute("avisoForm") AvisoForm form,
			BindingResult resultado, Model model, RedirectAttributes flash) {
		form.setId(id);
		validator.validate(form, resultado);
		if (!resultado.hasErrors()) {
			try {
				service.atualizar(id, form);
				flash.addFlashAttribute("mensagem", "Aviso atualizado com sucesso.");
				return "redirect:/avisos/" + id;
			} catch (RegraDeNegocioException e) {
				resultado.rejectValue(e.getCampo(), "regra", e.getMessage());
			}
		}
		carregarOpcoes(model);
		return "avisos/form";
	}

	// ---------------------------------------------------------------
	// Excluir (só por POST; a confirmação é feita na tela, antes de enviar)
	// ---------------------------------------------------------------

	// POST /avisos/3/excluir
	@PostMapping("/{id}/excluir")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes flash) {
		service.excluir(id);
		flash.addFlashAttribute("mensagem", "Aviso excluído com sucesso.");
		return "redirect:/avisos";
	}

	// ---------------------------------------------------------------

	/** Listas usadas pelos selects do formulário. */
	private void carregarOpcoes(Model model) {
		model.addAttribute("tipos", TipoAviso.values());
		model.addAttribute("campi", service.listarCampi());
		model.addAttribute("turmas", service.listarTurmas());
		model.addAttribute("espacos", service.listarEspacos());
		model.addAttribute("autores", service.listarAutores());
	}

}
