package com.farmacia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.farmacia.config.LocalDateFlexDeserializer;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medicamentos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "nome")
})
@Getter
@Setter
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório e único")
    private String nome;

    @NotNull(message = "Preço deve ser maior que zero")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    @Min(0)
    private Integer quantidade;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateFlexDeserializer.class)
    private LocalDate validade;

    private Boolean ativo = true;

    @ManyToOne
    private Categoria categoria;
}
