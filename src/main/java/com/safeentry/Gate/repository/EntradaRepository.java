package com.safeentry.Gate.repository;

import com.safeentry.Gate.model.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, UUID> {
    List<Entrada> findByPorteiroId(UUID porteiroId);
    List<Entrada> findByPorteiroIdOrderByDataHoraEntradaDesc(UUID porteiroId);
}