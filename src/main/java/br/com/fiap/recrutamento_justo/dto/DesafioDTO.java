package br.com.fiap.recrutamento_justo.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * DTO para desafio técnico.
 */
public record DesafioDTO(
    Long id,
    Long aplicacaoId,

    @NotBlank(message = "Descrição do desafio é obrigatória")
    String descricaoDesafio,

    String solucaoCandidato,
    LocalDateTime dataEnvio,
    LocalDateTime dataResposta,
    LocalDateTime prazoEntrega,
    Integer notaDesafio,
    String feedbackAvaliacao
) {}

