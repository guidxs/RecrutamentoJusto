package br.com.fiap.recrutamento_justo.dto;

/**
 * DTO padronizado para respostas de erro da API.
 */
public record ErrorResponseDTO(
    Integer status,
    String error,
    String message,
    String path,
    Long timestamp
) {
    public ErrorResponseDTO(Integer status, String error, String message, String path) {
        this(status, error, message, path, System.currentTimeMillis());
    }
}

