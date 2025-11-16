package br.com.fiap.recrutamento_justo.controller;

import br.com.fiap.recrutamento_justo.dto.CandidatoDTO;
import br.com.fiap.recrutamento_justo.dto.CandidatoResponseDTO;
import br.com.fiap.recrutamento_justo.model.Usuario;
import br.com.fiap.recrutamento_justo.service.CandidatoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para gerenciamento de candidatos.
 * Controla acesso a dados sensíveis conforme permissões.
 */
@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    private final CandidatoService candidatoService;

    public CandidatoController(CandidatoService candidatoService) {
        this.candidatoService = candidatoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<CandidatoDTO> criar(
            @RequestBody @Valid CandidatoDTO candidatoDTO,
            @AuthenticationPrincipal Usuario usuario) {
        CandidatoDTO criado = candidatoService.criar(candidatoDTO, usuario.getId());
        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatoResponseDTO> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        boolean podeVerDadosSensiveis = usuario.getRole().name().equals("ADMIN") ||
                                        usuario.getRole().name().equals("RH");
        CandidatoResponseDTO candidato = candidatoService.buscarPorId(id, podeVerDadosSensiveis);
        return ResponseEntity.ok(candidato);
    }

    @GetMapping("/meu-perfil")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<CandidatoResponseDTO> buscarMeuPerfil(
            @AuthenticationPrincipal Usuario usuario) {
        CandidatoResponseDTO candidato = candidatoService.buscarPorUsuarioId(usuario.getId(), true);
        return ResponseEntity.ok(candidato);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Page<CandidatoResponseDTO>> listarTodos(
            Pageable pageable,
            @AuthenticationPrincipal Usuario usuario) {
        boolean podeVerDadosSensiveis = usuario.getRole().name().equals("ADMIN") ||
                                        usuario.getRole().name().equals("RH");
        Page<CandidatoResponseDTO> candidatos = candidatoService.listarTodos(pageable, podeVerDadosSensiveis);
        return ResponseEntity.ok(candidatos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidatoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid CandidatoDTO candidatoDTO,
            @AuthenticationPrincipal Usuario usuario) {
        CandidatoDTO atualizado = candidatoService.atualizar(id, candidatoDTO);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        candidatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/liberar-dados")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<String> liberarDadosSensiveis(@PathVariable Long id) {
        candidatoService.liberarDadosSensiveis(id);
        return ResponseEntity.ok("Dados sensíveis liberados com sucesso");
    }
}
