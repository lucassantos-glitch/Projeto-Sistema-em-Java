package com.farmacia.controller;

import com.farmacia.model.Categoria;
import com.farmacia.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @Operation(summary = "Cadastrar categoria", description = "Cadastra uma nova categoria de medicamento.")
    @PostMapping
    public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(categoria));
    }

    @Operation(summary = "Listar categorias", description = "Retorna todas as categorias cadastradas.")
    @GetMapping
    public List<Categoria> listar() {
        return service.listar();
    }

    @Operation(summary = "Buscar categoria por ID", description = "Retorna os detalhes de uma categoria específica pelo seu ID.")
    @GetMapping("/{id}")
    public Categoria buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Atualizar categoria", description = "Atualiza as informações de uma categoria existente pelo ID.")
    @PutMapping("/{id}")
    public Categoria atualizar(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        return service.atualizar(id, categoria);
    }

    @Operation(summary = "Deletar categoria", description = "Remove uma categoria do sistema pelo ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
