package com.farmacia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    @Column(nullable = false)
    private LocalDateTime data;

    private String tipo; // ENTRADA ou SAIDA

    private Integer quantidade;

    private Integer estoqueAtual; // Estoque do medicamento após esta movimentação

    @ManyToOne
    private Medicamento medicamento;

    @PrePersist
    private void preencherDataSeVazia() {
        if (this.data == null) {
            this.data = LocalDateTime.now();
        }
    }
}
