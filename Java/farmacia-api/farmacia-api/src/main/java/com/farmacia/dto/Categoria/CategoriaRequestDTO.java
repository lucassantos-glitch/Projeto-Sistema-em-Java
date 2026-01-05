package com.farmacia.dto.Categoria;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoriaRequestDTO {

    @NotBlank
    private String nome;
}
