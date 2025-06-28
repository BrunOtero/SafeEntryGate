package com.safeentry.Gate.service;

import com.safeentry.Gate.dto.VisitServiceAgendamentoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

// Serviço para comunicação com o microsserviço de Visitantes
@Service
public class VisitServiceCommunicator {

    private final WebClient webClient;

    // A URL do serviço de visitantes será injetada do application.properties
    public VisitServiceCommunicator(@Value("${visit.service.url}") String visitServiceBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(visitServiceBaseUrl).build();
    }

    // Busca um agendamento pelo QR Token no serviço de visitantes
    public Mono<VisitServiceAgendamentoResponse> getAgendamentoByQrToken(String qrToken) {
        return webClient.get()
                .uri("/qr/{qrToken}", qrToken)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        Mono.error(new RuntimeException("Agendamento não encontrado ou QR inválido no Visit Service.")))
                .onStatus(status -> status.is5xxServerError(), clientResponse ->
                        Mono.error(new RuntimeException("Erro interno no Visit Service ao buscar agendamento.")))
                .bodyToMono(VisitServiceAgendamentoResponse.class);
    }

    // Marca um agendamento como usado no serviço de visitantes
    public Mono<VisitServiceAgendamentoResponse> markAgendamentoAsUsed(String qrToken) {
        return webClient.patch()
                .uri("/qr/{qrToken}/use", qrToken)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        Mono.error(new RuntimeException("Não foi possível marcar agendamento como usado (já usado ou inválido).")))
                .onStatus(status -> status.is5xxServerError(), clientResponse ->
                        Mono.error(new RuntimeException("Erro interno no Visit Service ao marcar agendamento como usado.")))
                .bodyToMono(VisitServiceAgendamentoResponse.class);
    }
}
