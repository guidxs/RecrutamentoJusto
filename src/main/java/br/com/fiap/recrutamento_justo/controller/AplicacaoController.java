package br.com.fiap.recrutamento_justo.controller;

import br.com.fiap.recrutamento_justo.dto.AplicacaoDTO;
import br.com.fiap.recrutamento_justo.dto.CriarAplicacaoDTO;
import br.com.fiap.recrutamento_justo.model.Candidato;
import br.com.fiap.recrutamento_justo.model.StatusAplicacao;
import br.com.fiap.recrutamento_justo.model.Usuario;
import br.com.fiap.recrutamento_justo.repository.CandidatoRepository;
import br.com.fiap.recrutamento_justo.service.AplicacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciamento de aplicações a vagas.
 * Implementa lógica de scoring por IA e controle de acesso a dados sensíveis.
 */
@RestController
@RequestMapping("/aplicacoes")
public class AplicacaoController {

    private final AplicacaoService aplicacaoService;
    private final CandidatoRepository candidatoRepository;

    public AplicacaoController(AplicacaoService aplicacaoService, CandidatoRepository candidatoRepository) {
        this.aplicacaoService = aplicacaoService;
        this.candidatoRepository = candidatoRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<AplicacaoDTO> aplicar(
            @RequestBody @Valid CriarAplicacaoDTO criarAplicacaoDTO,
            @AuthenticationPrincipal Usuario usuario) {
        Candidato candidato = candidatoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        AplicacaoDTO aplicacao = aplicacaoService.aplicar(candidato.getId(), criarAplicacaoDTO.vagaId());
        return ResponseEntity.status(201).body(aplicacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AplicacaoDTO> buscarPorId(@PathVariable Long id) {
        AplicacaoDTO aplicacao = aplicacaoService.buscarPorId(id);
        return ResponseEntity.ok(aplicacao);
    }

    @GetMapping("/minhas-aplicacoes")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<Page<AplicacaoDTO>> listarMinhasAplicacoes(
            Pageable pageable,
            @AuthenticationPrincipal Usuario usuario) {
        Candidato candidato = candidatoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        Page<AplicacaoDTO> aplicacoes = aplicacaoService.listarPorCandidato(candidato.getId(), pageable);
        return ResponseEntity.ok(aplicacoes);
    }

    @GetMapping("/vaga/{vagaId}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Page<AplicacaoDTO>> listarPorVaga(
            @PathVariable Long vagaId,
            Pageable pageable) {
        Page<AplicacaoDTO> aplicacoes = aplicacaoService.listarPorVaga(vagaId, pageable);
        return ResponseEntity.ok(aplicacoes);
    }

    @GetMapping("/vaga/{vagaId}/ranking")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<List<AplicacaoDTO>> listarRankingPorVaga(@PathVariable Long vagaId) {
        List<AplicacaoDTO> ranking = aplicacaoService.listarRankingPorVaga(vagaId);
        return ResponseEntity.ok(ranking);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<AplicacaoDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusAplicacao novoStatus) {
        AplicacaoDTO atualizada = aplicacaoService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        aplicacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
