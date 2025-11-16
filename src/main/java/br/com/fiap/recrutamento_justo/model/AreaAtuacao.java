package br.com.fiap.recrutamento_justo.model;

/**
 * Enum que define as áreas de atuação disponíveis para vagas e candidatos.
 */
public enum AreaAtuacao {
    DESENVOLVIMENTO_BACKEND("Desenvolvimento Backend"),
    DESENVOLVIMENTO_FRONTEND("Desenvolvimento Frontend"),
    DESENVOLVIMENTO_FULLSTACK("Desenvolvimento Fullstack"),
    DESENVOLVIMENTO_MOBILE("Desenvolvimento Mobile"),
    CIENCIA_DADOS("Ciência de Dados"),
    ENGENHARIA_DADOS("Engenharia de Dados"),
    DEVOPS("DevOps"),
    SEGURANCA_INFORMACAO("Segurança da Informação"),
    UX_UI_DESIGN("UX/UI Design"),
    ANALISE_SISTEMAS("Análise de Sistemas"),
    GESTAO_PROJETOS("Gestão de Projetos"),
    QUALIDADE_SOFTWARE("Qualidade de Software");

    private final String descricao;

    AreaAtuacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
