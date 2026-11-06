-- =====================================================================
-- IF Agora - data.sql (dados de EXEMPLO para desenvolvimento e demo)
-- Executado automaticamente pelo Spring Boot DEPOIS do schema.sql.
--
-- Regras deste arquivo:
--   * NENHUM id é escrito à mão. Os ids nascem do identity e as relações
--     são resolvidas por (SELECT id ... WHERE <chave natural>). Assim, os
--     INSERTs feitos depois pelo app não colidem com ids já usados.
--   * Pessoas, e-mails e tokens são FICTÍCIOS. Cursos, turmas, salas e
--     horários são ilustrativos, não refletem a grade real do IFTM.
--   * Senha de todos os usuários de exemplo: senha123 (hash BCrypt abaixo).
--   * Datas e horas são relativas a "agora" (DATEADD é do H2), então a demo
--     sempre mostra avisos e cardápio do dia.
--   * Salvar este arquivo em UTF-8 (o application.properties já exige isso).
-- =====================================================================

-- ---------------------------------------------------------------------
-- campus
-- ---------------------------------------------------------------------
INSERT INTO campus (nome, cidade) VALUES ('Uberaba', 'Uberaba');
INSERT INTO campus (nome, cidade) VALUES ('Uberaba Parque Tecnológico', 'Uberaba');
INSERT INTO campus (nome, cidade) VALUES ('Uberlândia', 'Uberlândia');
INSERT INTO campus (nome, cidade) VALUES ('Uberlândia Centro', 'Uberlândia');
INSERT INTO campus (nome, cidade) VALUES ('Ituiutaba', 'Ituiutaba');
INSERT INTO campus (nome, cidade) VALUES ('Paracatu', 'Paracatu');
INSERT INTO campus (nome, cidade) VALUES ('Patos de Minas', 'Patos de Minas');
INSERT INTO campus (nome, cidade) VALUES ('Patrocínio', 'Patrocínio');
INSERT INTO campus (nome, cidade) VALUES ('Campina Verde', 'Campina Verde');

-- ---------------------------------------------------------------------
-- curso e turma (só o campus Uberaba, para o piloto)
-- ---------------------------------------------------------------------
INSERT INTO curso (campus_id, nome, nivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Técnico em Informática', 'TECNICO_INTEGRADO');
INSERT INTO curso (campus_id, nome, nivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Técnico em Agropecuária', 'TECNICO_INTEGRADO');

INSERT INTO turma (curso_id, nome, ano_letivo) VALUES
    ((SELECT cu.id FROM curso cu JOIN campus ca ON ca.id = cu.campus_id
       WHERE ca.nome = 'Uberaba' AND cu.nome = 'Técnico em Informática'),
     'Informática 2º ano A', 2026);
INSERT INTO turma (curso_id, nome, ano_letivo) VALUES
    ((SELECT cu.id FROM curso cu JOIN campus ca ON ca.id = cu.campus_id
       WHERE ca.nome = 'Uberaba' AND cu.nome = 'Técnico em Agropecuária'),
     'Agropecuária 1º ano A', 2026);

-- ---------------------------------------------------------------------
-- usuario (hash BCrypt de "senha123")
-- ---------------------------------------------------------------------
INSERT INTO usuario (campus_id, turma_id, nome, email, senha_hash, perfil) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), NULL,
     'Admin Exemplo', 'admin@exemplo.com',
     '$2a$10$RojTkUZ2v3YZekJe1OPFXO72459.LZ0RtCUTqUh5PA5Rq.7SpAWgG', 'ADMIN');
INSERT INTO usuario (campus_id, turma_id, nome, email, senha_hash, perfil) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), NULL,
     'Carlos Mendes (professor)', 'carlos.mendes@exemplo.com',
     '$2a$10$RojTkUZ2v3YZekJe1OPFXO72459.LZ0RtCUTqUh5PA5Rq.7SpAWgG', 'PROFESSOR');
