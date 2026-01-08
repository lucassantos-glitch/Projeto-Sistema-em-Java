package com.farmacia.controller;

import com.farmacia.model.Venda;
import com.farmacia.model.ItemVenda;
import com.farmacia.model.Cliente;
import com.farmacia.model.Medicamento;
import com.farmacia.service.VendaService;
import com.farmacia.repository.ClienteRepository;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.dto.Venda.VendaRequestDTO;
import com.farmacia.dto.Venda.ItemVendaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/vendas")
@Tag(name = "Venda")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;


    @Operation(summary = "Registrar venda", description = "Registra uma nova venda no sistema.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = VendaRequestDTO.class),
            examples = {
                @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "Exemplo mínimo de venda",
                    value = "{\n  \"clienteId\": 1,\n  \"itens\": [\n    {\n      \"medicamentoId\": 1,\n      \"quantidade\": 2\n    }\n  ]\n}"
                )
            }
        )
    )
    @PostMapping
    public ResponseEntity<Venda> registrar(@Valid @RequestBody VendaRequestDTO vendaRequest) {
        // Buscar cliente
        if (vendaRequest.getClienteId() == null) {
            throw new IllegalArgumentException("ID do cliente não pode ser nulo");
        }
        Long clienteId = vendaRequest.getClienteId();
        if (clienteId == null) {
            throw new IllegalArgumentException("ID do cliente não pode ser nulo");
        }
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        // Mapear itens
        java.util.List<ItemVenda> itens = new java.util.ArrayList<>();
        for (ItemVendaDTO itemDTO : vendaRequest.getItens()) {
            Long medicamentoId = itemDTO.getMedicamentoId();
            if (medicamentoId == null) {
                throw new IllegalArgumentException("ID do medicamento não pode ser nulo");
            }
            Medicamento medicamento = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento não encontrado"));
            ItemVenda item = new ItemVenda();
            item.setMedicamento(medicamento);
            item.setQuantidade(itemDTO.getQuantidade());
            itens.add(item);
        }

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setItens(itens);

        Venda vendaRegistrada = vendaService.registrar(venda);
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaRegistrada);
    }


    @Operation(summary = "Listar vendas", description = "Retorna todas as vendas registradas.")
    @GetMapping
    public List<Venda> listar() {
        List<Venda> vendas = vendaService.listar();
        // Backfill: preencher datas nulas com a data atual e salvar
        vendas.stream()
            .filter(v -> v.getData() == null)
            .forEach(v -> {
                v.setData(java.time.LocalDateTime.now());
                vendaService.salvar(v);
            });
        return vendas;
    }


    @Operation(summary = "Buscar venda por ID", description = "Retorna os detalhes de uma venda específica pelo seu ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscar(@PathVariable Long id) {
        return vendaService.listar().stream()
            .filter(v -> v.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }



    @Operation(summary = "Listar vendas por cliente", description = "Retorna todas as vendas realizadas para um cliente específico.")
    @GetMapping("/cliente/{clienteId}")
    public List<Venda> listarPorCliente(@PathVariable Long clienteId) {
        return vendaService.listar().stream()
                .filter(v -> v.getCliente() != null && v.getCliente().getId().equals(clienteId))
                .collect(Collectors.toList());
    }
}
