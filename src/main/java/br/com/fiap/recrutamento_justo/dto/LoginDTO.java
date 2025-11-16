package br.com.fiap.recrutamento_justo.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para autenticação (login).
 */
public record LoginDTO(
    @NotBlank(message = "Login é obrigatório")
    String login,

    @NotBlank(message = "Senha é obrigatória")
    String senha
) {}

