package br.com.fiap.recrutamento_justo.controller;

import br.com.fiap.recrutamento_justo.dto.DesafioDTO;
import br.com.fiap.recrutamento_justo.model.Usuario;
import br.com.fiap.recrutamento_justo.service.DesafioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controller para gerenciamento de desafios técnicos.
 */
@RestController
@RequestMapping("/desafios")
public class DesafioController {

    private final DesafioService desafioService;

    public DesafioController(DesafioService desafioService) {
        this.desafioService = desafioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<DesafioDTO> criar(
            @RequestParam Long aplicacaoId,
            @RequestParam String descricao,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime prazo) {
        DesafioDTO desafio = desafioService.criar(aplicacaoId, descricao, prazo);
        return ResponseEntity.status(201).body(desafio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesafioDTO> buscarPorId(@PathVariable Long id) {
        DesafioDTO desafio = desafioService.buscarPorId(id);
        return ResponseEntity.ok(desafio);
    }

    @GetMapping("/aplicacao/{aplicacaoId}")
    public ResponseEntity<DesafioDTO> buscarPorAplicacao(@PathVariable Long aplicacaoId) {
        DesafioDTO desafio = desafioService.buscarPorAplicacao(aplicacaoId);
        return ResponseEntity.ok(desafio);
    }

    @PatchMapping("/{id}/responder")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<DesafioDTO> responder(
            @PathVariable Long id,
            @RequestParam String solucao) {
        DesafioDTO desafio = desafioService.responderDesafio(id, solucao);
        return ResponseEntity.ok(desafio);
    }

    @PatchMapping("/{id}/avaliar")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<DesafioDTO> avaliar(
            @PathVariable Long id,
            @RequestParam Integer nota,
            @RequestParam String feedback,
            @AuthenticationPrincipal Usuario usuario) {
        DesafioDTO desafio = desafioService.avaliarDesafio(id, nota, feedback, usuario.getId());
        return ResponseEntity.ok(desafio);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        desafioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