INSERT INTO usuario (campus_id, turma_id, nome, email, senha_hash, perfil) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), NULL,
     'Marina Lima (servidora)', 'marina.lima@exemplo.com',
     '$2a$10$RojTkUZ2v3YZekJe1OPFXO72459.LZ0RtCUTqUh5PA5Rq.7SpAWgG', 'SERVIDOR');
INSERT INTO usuario (campus_id, turma_id, nome, email, senha_hash, perfil) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'),
     (SELECT id FROM turma WHERE nome = 'Informática 2º ano A'),
     'Ana Souza', 'ana.souza@exemplo.com',
     '$2a$10$RojTkUZ2v3YZekJe1OPFXO72459.LZ0RtCUTqUh5PA5Rq.7SpAWgG', 'ALUNO');
INSERT INTO usuario (campus_id, turma_id, nome, email, senha_hash, perfil) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'),
     (SELECT id FROM turma WHERE nome = 'Informática 2º ano A'),
     'Bruno Alves', 'bruno.alves@exemplo.com',
     '$2a$10$RojTkUZ2v3YZekJe1OPFXO72459.LZ0RtCUTqUh5PA5Rq.7SpAWgG', 'ALUNO');
INSERT INTO usuario (campus_id, turma_id, nome, email, senha_hash, perfil) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'),
     (SELECT id FROM turma WHERE nome = 'Agropecuária 1º ano A'),
     'Camila Rocha', 'camila.rocha@exemplo.com',
     '$2a$10$RojTkUZ2v3YZekJe1OPFXO72459.LZ0RtCUTqUh5PA5Rq.7SpAWgG', 'ALUNO');

