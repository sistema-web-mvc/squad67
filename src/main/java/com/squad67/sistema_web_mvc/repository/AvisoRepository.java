package com.squad67.sistema_web_mvc.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.squad67.sistema_web_mvc.model.Aviso;
import com.squad67.sistema_web_mvc.model.AvisoForm;
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

	// ---------------------------------------------------------------
	// Criar, editar e excluir
	// ---------------------------------------------------------------

	private static final String INSERIR = """
			INSERT INTO aviso (campus_id, turma_id, autor_id, espaco_id, tipo, titulo, descricao, expira_em)
			VALUES (?, ?, ?, ?, ?, ?, ?, ?)
			""";

	private static final String ATUALIZAR = """
			UPDATE aviso
			   SET campus_id = ?, turma_id = ?, autor_id = ?, espaco_id = ?,
			       tipo = ?, titulo = ?, descricao = ?, expira_em = ?
			 WHERE id = ? AND ativo = TRUE
			""";

	private static final RowMapper<AvisoForm> MAPEADOR_FORM = (rs, linha) -> {
		AvisoForm form = new AvisoForm();
		form.setId(rs.getLong("id"));
		form.setCampusId(rs.getLong("campus_id"));
		form.setTurmaId(rs.getObject("turma_id", Long.class));
		form.setAutorId(rs.getLong("autor_id"));
		form.setEspacoId(rs.getObject("espaco_id", Long.class));
		form.setTipo(TipoAviso.valueOf(rs.getString("tipo")));
		form.setTitulo(rs.getString("titulo"));
		form.setDescricao(rs.getString("descricao"));
		form.setExpiraEm(rs.getObject("expira_em", LocalDateTime.class));
		return form;
	};

	/** Os dados de um aviso do jeito que o formulário de edição precisa (ids dos selects). */
	public Optional<AvisoForm> buscarFormPorId(Long id) {
		List<AvisoForm> encontrados = jdbc.query("""
				SELECT id, campus_id, turma_id, autor_id, espaco_id, tipo, titulo, descricao, expira_em
				  FROM aviso
				 WHERE id = ? AND ativo = TRUE
				""", MAPEADOR_FORM, id);
		return encontrados.stream().findFirst();
	}

	/** Insere o aviso e devolve o id gerado pelo banco. */
	public Long inserir(AvisoForm form) {
		KeyHolder chave = new GeneratedKeyHolder();
		jdbc.update(conexao -> {
			PreparedStatement ps = conexao.prepareStatement(INSERIR, new String[] { "id" });
			preencherCampos(ps, form);
			return ps;
		}, chave);
		return chave.getKey().longValue();
	}

	/** Atualiza o aviso. Devolve quantas linhas mudaram (0 = aviso não existe). */
	public int atualizar(Long id, AvisoForm form) {
		return jdbc.update(conexao -> {
			PreparedStatement ps = conexao.prepareStatement(ATUALIZAR);
			preencherCampos(ps, form);
			ps.setLong(9, id);
			return ps;
		});
	}

	/**
	 * Apaga o aviso. As confirmações e notificações dele são apagadas junto
	 * (ON DELETE CASCADE no schema.sql). Devolve quantas linhas foram apagadas.
	 */
	public int excluir(Long id) {
		return jdbc.update("DELETE FROM aviso WHERE id = ?", id);
	}

	/** Preenche os 8 primeiros "?" (os campos do formulário), na ordem do INSERT/UPDATE. */
	private static void preencherCampos(PreparedStatement ps, AvisoForm form) throws SQLException {
		ps.setLong(1, form.getCampusId());
		ps.setObject(2, form.getTurmaId(), Types.BIGINT);
		ps.setLong(3, form.getAutorId());
		ps.setObject(4, form.getEspacoId(), Types.BIGINT);
		ps.setString(5, form.getTipo().name());
		ps.setString(6, form.getTitulo());
		ps.setString(7, form.getDescricao());
		ps.setTimestamp(8, form.getExpiraEm() == null ? null : Timestamp.valueOf(form.getExpiraEm()));
	}

}
