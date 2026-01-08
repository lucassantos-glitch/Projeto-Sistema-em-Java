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

    @SuppressWarnings("null")
    public Medicamento atualizar(@org.springframework.lang.NonNull Long id, Medicamento med) {
        Medicamento existente = repository.findById(id)
            .orElseThrow(() -> new RegraNegocioException("Medicamento não encontrado"));

        // Nome: obrigatório e único (ignora se não enviado)
        if (med.getNome() != null && !med.getNome().trim().isEmpty()) {
            if (!med.getNome().equalsIgnoreCase(existente.getNome()) && repository.existsByNome(med.getNome())) {
                throw new RegraNegocioException("Nome é obrigatório e único");
            }
            existente.setNome(med.getNome().trim());
        }

        // Preço: > 0 (ignora se não enviado)
        if (med.getPreco() != null) {
            if (med.getPreco().doubleValue() <= 0) {
                throw new RegraNegocioException("Preço deve ser maior que zero");
            }
            existente.setPreco(med.getPreco());
        }

        // Quantidade: >= 0 (ignora se não enviado)
        if (med.getQuantidade() != null) {
            if (med.getQuantidade() < 0) {
                throw new RegraNegocioException("Quantidade em estoque deve ser maior ou igual a zero");
            }
            existente.setQuantidade(med.getQuantidade());
        }

        // Validade: deve ser futura (ignora se não enviado)
        if (med.getValidade() != null) {
            if (!med.getValidade().isAfter(LocalDate.now())) {
                throw new RegraNegocioException("Data de validade deve ser futura");
            }
            existente.setValidade(med.getValidade());
        }

        // Status ativo (ignora se não enviado)
        if (med.getAtivo() != null) {
            existente.setAtivo(med.getAtivo());
        }

        // Categoria (ignora se não enviada)
        if (med.getCategoria() != null && med.getCategoria().getId() != null) {
            existente.setCategoria(med.getCategoria());
        }

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
            // Não lança exceção - o soft delete é uma operação válida
        } else {
            // Exclusão física se nunca foi vendido
            repository.delete(med);
        }
    }
}
