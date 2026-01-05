package com.farmacia.service;

import com.farmacia.model.Medicamento;
import com.farmacia.model.MovimentacaoEstoque;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.repository.MovimentacaoEstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MovimentacaoEstoqueService {

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Transactional
    public MovimentacaoEstoque movimentar(Long medicamentoId, String tipo, Integer quantidade) {
        if (medicamentoId == null) {
            throw new RuntimeException("ID do medicamento não pode ser nulo");
        }
        Medicamento medicamento = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado"));

        if (quantidade == null || quantidade <= 0) {
            throw new RuntimeException("Quantidade inválida");
        }

        if ("SAIDA".equalsIgnoreCase(tipo)) {
            if (medicamento.getQuantidade() < quantidade) {
                throw new RuntimeException("Estoque insuficiente");
            }
            medicamento.setQuantidade(medicamento.getQuantidade() - quantidade);
        } else if ("ENTRADA".equalsIgnoreCase(tipo)) {
            medicamento.setQuantidade(medicamento.getQuantidade() + quantidade);
        } else {
            throw new RuntimeException("Tipo de movimentação inválido");
        }

        medicamentoRepository.save(medicamento);

        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setMedicamento(medicamento);
        mov.setTipo(tipo.toUpperCase());
        mov.setQuantidade(quantidade);
        mov.setData(LocalDateTime.now());
        return movimentacaoRepository.save(mov);
    }
}
