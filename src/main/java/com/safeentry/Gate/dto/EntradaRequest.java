package com.safeentry.Gate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

// DTO para a requisição de registro de entrada
public class EntradaRequest {

    @NotBlank(message = "O token QR é obrigatório.")
    private String qrToken;

    // O porteiroId virá do token JWT do porteiro, não do payload da requisição.
    // Incluí-lo aqui seria apenas para fins de teste sem autenticação configurada.
    // Para fins de demonstração, vou mantê-lo no service, injetando um mock ou obtendo do Authentication.

    private String observacoes; // Campo opcional

    // Getters e Setters
    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}