package com.safeentry.Gate.service;

import com.safeentry.Gate.dto.VisitServiceAgendamentoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class VisitServiceCommunicator {

    private final WebClient webClient;

    public VisitServiceCommunicator(@Value("${visit.service.url}") String visitServiceBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(visitServiceBaseUrl).build();
    }

    // Adicionado authorizationHeader
    public Mono<VisitServiceAgendamentoResponse> getAgendamentoByQrToken(String qrToken, String authorizationHeader) {
        return webClient.get()
                .uri("/qr/{qrToken}", qrToken)
                .header("Authorization", authorizationHeader) // Adiciona o cabeçalho Authorization
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        Mono.error(new RuntimeException("Agendamento não encontrado ou QR inválido no Visit Service.")))
                .onStatus(status -> status.is5xxServerError(), clientResponse ->
                        Mono.error(new RuntimeException("Erro interno no Visit Service ao buscar agendamento.")))
                .bodyToMono(VisitServiceAgendamentoResponse.class);
    }

    // Adicionado authorizationHeader
    public Mono<VisitServiceAgendamentoResponse> markAgendamentoAsUsed(String qrToken, String authorizationHeader) {
        return webClient.patch()
                .uri("/qr/{qrToken}/use", qrToken)
                .header("Authorization", authorizationHeader) // Adiciona o cabeçalho Authorization
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        Mono.error(new RuntimeException("Não foi possível marcar agendamento como usado (já usado ou inválido).")))
                .onStatus(status -> status.is5xxServerError(), clientResponse ->
                        Mono.error(new RuntimeException("Erro interno no Visit Service ao marcar agendamento como usado.")))
                .bodyToMono(VisitServiceAgendamentoResponse.class);
    }
}