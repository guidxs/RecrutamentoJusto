-- Criação das tabelas principais do sistema RecrutamentoJusto

-- Tabela de usuários (base de autenticação)
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_login (login),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabela de candidatos
CREATE TABLE candidatos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL UNIQUE,

    -- Dados sensíveis (ocultados inicialmente)
    nome_completo VARCHAR(200),
    idade INT,
    genero VARCHAR(50),
    instituicao_ensino VARCHAR(200),
    url_foto VARCHAR(500),
    telefone VARCHAR(20),

    -- Endereço (dados sensíveis)
    cep VARCHAR(10),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    bairro VARCHAR(100),
    logradouro VARCHAR(200),
    numero VARCHAR(20),
    complemento VARCHAR(100),

    -- Dados públicos para análise inicial
    resumo_profissional TEXT,
    area_interesse VARCHAR(50),
    experiencias_relevantes VARCHAR(1000),
    anos_codificacao INT,

    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dados_sensiveis_liberados BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_area_interesse (area_interesse)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabela de habilidades do candidato
CREATE TABLE candidato_habilidades (
    candidato_id BIGINT NOT NULL,
    habilidade VARCHAR(100) NOT NULL,
    FOREIGN KEY (candidato_id) REFERENCES candidatos(id) ON DELETE CASCADE,
    INDEX idx_candidato (candidato_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabela de vagas
CREATE TABLE vagas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT NOT NULL,
    area_atuacao VARCHAR(50) NOT NULL,
    nivel_senioridade VARCHAR(30) NOT NULL,
    salario_min DECIMAL(10,2),
    salario_max DECIMAL(10,2),
    empresa VARCHAR(200) NOT NULL,
    localizacao VARCHAR(200),
    remoto BOOLEAN NOT NULL DEFAULT FALSE,
    criado_por_id BIGINT NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_expiracao DATETIME,
    ativa BOOLEAN NOT NULL DEFAULT TRUE,

    FOREIGN KEY (criado_por_id) REFERENCES usuarios(id),
    INDEX idx_area_atuacao (area_atuacao),
    INDEX idx_ativa (ativa),
    INDEX idx_data_criacao (data_criacao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabela de habilidades requeridas pela vaga
CREATE TABLE vaga_habilidades_requeridas (
    vaga_id BIGINT NOT NULL,
    habilidade VARCHAR(100) NOT NULL,
    FOREIGN KEY (vaga_id) REFERENCES vagas(id) ON DELETE CASCADE,
    INDEX idx_vaga (vaga_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabela de aplicações
CREATE TABLE aplicacoes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidato_id BIGINT NOT NULL,
    vaga_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    data_aplicacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Análise da IA
    score_ia INT,
    analise_ia VARCHAR(1000),

    -- Controle de liberação de dados sensíveis
    dados_sensiveis_liberados BOOLEAN NOT NULL DEFAULT FALSE,
    data_liberacao_dados DATETIME,
    liberado_por_id BIGINT,

    FOREIGN KEY (candidato_id) REFERENCES candidatos(id) ON DELETE CASCADE,
    FOREIGN KEY (vaga_id) REFERENCES vagas(id) ON DELETE CASCADE,
    FOREIGN KEY (liberado_por_id) REFERENCES usuarios(id),
    UNIQUE KEY uk_candidato_vaga (candidato_id, vaga_id),
    INDEX idx_candidato (candidato_id),
    INDEX idx_vaga (vaga_id),
    INDEX idx_status (status),
    INDEX idx_score (score_ia)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabela de desafios técnicos
CREATE TABLE desafios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    aplicacao_id BIGINT NOT NULL UNIQUE,
    descricao_desafio TEXT NOT NULL,
    solucao_candidato TEXT,
    data_envio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_resposta DATETIME,
    prazo_entrega DATETIME,
    nota_desafio INT,
    feedback_avaliacao VARCHAR(1000),
    avaliado_por_id BIGINT,

    FOREIGN KEY (aplicacao_id) REFERENCES aplicacoes(id) ON DELETE CASCADE,
    FOREIGN KEY (avaliado_por_id) REFERENCES usuarios(id),
    INDEX idx_aplicacao (aplicacao_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

