package com.farmacia.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.repository.ItemVendaRepository;
import com.farmacia.model.Medicamento;
import com.farmacia.exception.RegraNegocioException;
import java.time.LocalDate;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository repository;

    @Autowired
    private ItemVendaRepository itemVendaRepository;


    public Medicamento criar(Medicamento med) {
        if (med.getId() != null) {
            throw new RegraNegocioException("Não envie o campo 'id' ao cadastrar um novo medicamento.");
        }
        if (med.getNome() == null || med.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome é obrigatório");
        }
        if (repository.existsByNome(med.getNome())) {
            throw new RegraNegocioException("Medicamento já cadastrado");
        }
        if (med.getPreco() == null || med.getPreco().doubleValue() <= 0) {
            throw new RegraNegocioException("Preço deve ser maior que zero");
        }
        if (med.getQuantidade() == null || med.getQuantidade() <= 0) {
            throw new RegraNegocioException("Quantidade em estoque deve ser maior que zero");
        }
        if (med.getValidade() == null || !med.getValidade().isAfter(LocalDate.now())) {
            throw new RegraNegocioException("Data de validade deve ser futura");
        }
        med.setAtivo(true);
        return repository.save(med);
    }

    public Medicamento atualizar(@org.springframework.lang.NonNull Long id, Medicamento med) {
        Medicamento existente = repository.findById(id)
            .orElseThrow(() -> new RegraNegocioException("Medicamento não encontrado"));
        // Atualize apenas os campos permitidos
        existente.setNome(med.getNome());
        existente.setPreco(med.getPreco());
        if (med.getQuantidade() == null || med.getQuantidade() <= 0) {
            throw new RegraNegocioException("Quantidade em estoque deve ser maior que zero");
        }
        existente.setQuantidade(med.getQuantidade());
        existente.setValidade(med.getValidade());
        existente.setAtivo(med.getAtivo());
        existente.setCategoria(med.getCategoria());
        return repository.save(existente);
    }

    public void alterarStatus(@org.springframework.lang.NonNull Long id, Boolean ativo) {
        Medicamento med = repository.findById(id)
            .orElseThrow(() -> new RegraNegocioException("Medicamento não encontrado"));
        // Validação: só permite alteração se validade for futura
        if (med.getValidade() == null || !med.getValidade().isAfter(java.time.LocalDate.now())) {
            throw new RegraNegocioException("Não é possível alterar o status: a validade do medicamento deve ser futura.");
        }
        med.setAtivo(ativo);
        repository.save(med);
    }

    @SuppressWarnings("null")
    public void deletar(@org.springframework.lang.NonNull Long id) {
        Medicamento med = repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Medicamento não encontrado"));
        boolean vendido = itemVendaRepository.existsByMedicamentoId(id);
        if (vendido) {
            // Soft delete: inativa em vez de excluir fisicamente
            med.setAtivo(false);
            repository.save(med);
            throw new RegraNegocioException("Medicamento já foi vendido. Exclusão física não permitida. Medicamento inativado.");
        } else {
            // Exclusão física se nunca foi vendido
            repository.delete(med);
        }
    }
}
