package br.com.fiap.recrutamento_justo.dto;

import br.com.fiap.recrutamento_justo.model.AreaAtuacao;
import br.com.fiap.recrutamento_justo.model.DadosSensiveisVO;
import br.com.fiap.recrutamento_justo.model.EnderecoVO;

import java.util.List;

/**
 * DTO para resposta de candidato (dados públicos ou sensíveis conforme permissão).
 */
public record CandidatoResponseDTO(
    Long id,
    String resumoProfissional,
    List<String> habilidades,
    AreaAtuacao areaInteresse,
    String experienciasRelevantes,
    Integer anosCodificacao,

    // Campos sensíveis - retornados apenas se liberados
    DadosSensiveisVO dadosSensiveis,
    EnderecoVO endereco,
    Boolean dadosSensiveisLiberados
) {}

