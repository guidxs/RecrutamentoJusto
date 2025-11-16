package br.com.fiap.recrutamento_justo.controller;

import br.com.fiap.recrutamento_justo.dto.VagaDTO;
import br.com.fiap.recrutamento_justo.model.Usuario;
import br.com.fiap.recrutamento_justo.service.VagaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para gerenciamento de vagas.
 */
@RestController
@RequestMapping("/vagas")
public class VagaController {

    private final VagaService vagaService;

    public VagaController(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<VagaDTO> criar(
            @RequestBody @Valid VagaDTO vagaDTO,
            @AuthenticationPrincipal Usuario usuario) {
        VagaDTO criada = vagaService.criar(vagaDTO, usuario.getId());
        return ResponseEntity.status(201).body(criada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VagaDTO> buscarPorId(@PathVariable Long id) {
        VagaDTO vaga = vagaService.buscarPorId(id);
        return ResponseEntity.ok(vaga);
    }

    @GetMapping
    public ResponseEntity<Page<VagaDTO>> listarAtivas(Pageable pageable) {
        Page<VagaDTO> vagas = vagaService.listarAtivas(pageable);
        return ResponseEntity.ok(vagas);
    }

    @GetMapping("/todas")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Page<VagaDTO>> listarTodas(Pageable pageable) {
        Page<VagaDTO> vagas = vagaService.listarTodas(pageable);
        return ResponseEntity.ok(vagas);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<VagaDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid VagaDTO vagaDTO) {
        VagaDTO atualizada = vagaService.atualizar(id, vagaDTO);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vagaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<String> desativar(@PathVariable Long id) {
        vagaService.desativar(id);
        return ResponseEntity.ok("Vaga desativada com sucesso");
    }
}
