package com.squad67.sistema_web_mvc.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.squad67.sistema_web_mvc.model.Aviso;
import com.squad67.sistema_web_mvc.model.TipoAviso;

/**
 * Acesso ao banco para avisos. Só faz SQL: nenhuma regra de negócio aqui.
 *
 * Todo valor que vem de fora entra pelo placeholder "?" (nunca concatenado
 * no texto do SQL), o que impede SQL injection. As únicas partes concatenadas
 * são trechos fixos escritos neste arquivo.
 */
@Repository
public class AvisoRepository {

	private static final String SELECT_AVISO = """
			SELECT a.id, a.tipo, a.titulo, a.descricao, a.criado_em, a.expira_em,
			       c.nome AS campus,
			       t.nome AS turma,
			       u.nome AS autor,
			       e.nome AS espaco,
			       (SELECT COUNT(*) FROM confirmacao_aviso ca WHERE ca.aviso_id = a.id) AS confirmacoes
			  FROM aviso a
			  JOIN campus  c ON c.id = a.campus_id
			  JOIN usuario u ON u.id = a.autor_id
			  LEFT JOIN turma  t ON t.id = a.turma_id
			  LEFT JOIN espaco e ON e.id = a.espaco_id
			""";

	/** Aviso ativo e que ainda não expirou. */
	private static final String VIGENTE =
			" WHERE a.ativo = TRUE AND (a.expira_em IS NULL OR a.expira_em > CURRENT_TIMESTAMP)";

	private static final String MAIS_RECENTES_PRIMEIRO = " ORDER BY a.criado_em DESC, a.id DESC";

	private static final RowMapper<Aviso> MAPEADOR = (rs, linha) -> new Aviso(
			rs.getLong("id"),
			TipoAviso.valueOf(rs.getString("tipo")),
			rs.getString("titulo"),
			rs.getString("descricao"),
			rs.getObject("criado_em", LocalDateTime.class),
			rs.getObject("expira_em", LocalDateTime.class),
			rs.getString("campus"),
			rs.getString("turma"),
			rs.getString("autor"),
			rs.getString("espaco"),
			rs.getLong("confirmacoes"));

	private final JdbcTemplate jdbc;

	public AvisoRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	/**
	 * Avisos vigentes, do mais novo para o mais antigo.
	 *
	 * @param tipo filtra por tipo; null traz todos os tipos
	 */
	public List<Aviso> listarVigentes(TipoAviso tipo) {
		if (tipo == null) {
			return jdbc.query(SELECT_AVISO + VIGENTE + MAIS_RECENTES_PRIMEIRO, MAPEADOR);
		}
		return jdbc.query(
				SELECT_AVISO + VIGENTE + " AND a.tipo = ?" + MAIS_RECENTES_PRIMEIRO,
				MAPEADOR,
				tipo.name());
	}

	/**
	 * Um aviso pelo id (mesmo que já tenha expirado). Avisos desativados
	 * (ativo = FALSE) não aparecem.
	 */
	public Optional<Aviso> buscarPorId(Long id) {
		List<Aviso> encontrados = jdbc.query(
				SELECT_AVISO + " WHERE a.id = ? AND a.ativo = TRUE",
				MAPEADOR,
				id);
		return encontrados.stream().findFirst();
	}

}
