package com.farmacia.controller;

import com.farmacia.model.Medicamento;
import com.farmacia.model.MovimentacaoEstoque;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.repository.MovimentacaoEstoqueRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/estoque")
@Tag(name = "Estoque")
public class EstoqueController {
    @Autowired
    private MedicamentoRepository medicamentoRepository;
    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    // Entrada de estoque
    @Operation(summary = "Registrar entrada de estoque", description = "Registra a entrada de medicamentos no estoque.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                name = "EntradaEstoqueMinima",
                summary = "Exemplo mínimo para entrada de estoque",
                value = "{\n  \"quantidade\": 10,\n  \"medicamento\": { \"id\": 1 }\n}"
            )
        )
    )
    @PostMapping("/entrada")
    public ResponseEntity<?> entradaEstoque(@Valid @RequestBody MovimentacaoEstoque movimentacao) {
        try {
            if (movimentacao.getQuantidade() == null || movimentacao.getQuantidade() <= 0) {
                return ResponseEntity.badRequest().body("Quantidade deve ser maior que zero.");
            }
            if (movimentacao.getMedicamento() == null || movimentacao.getMedicamento().getId() == null) {
                return ResponseEntity.badRequest().body("ID do medicamento não pode ser nulo.");
            }
            Long medicamentoId = java.util.Objects.requireNonNull(movimentacao.getMedicamento().getId(), "ID do medicamento não pode ser nulo.");
            Optional<Medicamento> optMed = medicamentoRepository.findById(medicamentoId);
            if (optMed.isEmpty()) {
                return ResponseEntity.badRequest().body("Medicamento não encontrado.");
            }
            Medicamento medicamento = optMed.get();
            // Regra: Entrada aumenta o estoque
            medicamento.setQuantidade(medicamento.getQuantidade() + movimentacao.getQuantidade());
            medicamentoRepository.save(medicamento);
            // Registrar movimentação com data, tipo, quantidade e estoque atual
            movimentacao.setTipo("ENTRADA");
            movimentacao.setData(java.time.LocalDateTime.now());
            movimentacao.setEstoqueAtual(medicamento.getQuantidade());
            movimentacaoEstoqueRepository.save(movimentacao);
            return ResponseEntity.ok(medicamento);
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity.status(500).body("Erro ao registrar entrada de estoque: " + e.getMessage() + "\n" + sw.toString());
        }
    }

    // Saída de estoque
    @Operation(summary = "Registrar saída de estoque", description = "Registra a saída de medicamentos do estoque.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                name = "SaidaEstoqueMinima",
                summary = "Exemplo mínimo para saída de estoque",
                value = "{\n  \"quantidade\": 10,\n  \"medicamento\": { \"id\": 1 }\n}"
            )
        )
    )
    @PostMapping("/saida")
    public ResponseEntity<?> saidaEstoque(@Valid @RequestBody MovimentacaoEstoque movimentacao) {
        try {
            if (movimentacao.getQuantidade() == null || movimentacao.getQuantidade() <= 0) {
                return ResponseEntity.badRequest().body("Quantidade deve ser maior que zero.");
            }
            if (movimentacao.getMedicamento() == null || movimentacao.getMedicamento().getId() == null) {
                return ResponseEntity.badRequest().body("ID do medicamento não pode ser nulo.");
            }
            Long medicamentoId = java.util.Objects.requireNonNull(movimentacao.getMedicamento().getId(), "ID do medicamento não pode ser nulo.");
            Optional<Medicamento> optMed = medicamentoRepository.findById(medicamentoId);
            if (optMed.isEmpty()) {
                return ResponseEntity.badRequest().body("Medicamento não encontrado.");
            }
            Medicamento medicamento = optMed.get();
            // Regra: Não permitir saída maior que estoque disponível
            if (medicamento.getQuantidade() < movimentacao.getQuantidade()) {
                return ResponseEntity.badRequest().body("Estoque insuficiente.");
            }
            // Regra: Saída diminui o estoque
            medicamento.setQuantidade(medicamento.getQuantidade() - movimentacao.getQuantidade());
            medicamentoRepository.save(medicamento);
            // Registrar movimentação com data, tipo, quantidade e estoque atual
            movimentacao.setTipo("SAIDA");
            movimentacao.setData(java.time.LocalDateTime.now());
            movimentacao.setEstoqueAtual(medicamento.getQuantidade());
            movimentacaoEstoqueRepository.save(movimentacao);
            return ResponseEntity.ok(medicamento);
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity.status(500).body("Erro ao registrar saída de estoque: " + e.getMessage() + "\n" + sw.toString());
        }
    }

    // Consultar estoque de um medicamento
    @Operation(summary = "Consultar estoque de medicamento", description = "Consulta a quantidade disponível em estoque de um medicamento pelo ID.")
    @GetMapping("/{medicamentoId}")
    public ResponseEntity<?> consultarEstoque(@PathVariable Long medicamentoId) {
        if (medicamentoId == null) {
            return ResponseEntity.badRequest().body("O ID do medicamento não pode ser nulo.");
        }
        Optional<Medicamento> optMed = medicamentoRepository.findById(medicamentoId);
        if (optMed.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Medicamento medicamento = optMed.get();
        return ResponseEntity.ok(medicamento.getQuantidade());
    }

    @Operation(summary = "Histórico de movimentações", description = "Lista movimentações de estoque por medicamento (mais recentes primeiro).")
    @GetMapping("/historico/{medicamentoId}")
    public ResponseEntity<?> historico(@PathVariable Long medicamentoId) {
        if (medicamentoId == null) {
            return ResponseEntity.badRequest().body("O ID do medicamento não pode ser nulo.");
        }
        Optional<Medicamento> optMed = medicamentoRepository.findById(medicamentoId);
        if (optMed.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Medicamento medicamento = optMed.get();
        var lista = movimentacaoEstoqueRepository.findByMedicamentoIdOrderByDataDesc(medicamentoId);
        
        // Backfill: corrige registros antigos sem data ou estoqueAtual
        boolean precisaSalvar = false;
        for (var mov : lista) {
            if (mov.getData() == null) {
                mov.setData(java.time.LocalDateTime.now());
                precisaSalvar = true;
            }
            if (mov.getEstoqueAtual() == null) {
                // Define estoque atual como o estoque atual do medicamento para registros antigos
                mov.setEstoqueAtual(medicamento.getQuantidade());
                precisaSalvar = true;
            }
        }
        if (precisaSalvar && lista != null) {
            movimentacaoEstoqueRepository.saveAll(lista);
            lista = movimentacaoEstoqueRepository.findByMedicamentoIdOrderByDataDesc(medicamentoId);
        }
        return ResponseEntity.ok(lista);
    }
}