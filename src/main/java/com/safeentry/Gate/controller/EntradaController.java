package com.safeentry.Gate.controller;

import com.safeentry.Gate.dto.EntradaRequest;
import com.safeentry.Gate.dto.EntradaResponse;
import com.safeentry.Gate.model.Entrada;
import com.safeentry.Gate.service.EntradaService;
import com.safeentry.Gate.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    private final EntradaService entradaService;
    private final JwtUtil jwtUtil;

    public EntradaController(EntradaService entradaService, JwtUtil jwtUtil) {
        this.entradaService = entradaService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<EntradaResponse> registrarEntrada(@Valid @RequestBody EntradaRequest request,
                                                            Authentication authentication,
                                                            HttpServletRequest httpRequest) {
        try {
            UUID porteiroId = null;
            String authorizationHeader = httpRequest.getHeader("Authorization");
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                    String jwt = authorizationHeader.substring(7); // Extrair o token JWT
                    porteiroId = jwtUtil.extractUserId(jwt); // Extrair o userId do token
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT não fornecido ou formato inválido.");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado ou ID do porteiro não disponível.");
            }

            Entrada novaEntrada = entradaService.registrarEntrada(request, porteiroId, authorizationHeader);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novaEntrada));
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao registrar entrada: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<List<EntradaResponse>> getMyEntradas(Authentication authentication,
                                                               HttpServletRequest httpRequest) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }

        String authorizationHeader = httpRequest.getHeader("Authorization"); // Obter o cabeçalho Authorization
        UUID porteiroId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7); // Extrair o token JWT
            porteiroId = jwtUtil.extractUserId(jwt); // Extrair o userId do token
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT não fornecido ou formato inválido.");
        }

        List<Entrada> entradas = entradaService.getEntradasByPorteiro(porteiroId);
        List<EntradaResponse> responses = entradas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    private EntradaResponse convertToDto(Entrada entrada) {
        return new EntradaResponse(
                entrada.getId(),
                entrada.getAgendamentoId(),
                entrada.getPorteiroId(),
                entrada.getDataHoraEntrada(),
                entrada.getObservacoes()
        );
    }
}