-- ---------------------------------------------------------------------
-- espaco (campus Uberaba; o nome basta para localizar nos INSERTs abaixo)
-- ---------------------------------------------------------------------
INSERT INTO espaco (campus_id, nome, bloco, tipo, acessivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Sala 101', 'Bloco A', 'SALA', TRUE);
INSERT INTO espaco (campus_id, nome, bloco, tipo, acessivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Sala 102', 'Bloco A', 'SALA', TRUE);
INSERT INTO espaco (campus_id, nome, bloco, tipo, acessivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Sala 201', 'Bloco B', 'SALA', FALSE);
INSERT INTO espaco (campus_id, nome, bloco, tipo, acessivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Laboratório de Informática 1', 'Bloco B', 'LABORATORIO', TRUE);
INSERT INTO espaco (campus_id, nome, bloco, tipo, acessivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Biblioteca', 'Bloco C', 'BIBLIOTECA', TRUE);
INSERT INTO espaco (campus_id, nome, bloco, tipo, acessivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Refeitório', 'Bloco C', 'REFEITORIO', TRUE);
INSERT INTO espaco (campus_id, nome, bloco, tipo, acessivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Elevador do Bloco B', 'Bloco B', 'ELEVADOR', TRUE);
INSERT INTO espaco (campus_id, nome, bloco, tipo, acessivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Rampa de acesso do Bloco A', 'Bloco A', 'RAMPA', TRUE);
INSERT INTO espaco (campus_id, nome, bloco, tipo, acessivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Banheiro acessível do Bloco A', 'Bloco A', 'BANHEIRO', TRUE);
INSERT INTO espaco (campus_id, nome, bloco, tipo, acessivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), 'Quadra poliesportiva', NULL, 'QUADRA', TRUE);

-- ---------------------------------------------------------------------
-- aviso
-- ---------------------------------------------------------------------
-- Aula cancelada, só para a turma de Informática 2º ano A
INSERT INTO aviso (campus_id, turma_id, autor_id, espaco_id, tipo, titulo, descricao, expira_em) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'),
     (SELECT id FROM turma WHERE nome = 'Informática 2º ano A'),
     (SELECT id FROM usuario WHERE email = 'carlos.mendes@exemplo.com'),
     (SELECT id FROM espaco WHERE nome = 'Sala 101'),
     'AULA_CANCELADA', 'Aula de Programação Web cancelada hoje',
     'O professor está em reunião. A aula será reposta na próxima semana.',
     DATEADD('HOUR', 8, CURRENT_TIMESTAMP));

-- Sala trocada: a aula passa para o laboratório
INSERT INTO aviso (campus_id, turma_id, autor_id, espaco_id, tipo, titulo, descricao, expira_em) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'),
     (SELECT id FROM turma WHERE nome = 'Informática 2º ano A'),
     (SELECT id FROM usuario WHERE email = 'carlos.mendes@exemplo.com'),
     (SELECT id FROM espaco WHERE nome = 'Laboratório de Informática 1'),
     'SALA_TROCADA', 'Banco de Dados mudou para o Laboratório de Informática 1',
     'A Sala 102 está em manutenção. Levem o notebook, se tiverem.',
     DATEADD('HOUR', 6, CURRENT_TIMESTAMP));

-- Aviso do campus inteiro (turma_id NULL)
INSERT INTO aviso (campus_id, turma_id, autor_id, espaco_id, tipo, titulo, descricao, expira_em) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'),
     NULL,
     (SELECT id FROM usuario WHERE email = 'marina.lima@exemplo.com'),
     (SELECT id FROM espaco WHERE nome = 'Refeitório'),
     'COMUNICADO', 'Almoço será servido mais cedo hoje',
     'O refeitório abre às 11h por causa de uma atividade externa.',
     DATEADD('HOUR', 4, CURRENT_TIMESTAMP));

-- Aviso já expirado (para testar o filtro "só o que é de agora")
INSERT INTO aviso (campus_id, turma_id, autor_id, tipo, titulo, descricao, criado_em, expira_em) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'),
     NULL,
     (SELECT id FROM usuario WHERE email = 'marina.lima@exemplo.com'),
     'ATRASO', 'Ônibus do campus atrasado',
     'O ônibus das 17h atrasou cerca de 20 minutos.',
     DATEADD('HOUR', -5, CURRENT_TIMESTAMP), DATEADD('HOUR', -3, CURRENT_TIMESTAMP));

-- ---------------------------------------------------------------------
-- dispositivo (tokens fictícios) e notificacao
-- ---------------------------------------------------------------------
INSERT INTO dispositivo (usuario_id, token_push, plataforma) VALUES
    ((SELECT id FROM usuario WHERE email = 'ana.souza@exemplo.com'),
     'TOKEN_EXEMPLO_ANA_ANDROID', 'ANDROID');
INSERT INTO dispositivo (usuario_id, token_push, plataforma) VALUES
    ((SELECT id FROM usuario WHERE email = 'bruno.alves@exemplo.com'),
     'TOKEN_EXEMPLO_BRUNO_IOS', 'IOS');

INSERT INTO notificacao (usuario_id, aviso_id, lida) VALUES
    ((SELECT id FROM usuario WHERE email = 'ana.souza@exemplo.com'),
     (SELECT id FROM aviso WHERE titulo = 'Aula de Programação Web cancelada hoje'), TRUE);
INSERT INTO notificacao (usuario_id, aviso_id, lida) VALUES
    ((SELECT id FROM usuario WHERE email = 'bruno.alves@exemplo.com'),
     (SELECT id FROM aviso WHERE titulo = 'Aula de Programação Web cancelada hoje'), FALSE);
INSERT INTO notificacao (usuario_id, aviso_id, lida) VALUES
    ((SELECT id FROM usuario WHERE email = 'ana.souza@exemplo.com'),
     (SELECT id FROM aviso WHERE titulo = 'Banco de Dados mudou para o Laboratório de Informática 1'), FALSE);

-- ---------------------------------------------------------------------
-- [S] confirmacao_aviso
-- ---------------------------------------------------------------------
INSERT INTO confirmacao_aviso (aviso_id, usuario_id) VALUES
    ((SELECT id FROM aviso WHERE titulo = 'Aula de Programação Web cancelada hoje'),
     (SELECT id FROM usuario WHERE email = 'ana.souza@exemplo.com'));
INSERT INTO confirmacao_aviso (aviso_id, usuario_id) VALUES
    ((SELECT id FROM aviso WHERE titulo = 'Aula de Programação Web cancelada hoje'),
     (SELECT id FROM usuario WHERE email = 'bruno.alves@exemplo.com'));

-- ---------------------------------------------------------------------
-- [S] relato_acessibilidade (um aberto e um já resolvido)
-- ---------------------------------------------------------------------
INSERT INTO relato_acessibilidade (espaco_id, usuario_id, tipo, descricao, status) VALUES
    ((SELECT id FROM espaco WHERE nome = 'Elevador do Bloco B'),
     (SELECT id FROM usuario WHERE email = 'camila.rocha@exemplo.com'),
     'ELEVADOR_QUEBRADO', 'O elevador está parado no térreo com a porta aberta.', 'ABERTO');
INSERT INTO relato_acessibilidade (espaco_id, usuario_id, tipo, descricao, status, criado_em, resolvido_em) VALUES
    ((SELECT id FROM espaco WHERE nome = 'Rampa de acesso do Bloco A'),
     (SELECT id FROM usuario WHERE email = 'ana.souza@exemplo.com'),
     'RAMPA_BLOQUEADA', 'Havia bicicletas encostadas na rampa.', 'RESOLVIDO',
     DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -1, CURRENT_TIMESTAMP));

-- ---------------------------------------------------------------------
-- [S] horario_aula (Informática 2º ano A, segunda e terça)
-- dia_semana: 1 = segunda, 2 = terça ...
-- ---------------------------------------------------------------------
INSERT INTO horario_aula (turma_id, espaco_id, professor_id, disciplina, dia_semana, hora_inicio, hora_fim) VALUES
    ((SELECT id FROM turma WHERE nome = 'Informática 2º ano A'),
     (SELECT id FROM espaco WHERE nome = 'Sala 101'),
     (SELECT id FROM usuario WHERE email = 'carlos.mendes@exemplo.com'),
     'Programação Web', 1, '07:30:00', '09:10:00');
INSERT INTO horario_aula (turma_id, espaco_id, professor_id, disciplina, dia_semana, hora_inicio, hora_fim) VALUES
    ((SELECT id FROM turma WHERE nome = 'Informática 2º ano A'),
     (SELECT id FROM espaco WHERE nome = 'Sala 102'),
     (SELECT id FROM usuario WHERE email = 'carlos.mendes@exemplo.com'),
     'Banco de Dados', 1, '09:30:00', '11:10:00');
INSERT INTO horario_aula (turma_id, espaco_id, professor_id, disciplina, dia_semana, hora_inicio, hora_fim) VALUES
    ((SELECT id FROM turma WHERE nome = 'Informática 2º ano A'),
     (SELECT id FROM espaco WHERE nome = 'Laboratório de Informática 1'),
     NULL,
     'Lógica de Programação', 2, '07:30:00', '09:10:00');

-- ---------------------------------------------------------------------
-- [S] cardapio (hoje e amanhã)
-- ---------------------------------------------------------------------
INSERT INTO cardapio (campus_id, data_refeicao, refeicao, descricao) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), CURRENT_DATE, 'ALMOCO',
     'Arroz, feijão, frango grelhado, salada de alface e tomate, fruta');
INSERT INTO cardapio (campus_id, data_refeicao, refeicao, descricao) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), CURRENT_DATE, 'JANTAR',
     'Arroz, feijão, carne moída, purê de batata, salada de repolho');
INSERT INTO cardapio (campus_id, data_refeicao, refeicao, descricao) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'), DATEADD('DAY', 1, CURRENT_DATE), 'ALMOCO',
     'Arroz, feijão, peixe assado, legumes salteados, fruta');

-- ---------------------------------------------------------------------
-- [S] relato_fila
-- ---------------------------------------------------------------------
INSERT INTO relato_fila (campus_id, usuario_id, nivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'),
     (SELECT id FROM usuario WHERE email = 'bruno.alves@exemplo.com'), 'GRANDE');
INSERT INTO relato_fila (campus_id, usuario_id, nivel) VALUES
    ((SELECT id FROM campus WHERE nome = 'Uberaba'),
     (SELECT id FROM usuario WHERE email = 'camila.rocha@exemplo.com'), 'MEDIA');
