package com.farmacia.model;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ItemVenda {
    private BigDecimal precoUnitario;

    @ManyToOne
    private Medicamento medicamento;
    
    private Integer quantidade;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Integer getQuantidade() {
        return quantidade;  
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Medicamento getMedicamento() {
    return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
    this.medicamento = medicamento;
    } 

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}