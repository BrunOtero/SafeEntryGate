package com.safeentry.Gate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.UUID;

// DTO para a resposta de uma entrada registrada
public class EntradaResponse {
    private UUID id;
    private UUID agendamentoId;
    private UUID porteiroId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime dataHoraEntrada;
    private String observacoes;

    public EntradaResponse(UUID id, UUID agendamentoId, UUID porteiroId, LocalDateTime dataHoraEntrada, String observacoes) {
        this.id = id;
        this.agendamentoId = agendamentoId;
        this.porteiroId = porteiroId;
        this.dataHoraEntrada = dataHoraEntrada;
        this.observacoes = observacoes;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getAgendamentoId() {
        return agendamentoId;
    }

    public UUID getPorteiroId() {
        return porteiroId;
    }

    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    public String getObservacoes() {
        return observacoes;
    }
}