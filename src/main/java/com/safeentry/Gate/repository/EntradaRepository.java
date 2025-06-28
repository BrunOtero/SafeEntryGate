package com.safeentry.Gate.repository;

import com.safeentry.Gate.model.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, UUID> {
    // Você pode adicionar métodos de busca específicos aqui se precisar, por exemplo:
    // List<Entrada> findByPorteiroId(UUID porteiroId);
    // List<Entrada> findByAgendamentoId(UUID agendamentoId);
}