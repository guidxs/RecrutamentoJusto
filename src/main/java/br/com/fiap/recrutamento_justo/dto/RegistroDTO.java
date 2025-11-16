package br.com.fiap.recrutamento_justo.dto;

import br.com.fiap.recrutamento_justo.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para registro de novo usuário no sistema.
 */
public record RegistroDTO(
    @NotBlank(message = "Login é obrigatório")
    String login,

    @NotBlank(message = "Senha é obrigatória")
    String senha,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    @NotNull(message = "Role é obrigatória")
    UserRole role
) {}

