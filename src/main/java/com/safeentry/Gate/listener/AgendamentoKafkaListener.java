package com.safeentry.Gate.listener;

import com.safeentry.Gate.dto.VisitServiceAgendamentoResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoKafkaListener {

    @KafkaListener(topics = "agendamentos-criados", groupId = "${spring.kafka.consumer.group-id}")
    public void listenAgendamentoCriado(VisitServiceAgendamentoResponse agendamento) {
        System.out.println("Mensagem Kafka recebida - Novo Agendamento Criado: " + agendamento.getId() +
                           " - Morador: " + agendamento.getMoradorId() +
                           " - Visitante: " + agendamento.getVisitante().getNome() +
                           " - Token QR: " + agendamento.getQrToken());

    }
}