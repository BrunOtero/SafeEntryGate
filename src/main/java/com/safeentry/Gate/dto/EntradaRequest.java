package com.safeentry.Gate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

// DTO para a requisição de registro de entrada
public class EntradaRequest {

    @NotBlank(message = "O token QR é obrigatório.")
    private String qrToken;

    private String observacoes;

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