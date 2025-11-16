package br.com.fiap.recrutamento_justo.dto;

/**
 * DTO para resposta de autenticação com token JWT.
 */
public record TokenDTO(
    String token,
    String tipo,
    Long expiresIn
) {
    public TokenDTO(String token, Long expiresIn) {
        this(token, "Bearer", expiresIn);
    }
}

