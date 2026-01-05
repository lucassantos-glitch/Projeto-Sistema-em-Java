package com.farmacia.repository;

import com.farmacia.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    boolean existsByMedicamentoId(Long medicamentoId);
}
