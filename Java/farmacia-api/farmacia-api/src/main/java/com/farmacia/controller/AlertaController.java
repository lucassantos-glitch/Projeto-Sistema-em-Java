// AlertaController.java
package com.farmacia.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.farmacia.model.Medicamento;
import com.farmacia.service.AlertaService;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/alertas")
@Tag(name = "Alerta")
public class AlertaController {
    @Autowired
    private AlertaService alertaService;

    @Operation(summary = "Listar medicamentos com estoque baixo", description = "Retorna todos os medicamentos cujo estoque está abaixo do mínimo definido.")
    @GetMapping("/estoque-baixo")
    public List<Medicamento> listarEstoqueBaixo() {
        return alertaService.buscarEstoqueBaixo();
    }

    @Operation(summary = "Listar medicamentos com validade próxima", description = "Retorna todos os medicamentos cuja validade está próxima de expirar.")
 
    @GetMapping("/validade-proxima")
    public List<Medicamento> listarValidadeProxima() {
        return alertaService.buscarValidadeProxima();
    }
}