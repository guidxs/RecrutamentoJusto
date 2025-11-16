package br.com.fiap.recrutamento_justo.dto;

import br.com.fiap.recrutamento_justo.model.AreaAtuacao;
import br.com.fiap.recrutamento_justo.model.NivelSenioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para criação/atualização de vaga.
 */
public record VagaDTO(
    Long id,

    @NotBlank(message = "Título é obrigatório")
    String titulo,

    @NotBlank(message = "Descrição é obrigatória")
    String descricao,

    @NotNull(message = "Área de atuação é obrigatória")
    AreaAtuacao areaAtuacao,

    @NotNull(message = "Nível de senioridade é obrigatório")
    NivelSenioridade nivelSenioridade,

    List<String> habilidadesRequeridas,
    BigDecimal salarioMin,
    BigDecimal salarioMax,

    @NotBlank(message = "Empresa é obrigatória")
    String empresa,

    String localizacao,
    Boolean remoto,
    LocalDateTime dataExpiracao,
    Boolean ativa
) {}

