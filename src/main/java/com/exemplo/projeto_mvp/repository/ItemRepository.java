package com.exemplo.projeto_mvp.repository;

import com.exemplo.projeto_mvp.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Item> rowMapper = new RowMapper<Item>() {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            Item item = new Item();
            item.setId(rs.getLong("id"));
            item.setNome(rs.getString("nome"));
            item.setDescricao(rs.getString("descricao"));
            item.setPreco(rs.getDouble("preco"));
            item.setDataCadastro(rs.getDate("data_cadastro").toLocalDate());
            item.setAtivo(rs.getBoolean("ativo"));
            return item;
        }
    };

    public List<Item> findAll() {
        return jdbcTemplate.query("SELECT * FROM item", rowMapper);
    }

    public Optional<Item> findById(Long id) {
        String sql = "SELECT * FROM item WHERE id = ?";
        List<Item> itens = jdbcTemplate.query(sql, rowMapper, id);
        return itens.stream().findFirst();
    }

    public void insert(Item item) {
        String sql = "INSERT INTO item (nome, descricao, preco, data_cadastro, ativo) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, item.getNome(), item.getDescricao(), item.getPreco(), item.getDataCadastro(), item.getAtivo());
    }

    public void update(Item item) {
        String sql = "UPDATE item SET nome = ?, descricao = ?, preco = ?, data_cadastro = ?, ativo = ? WHERE id = ?";
        jdbcTemplate.update(sql, item.getNome(), item.getDescricao(), item.getPreco(), item.getDataCadastro(), item.getAtivo(), item.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}