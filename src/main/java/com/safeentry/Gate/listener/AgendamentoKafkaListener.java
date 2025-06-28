package com.safeentry.Gate.listener;

import com.safeentry.Gate.dto.VisitServiceAgendamentoResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoKafkaListener {

    // Listener para o tópico de agendamentos criados
    // O id do grupo de consumidores ("gate-service-group") está definido no application.properties
    @KafkaListener(topics = "agendamentos-criados", groupId = "${spring.kafka.consumer.group-id}")
    public void listenAgendamentoCriado(VisitServiceAgendamentoResponse agendamento) {
        System.out.println("Mensagem Kafka recebida - Novo Agendamento Criado: " + agendamento.getId() +
                           " - Morador: " + agendamento.getMoradorId() +
                           " - Visitante: " + agendamento.getVisitante().getNome() +
                           " - Token QR: " + agendamento.getQrToken());

        // Aqui você pode adicionar lógica para o que fazer com a mensagem.
        // Exemplos:
        // - Salvar o agendamento em um cache local (Redis, por exemplo) para consultas mais rápidas.
        // - Inserir em uma fila de trabalho para processamento posterior.
        // Para o escopo atual, apenas registramos a entrada via REST, então este listener é mais para observação
        // ou para futuras funcionalidades assíncronas (como notificar a portaria sobre visitantes esperados).
    }
}
