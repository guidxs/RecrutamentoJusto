package br.com.fiap.recrutamento_justo.model;

/**
 * Enum que define os níveis de senioridade.
 */
public enum NivelSenioridade {
    ESTAGIO("Estágio"),
    JUNIOR("Júnior"),
    PLENO("Pleno"),
    SENIOR("Sênior"),
    ESPECIALISTA("Especialista");

    private final String descricao;

    NivelSenioridade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

