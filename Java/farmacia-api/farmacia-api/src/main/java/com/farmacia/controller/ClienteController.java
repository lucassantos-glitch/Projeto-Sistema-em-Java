// ClienteController.java
package com.farmacia.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.farmacia.model.Cliente;
import com.farmacia.service.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;


    @Operation(summary = "Listar clientes", description = "Retorna todos os clientes cadastrados.")
    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listar();
    }


    @Operation(summary = "Cadastrar cliente", description = "Cadastra um novo cliente no sistema.")
    @PostMapping
    public Cliente criar(@Valid @RequestBody Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        return clienteService.criar(cliente);
    }


    @Operation(summary = "Buscar cliente por ID", description = "Retorna os detalhes de um cliente específico pelo seu ID.")
    @GetMapping("/{id}")
    public Optional<Cliente> buscar(@PathVariable Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(clienteService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza as informações de um cliente existente pelo ID.")
    @PutMapping("/{id}")
    public Optional<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(clienteService.atualizar(id, cliente));
    }

}