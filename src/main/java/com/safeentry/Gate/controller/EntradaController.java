package com.safeentry.Gate.controller;

import com.safeentry.Gate.dto.EntradaRequest;
import com.safeentry.Gate.dto.EntradaResponse;
import com.safeentry.Gate.model.Entrada;
import com.safeentry.Gate.service.EntradaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Para obter o usuário autenticado
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    private final EntradaService entradaService;

    public EntradaController(EntradaService entradaService) {
        this.entradaService = entradaService;
    }

    // Endpoint para registrar uma nova entrada
    // Apenas porteiros podem registrar entradas
    @PostMapping
    // @PreAuthorize("hasAuthority('PORTEIRO')") // Use Spring Security se integrar com Auth Service
    public ResponseEntity<EntradaResponse> registrarEntrada(@Valid @RequestBody EntradaRequest request,
                                                            Authentication authentication) {
        try {
            // Extrair o ID do porteiro do token JWT (assumindo que o ID do usuário é um claim no JWT)
            // Assim como no Visit Service, o porteiroId deve vir do token JWT após autenticação.
            // Para testar, vamos usar um UUID mock.
            UUID porteiroId = null;
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                // Aqui você extrairia o UUID do porteiro do seu JWT, de forma semelhante ao moradorId no Visit Service
                porteiroId = UUID.randomUUID(); // Mock UUID para demonstração. SUBSTITUA ISSO NO SEU APP REAL.
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado ou ID do porteiro não disponível.");
            }

            Entrada novaEntrada = entradaService.registrarEntrada(request, porteiroId);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novaEntrada));
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao registrar entrada: " + e.getMessage());
        }
    }

    // Método auxiliar para converter Entrada para EntradaResponse DTO
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