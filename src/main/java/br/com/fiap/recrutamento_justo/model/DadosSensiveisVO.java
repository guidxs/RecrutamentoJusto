package br.com.fiap.recrutamento_justo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Value Object para armazenar informações sensíveis do candidato.
 * Estas informações são ocultadas inicialmente e liberadas apenas após pré-seleção.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DadosSensiveisVO {

    private String nomeCompleto;
    private Integer idade;
    private String genero;
    private String instituicaoEnsino;
    private String urlFoto;
    private String telefone;
}
