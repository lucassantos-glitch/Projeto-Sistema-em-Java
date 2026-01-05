package com.farmacia.repository;

import com.farmacia.model.Medicamento;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
        boolean existsByCategoriaId(Long categoriaId);
    List<Medicamento> findByQuantidadeLessThanAndAtivoTrue(Integer quantidade);
    List<Medicamento> findByValidadeBetweenAndAtivoTrue(LocalDate inicio, LocalDate fim);
    BigDecimal findPrecoById(Long id);
    LocalDate findValidadeById(Long id);
    Integer findEstoqueById(Long id);
    boolean existsByNome(String nome);
}