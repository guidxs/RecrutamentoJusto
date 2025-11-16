package br.com.fiap.recrutamento_justo.model;

/**
 * Enum que define o status de uma aplicação para vaga.
 */
public enum StatusAplicacao {
    APLICADO("Candidatura enviada"),
    EM_ANALISE("Em análise pela IA"),
    DESAFIO_ENVIADO("Desafio técnico enviado"),
    DESAFIO_CONCLUIDO("Desafio técnico concluído"),
    PRE_SELECIONADO("Pré-selecionado para entrevista"),
    ENTREVISTA_AGENDADA("Entrevista agendada"),
    APROVADO("Aprovado"),
    REPROVADO("Reprovado");

    private final String descricao;

    StatusAplicacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

