package com.squad67.sistema_web_mvc.validator;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.squad67.sistema_web_mvc.model.AvisoForm;

/**
 * Valida o que dá para checar só olhando o formulário (campo vazio, tamanho,
 * data). Regras que precisam do banco ficam no AvisoService.
 */
@Component
public class AvisoFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> classe) {
		return AvisoForm.class.equals(classe);
	}

	@Override
	public void validate(Object alvo, Errors erros) {
		AvisoForm form = (AvisoForm) alvo;

		ValidationUtils.rejectIfEmpty(erros, "tipo", "obrigatorio", "Escolha o tipo do aviso.");

		ValidationUtils.rejectIfEmptyOrWhitespace(erros, "titulo", "obrigatorio", "Informe o título.");
		if (form.getTitulo() != null && form.getTitulo().trim().length() > 150) {
			erros.rejectValue("titulo", "tamanho", "O título pode ter no máximo 150 caracteres.");
		}

		if (form.getDescricao() != null && form.getDescricao().trim().length() > 1000) {
			erros.rejectValue("descricao", "tamanho", "A descrição pode ter no máximo 1000 caracteres.");
		}

		ValidationUtils.rejectIfEmpty(erros, "campusId", "obrigatorio", "Escolha o campus.");
		ValidationUtils.rejectIfEmpty(erros, "autorId", "obrigatorio", "Escolha quem está publicando.");

		// Só olha a data se ela veio sem erro de formato (esse erro o Spring já registrou).
		if (!erros.hasFieldErrors("expiraEm") && form.getExpiraEm() != null
				&& !form.getExpiraEm().isAfter(LocalDateTime.now())) {
			erros.rejectValue("expiraEm", "passado", "A validade precisa ser uma data futura.");
		}
	}

}
