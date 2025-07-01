package com.safeentry.Gate.service;

import com.safeentry.Gate.dto.EntradaRequest;
import com.safeentry.Gate.dto.VisitServiceAgendamentoResponse;
import com.safeentry.Gate.model.Entrada;
import com.safeentry.Gate.repository.EntradaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EntradaService {

    private final EntradaRepository entradaRepository;
    private final VisitServiceCommunicator visitServiceCommunicator;

    public EntradaService(EntradaRepository entradaRepository, VisitServiceCommunicator visitServiceCommunicator) {
        this.entradaRepository = entradaRepository;
        this.visitServiceCommunicator = visitServiceCommunicator;
    }

    @Transactional
    // Adicionado authorizationHeader
    public Entrada registrarEntrada(EntradaRequest request, UUID porteiroId, String authorizationHeader) {
        // 1. Validar o QR Token com o Serviço de Visitantes
        VisitServiceAgendamentoResponse agendamentoResponse = visitServiceCommunicator
                .getAgendamentoByQrToken(request.getQrToken(), authorizationHeader) // Passa o authorizationHeader
                .blockOptional()
                .orElseThrow(() -> new IllegalArgumentException("QR Token inválido ou agendamento não encontrado."));

        // 2. Realizar validações adicionais (ex: verificar se o agendamento está pendente e não expirou)
        if ("usado".equals(agendamentoResponse.getStatus())) {
            throw new IllegalStateException("Este agendamento já foi utilizado.");
        }
        if ("cancelado".equals(agendamentoResponse.getStatus())) {
            throw new IllegalStateException("Este agendamento foi cancelado.");
        }
        if ("expirado".equals(agendamentoResponse.getStatus())) {
            throw new IllegalStateException("Este agendamento expirou.");
        }
        if (agendamentoResponse.getDataHoraVisita().isAfter(LocalDateTime.now().plusMinutes(30))) {
            System.out.println("Aviso: Tentativa de entrada muito antes do horário agendado. Agendamento para: " + agendamentoResponse.getDataHoraVisita());
        }

        // 3. Registrar a entrada no banco de dados do Gate Service
        Entrada entrada = new Entrada();
        entrada.setAgendamentoId(agendamentoResponse.getId());
        entrada.setPorteiroId(porteiroId);
        entrada.setObservacoes(request.getObservacoes());
        entrada.setDataHoraEntrada(LocalDateTime.now());

        Entrada savedEntrada = entradaRepository.save(entrada);

        // 4. Marcar o agendamento como usado no Serviço de Visitantes
        visitServiceCommunicator.markAgendamentoAsUsed(request.getQrToken(), authorizationHeader) // Passa o authorizationHeader
                .block();

        System.out.println("Entrada registrada com sucesso para agendamento ID: " + agendamentoResponse.getId());

        return savedEntrada;
    }

    public List<Entrada> getEntradasByPorteiro(UUID porteiroId) {
        return entradaRepository.findByPorteiroIdOrderByDataHoraEntradaDesc(porteiroId); // Modificação aqui
    }
}