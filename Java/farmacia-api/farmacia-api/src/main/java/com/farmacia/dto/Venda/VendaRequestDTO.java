package com.farmacia.dto.Venda;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.Valid;
import java.util.List;

public class VendaRequestDTO {
	@NotNull(message = "ID do cliente é obrigatório")
	@Schema(description = "ID do cliente", example = "1")
	private Long clienteId;

	@NotEmpty(message = "Lista de itens não pode estar vazia")
	@Valid
	@Schema(description = "Lista de itens da venda")
	private List<ItemVendaDTO> itens;

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public List<ItemVendaDTO> getItens() {
		return itens;
	}

	public void setItens(List<ItemVendaDTO> itens) {
		this.itens = itens;
	}
}
