package com.exemplo.projeto_mvp.controller;

import com.exemplo.projeto_mvp.model.Item;
import com.exemplo.projeto_mvp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/itens")
public class ItemController {

    @Autowired
    private ItemService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("itens", service.listarTodos());
        return "itens/lista";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        Optional<Item> item = service.buscarPorId(id);
        if (item.isPresent()) {
            model.addAttribute("item", item.get());
            return "itens/detalhe";
        }
        return "redirect:/itens";
    }
}