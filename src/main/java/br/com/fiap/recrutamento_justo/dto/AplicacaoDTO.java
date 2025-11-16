package br.com.fiap.recrutamento_justo.dto;

import br.com.fiap.recrutamento_justo.model.StatusAplicacao;

import java.time.LocalDateTime;

/**
 * DTO para aplicação a uma vaga.
 */
public record AplicacaoDTO(
    Long id,
    Long candidatoId,
    Long vagaId,
    StatusAplicacao status,
    LocalDateTime dataAplicacao,
    Integer scoreIA,
    String analiseIA,
    Boolean dadosSensiveisLiberados
) {}

