package com.farmacia.dto.Venda;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public class ItemVendaDTO {
	@Schema(description = "ID do medicamento", example = "1")
	private Long medicamentoId;

	@Min(value = 1, message = "Quantidade deve ser maior que zero")
	@Schema(description = "Quantidade do medicamento", example = "2")
	private Integer quantidade;

	public Long getMedicamentoId() {
		return medicamentoId;
	}

	public void setMedicamentoId(Long medicamentoId) {
		this.medicamentoId = medicamentoId;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
}
