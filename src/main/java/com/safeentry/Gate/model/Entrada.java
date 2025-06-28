package com.safeentry.Gate.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "entradas")
public class Entrada {

    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "agendamento_id", nullable = false)
    private UUID agendamentoId; // ID do agendamento que gerou esta entrada

    @Column(name = "porteiro_id", nullable = false)
    private UUID porteiroId; // ID do porteiro que registrou a entrada (viria do token JWT do porteiro)

    @Column(name = "data_hora_entrada", nullable = false)
    private LocalDateTime dataHoraEntrada = LocalDateTime.now();

    @Column(name = "observacoes")
    private String observacoes;

    // Construtor padrão
    public Entrada() {}

    // Construtor para criação
    public Entrada(UUID agendamentoId, UUID porteiroId, String observacoes) {
        this.agendamentoId = agendamentoId;
        this.porteiroId = porteiroId;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(UUID agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public UUID getPorteiroId() {
        return porteiroId;
    }

    public void setPorteiroId(UUID porteiroId) {
        this.porteiroId = porteiroId;
    }

    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
        this.dataHoraEntrada = dataHoraEntrada;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return "Entrada{" +
               "id=" + id +
               ", agendamentoId=" + agendamentoId +
               ", porteiroId=" + porteiroId +
               ", dataHoraEntrada=" + dataHoraEntrada +
               ", observacoes='" + observacoes + '\'' +
               '}';
    }
}