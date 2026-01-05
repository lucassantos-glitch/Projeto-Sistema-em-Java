// MedicamentoController.java
package com.farmacia.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.farmacia.model.Medicamento;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.service.MedicamentoService;
import java.util.List;
import org.springframework.lang.NonNull;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/medicamentos")
@Tag(name = "Medicamento")
public class MedicamentoController {
    @Autowired
    private MedicamentoRepository repository;

    @Autowired
    private MedicamentoService medicamentoService;

    @Operation(summary = "Listar medicamentos", description = "Retorna a lista de todos os medicamentos cadastrados.")
    @GetMapping
    public List<Medicamento> listar() {
        return repository.findAll();
    }

    @Operation(summary = "Cadastrar medicamento", description = "Cadastra um novo medicamento no sistema.")
    @PostMapping
    public Medicamento criar(@RequestBody @Valid @NonNull Medicamento medicamento) {
        return medicamentoService.criar(medicamento);
    }

    @Operation(summary = "Buscar medicamento por ID", description = "Retorna os detalhes de um medicamento específico pelo seu ID.")
    @GetMapping("/{id}")
    public Medicamento buscar(@PathVariable @NonNull Long id) {
        return repository.findById(id).orElse(null);
    }

    @Operation(summary = "Atualizar medicamento", description = "Atualiza as informações de um medicamento existente pelo ID.")
    @PutMapping("/{id}")
    public Medicamento atualizar(@PathVariable @NonNull Long id, @RequestBody Medicamento medicamento) {
        return medicamentoService.atualizar(id, medicamento);
    }

    @Operation(summary = "Deletar medicamento", description = "Remove um medicamento do sistema pelo ID.")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable @NonNull Long id) {
        medicamentoService.deletar(id);
    }

    @Operation(summary = "Alterar status do medicamento", description = "Ativa ou inativa um medicamento pelo ID.")
    @PatchMapping("/{id}/status")
    public void alterarStatus(
        @PathVariable @NonNull Long id,
        @Parameter(description = "Status do medicamento: Status Ativo (true), Status Inativo (false)")
        @RequestParam Boolean ativo) {
        medicamentoService.alterarStatus(id, ativo);
    }
}