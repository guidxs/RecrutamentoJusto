package br.com.fiap.recrutamento_justo.dto;

import br.com.fiap.recrutamento_justo.model.AreaAtuacao;
import br.com.fiap.recrutamento_justo.model.DadosSensiveisVO;
import br.com.fiap.recrutamento_justo.model.EnderecoVO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO para criação/atualização de candidato.
 */
public record CandidatoDTO(
    Long id,
    Long usuarioId,

    @NotNull(message = "Dados sensíveis são obrigatórios")
    DadosSensiveisVO dadosSensiveis,

    @NotNull(message = "Endereço é obrigatório")
    EnderecoVO endereco,

    @NotBlank(message = "Resumo profissional é obrigatório")
    String resumoProfissional,

    List<String> habilidades,

    @NotNull(message = "Área de interesse é obrigatória")
    AreaAtuacao areaInteresse,

    String experienciasRelevantes,
    Integer anosCodificacao,
    Boolean dadosSensiveisLiberados
) {}

