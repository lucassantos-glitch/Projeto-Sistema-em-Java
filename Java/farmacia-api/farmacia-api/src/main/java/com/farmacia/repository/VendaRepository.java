// Define your repository interface or class here, for example:
package com.farmacia.repository;

import com.farmacia.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
     // Repository methods here
}

