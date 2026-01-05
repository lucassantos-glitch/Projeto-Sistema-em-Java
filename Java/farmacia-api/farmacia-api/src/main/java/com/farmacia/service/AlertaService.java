package com.farmacia.service;

import com.farmacia.model.Medicamento;
import com.farmacia.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AlertaService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Value("${farmacia.estoque.minimo:10}")
    private Integer estoqueMinimo;

    public List<Medicamento> buscarEstoqueBaixo() {
        return medicamentoRepository.findByQuantidadeLessThanAndAtivoTrue(estoqueMinimo);
    }

    public List<Medicamento> buscarValidadeProxima() {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(30);
        return medicamentoRepository.findByValidadeBetweenAndAtivoTrue(hoje, limite);
    }
}