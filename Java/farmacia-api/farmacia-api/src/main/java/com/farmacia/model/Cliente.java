package com.farmacia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.farmacia.validation.CPF;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nome;

	@NotBlank
	@CPF
	@Column(unique = true)
	private String cpf;

	@NotBlank(message = "E-mail obrigatório e válido")
	@Email(message = "E-mail obrigatório e válido")
	@Column(unique = true)
	private String email;

	@NotNull
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataNascimento;
}
