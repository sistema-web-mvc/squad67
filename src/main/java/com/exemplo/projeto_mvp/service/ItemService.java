package com.exemplo.projeto_mvp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exemplo.projeto_mvp.model.Item;
import com.exemplo.projeto_mvp.repository.ItemRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository repository;

    public List<Item> listarTodos() {
        return repository.findAll();
    }

    public Optional<Item> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void salvar(Item item) {
        if (item.getId() == null) {
            repository.insert(item);
        } else {
            repository.update(item);
        }
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}