package com.safeentry.Gate.service;

import com.safeentry.Gate.dto.EntradaRequest;
import com.safeentry.Gate.dto.VisitServiceAgendamentoResponse;
import com.safeentry.Gate.model.Entrada;
import com.safeentry.Gate.repository.EntradaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public Entrada registrarEntrada(EntradaRequest request, UUID porteiroId) {
        // 1. Validar o QR Token com o Serviço de Visitantes
        // Usamos .block() aqui para transformar o Mono em um objeto síncrono.
        // Em um ambiente reativo, você faria toda a cadeia de forma reativa.
        VisitServiceAgendamentoResponse agendamentoResponse = visitServiceCommunicator
                .getAgendamentoByQrToken(request.getQrToken())
                .blockOptional()
                .orElseThrow(() -> new IllegalArgumentException("QR Token inválido ou agendamento não encontrado."));

        // 2. Realizar validações adicionais (ex: verificar se o agendamento está pendente e não expirou)
        if ("USADO".equals(agendamentoResponse.getStatus())) {
            throw new IllegalStateException("Este agendamento já foi utilizado.");
        }
        if ("CANCELADO".equals(agendamentoResponse.getStatus())) {
            throw new IllegalStateException("Este agendamento foi cancelado.");
        }
        if ("EXPIRADO".equals(agendamentoResponse.getStatus())) {
            throw new IllegalStateException("Este agendamento expirou.");
        }
        // Validação da data/hora da visita se for importante que a entrada ocorra próximo ao agendado
        if (agendamentoResponse.getDataHoraVisita().isAfter(LocalDateTime.now().plusMinutes(30))) { // Ex: 30 minutos de tolerância após a data agendada
             // Poderia ser um erro ou apenas um aviso, dependendo da regra de negócio
            System.out.println("Aviso: Tentativa de entrada muito antes do horário agendado. Agendamento para: " + agendamentoResponse.getDataHoraVisita());
        }

        // 3. Registrar a entrada no banco de dados do Gate Service
        Entrada entrada = new Entrada();
        entrada.setAgendamentoId(agendamentoResponse.getId()); // ID do agendamento validado
        entrada.setPorteiroId(porteiroId); // ID do porteiro autenticado
        entrada.setObservacoes(request.getObservacoes());
        entrada.setDataHoraEntrada(LocalDateTime.now()); // Data e hora da entrada

        Entrada savedEntrada = entradaRepository.save(entrada);

        // 4. Marcar o agendamento como usado no Serviço de Visitantes
        visitServiceCommunicator.markAgendamentoAsUsed(request.getQrToken())
                .block(); // Bloqueia para garantir que a atualização ocorra antes de retornar

        System.out.println("Entrada registrada com sucesso para agendamento ID: " + agendamentoResponse.getId());

        return savedEntrada;
    }
}