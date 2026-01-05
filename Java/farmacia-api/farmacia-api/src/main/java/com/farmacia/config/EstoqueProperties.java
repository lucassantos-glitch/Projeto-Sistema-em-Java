package com.farmacia.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Propriedades de configuração do estoque
 */
@Component
@ConfigurationProperties(prefix = "farmacia.estoque")
public class EstoqueProperties {

    /**
     * Quantidade mínima de estoque para alertas
     */
    private Integer minimo = 10;

    public Integer getMinimo() {
        return minimo;
    }

    public void setMinimo(Integer minimo) {
        this.minimo = minimo;
    }
}
