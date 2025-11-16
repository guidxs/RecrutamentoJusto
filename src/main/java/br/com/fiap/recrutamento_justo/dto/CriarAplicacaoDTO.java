package br.com.fiap.recrutamento_justo.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para criar uma aplicação.
 */
public record CriarAplicacaoDTO(
    @NotNull(message = "ID da vaga é obrigatório")
    Long vagaId
) {}

