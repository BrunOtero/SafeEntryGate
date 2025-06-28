package com.safeentry.Gate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

// DTO para mapear a resposta do Serviço de Visitantes (para o agendamento)
// Precisa ser compatível com o AgendamentoResponse do Visit Service
public class VisitServiceAgendamentoResponse {
    private UUID id;
    private UUID moradorId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHoraVisita;
    private VisitanteInfo visitante; // Usaremos a mesma classe VisitanteInfo do Visit Service aqui
    private String qrToken;
    private Boolean usado;
    private String status; // String para o enum AgendamentoStatus do Visit Service
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime criadoEm;

    // Classe interna para VisitanteInfo para evitar duplicação ou dependência cíclica
    // Se quiser, pode criar um pacote 'shared' para DTOs comuns
    public static class VisitanteInfo {
        @JsonProperty("nome")
        private String nome;
        @JsonProperty("documento")
        private String documento;
        @JsonProperty("veiculo")
        private String veiculo;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getDocumento() { return documento; }
        public void setDocumento(String documento) { this.documento = documento; }
        public String getVeiculo() { return veiculo; }
        public void setVeiculo(String veiculo) { this.veiculo = veiculo; }
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getMoradorId() { return moradorId; }
    public void setMoradorId(UUID moradorId) { this.moradorId = moradorId; }
    public LocalDateTime getDataHoraVisita() { return dataHoraVisita; }
    public void setDataHoraVisita(LocalDateTime dataHoraVisita) { this.dataHoraVisita = dataHoraVisita; }
    public VisitanteInfo getVisitante() { return visitante; }
    public void setVisitante(VisitanteInfo visitante) { this.visitante = visitante; }
    public String getQrToken() { return qrToken; }
    public void setQrToken(String qrToken) { this.qrToken = qrToken; }
    public Boolean getUsado() { return usado; }
    public void setUsado(Boolean usado) { this.usado = usado; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}