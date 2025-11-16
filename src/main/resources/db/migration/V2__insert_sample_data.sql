-- Inserção de dados de exemplo para testes

-- Usuários de exemplo (senha: 123456 criptografada com BCrypt)
INSERT INTO usuarios (login, senha, email, role, ativo) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@recrutamento.com', 'ADMIN', true),
('rh_maria', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'maria@empresa.com', 'RH', true),
('candidato_joao', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'joao@email.com', 'CANDIDATO', true),
('candidato_ana', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ana@email.com', 'CANDIDATO', true);

-- Candidatos de exemplo
INSERT INTO candidatos (usuario_id, nome_completo, idade, genero, instituicao_ensino, telefone,
                        cep, cidade, estado, bairro, resumo_profissional, area_interesse,
                        anos_codificacao, dados_sensiveis_liberados) VALUES
(3, 'João Silva Santos', 28, 'Masculino', 'FIAP', '11987654321',
 '01310-100', 'São Paulo', 'SP', 'Centro',
 'Desenvolvedor Full Stack com 5 anos de experiência em projetos web escaláveis.',
 'DESENVOLVIMENTO_FULLSTACK', 5, false),
(4, 'Ana Paula Costa', 25, 'Feminino', 'USP', '11976543210',
 '05508-000', 'São Paulo', 'SP', 'Pinheiros',
 'Especialista em Data Science com foco em Machine Learning e análise preditiva.',
 'CIENCIA_DADOS', 3, false);

-- Habilidades dos candidatos
INSERT INTO candidato_habilidades (candidato_id, habilidade) VALUES
(1, 'Java'), (1, 'Spring Boot'), (1, 'React'), (1, 'MySQL'), (1, 'Docker'),
(2, 'Python'), (2, 'Machine Learning'), (2, 'TensorFlow'), (2, 'SQL'), (2, 'Pandas');

-- Vagas de exemplo
INSERT INTO vagas (titulo, descricao, area_atuacao, nivel_senioridade, salario_min, salario_max,
                   empresa, localizacao, remoto, criado_por_id, ativa) VALUES
('Desenvolvedor Full Stack Pleno',
 'Buscamos desenvolvedor Full Stack para atuar em projetos inovadores de transformação digital.',
 'DESENVOLVIMENTO_FULLSTACK', 'PLENO', 8000.00, 12000.00,
 'Tech Solutions Brasil', 'São Paulo - SP', true, 2, true),
('Cientista de Dados Júnior',
 'Oportunidade para cientista de dados júnior trabalhar com análise de grandes volumes de dados.',
 'CIENCIA_DADOS', 'JUNIOR', 6000.00, 9000.00,
 'Data Analytics Corp', 'São Paulo - SP', true, 2, true);

-- Habilidades requeridas pelas vagas
INSERT INTO vaga_habilidades_requeridas (vaga_id, habilidade) VALUES
(1, 'Java'), (1, 'Spring Boot'), (1, 'React'), (1, 'MySQL'),
(2, 'Python'), (2, 'Machine Learning'), (2, 'SQL');

