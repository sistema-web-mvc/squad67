package com.squad67.sistema_web_mvc.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.squad67.sistema_web_mvc.model.Opcao;

/**
 * Consultas de apoio ao formulário de avisos: as listas dos selects (campus,
 * turma, espaço, autor) e as checagens de "esse id existe / pertence a esse campus".
 * Só SQL, com "?" para todo valor externo.
 */
@Repository
public class ReferenciaRepository {

	private static final RowMapper<Opcao> MAPEADOR = (rs, linha) -> new Opcao(rs.getLong("id"), rs.getString("rotulo"));

	private final JdbcTemplate jdbc;

	public ReferenciaRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public List<Opcao> listarCampi() {
		return jdbc.query("SELECT id, nome AS rotulo FROM campus WHERE ativo = TRUE ORDER BY nome", MAPEADOR);
	}

	public List<Opcao> listarTurmas() {
		return jdbc.query("""
				SELECT t.id, t.nome || ' (' || cu.nome || ', ' || ca.nome || ')' AS rotulo
				  FROM turma t
				  JOIN curso  cu ON cu.id = t.curso_id
				  JOIN campus ca ON ca.id = cu.campus_id
				 ORDER BY ca.nome, t.nome
				""", MAPEADOR);
	}

	public List<Opcao> listarEspacos() {
		return jdbc.query("""
				SELECT e.id, e.nome || ' (' || c.nome || ')' AS rotulo
				  FROM espaco e
				  JOIN campus c ON c.id = e.campus_id
				 ORDER BY c.nome, e.nome
				""", MAPEADOR);
	}

	/** Quem pode publicar aviso: todos os usuários ativos, menos alunos. */
	public List<Opcao> listarAutores() {
		return jdbc.query("""
				SELECT id, nome AS rotulo
				  FROM usuario
				 WHERE ativo = TRUE AND perfil <> 'ALUNO'
				 ORDER BY nome
				""", MAPEADOR);
	}

	public boolean existeCampus(Long campusId) {
		return existe("SELECT COUNT(*) FROM campus WHERE id = ? AND ativo = TRUE", campusId);
	}

	public boolean existeAutor(Long usuarioId) {
		return existe("SELECT COUNT(*) FROM usuario WHERE id = ? AND ativo = TRUE AND perfil <> 'ALUNO'", usuarioId);
	}

	public boolean turmaPertenceAoCampus(Long turmaId, Long campusId) {
		return existe("""
				SELECT COUNT(*)
				  FROM turma t
				  JOIN curso cu ON cu.id = t.curso_id
				 WHERE t.id = ? AND cu.campus_id = ?
				""", turmaId, campusId);
	}

	public boolean espacoPertenceAoCampus(Long espacoId, Long campusId) {
		return existe("SELECT COUNT(*) FROM espaco WHERE id = ? AND campus_id = ?", espacoId, campusId);
	}

	private boolean existe(String sql, Object... parametros) {
		Integer total = jdbc.queryForObject(sql, Integer.class, parametros);
		return total != null && total > 0;
	}

}
