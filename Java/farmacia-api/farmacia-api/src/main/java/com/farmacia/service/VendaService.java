package com.farmacia.service;

import com.farmacia.model.Venda;
import com.farmacia.model.ItemVenda;
import com.farmacia.model.Medicamento;
import com.farmacia.model.MovimentacaoEstoque;
import com.farmacia.repository.VendaRepository;

// Adicione o import para RegraNegocioException se ela existir em outro pacote
import com.farmacia.exception.RegraNegocioException;

import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.repository.MovimentacaoEstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Service
public class VendaService {
    public List<Venda> listar() {
        return vendaRepository.findAll();
    }

    public Venda salvar(Venda venda) {
        if (venda == null) {
            throw new IllegalArgumentException("A venda não pode ser nula");
        }
        return vendaRepository.save(venda);
    }

    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo");
        }
        vendaRepository.deleteById(id);
    }

    @Transactional
    public Venda registrar(Venda venda) {
        if (venda.getItens().isEmpty()) {
            throw new RegraNegocioException("Venda sem itens");
        }

        // Validação de maioridade do cliente
        if (venda.getCliente() == null || venda.getCliente().getDataNascimento() == null ||
            java.time.Period.between(venda.getCliente().getDataNascimento(), java.time.LocalDate.now()).getYears() < 18) {
            throw new RegraNegocioException("Cliente deve ter mais de 18 anos para realizar compras");
        }

        BigDecimal total = BigDecimal.ZERO;
        venda.setData(LocalDateTime.now());

        for (ItemVenda item : venda.getItens()) {
            Medicamento med = item.getMedicamento();

            if (!med.getAtivo())
                throw new RegraNegocioException("Medicamento inativo não pode ser vendido");

            if (med.getValidade().isBefore(LocalDate.now()))
                throw new RegraNegocioException("Medicamento vencido");

            if (med.getQuantidade() < item.getQuantidade())
                throw new RegraNegocioException("Estoque insuficiente");

            // Sempre usar o preço atual do medicamento
            item.setPrecoUnitario(med.getPreco());
            total = total.add(med.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));

            // Atualiza estoque do medicamento
            med.setQuantidade(med.getQuantidade() - item.getQuantidade());
            medicamentoRepository.save(med);

            // Registra movimentação de estoque (SAÍDA por venda)
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setMedicamento(med);
            movimentacao.setTipo("SAIDA");
            movimentacao.setQuantidade(item.getQuantidade());
            movimentacao.setData(LocalDateTime.now());
            movimentacao.setEstoqueAtual(med.getQuantidade());
            movimentacaoEstoqueRepository.save(movimentacao);
        }

        venda.setTotal(total);
        return vendaRepository.save(venda);
    }

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
}