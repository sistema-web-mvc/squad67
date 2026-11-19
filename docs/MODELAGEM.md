# IF Agora: modelagem de dados

Banco: **H2 em memória** (já está no `pom.xml`), acesso por **Spring Data JDBC**.
Scripts: `src/main/resources/schema.sql` (tabelas) e `src/main/resources/data.sql` (dados de exemplo).

![Diagrama ER](diagrama-er.png)

O mesmo diagrama em texto (Mermaid, renderiza no GitHub) está em `diagrama-er.mmd`.

## Tabelas e prioridade no MVP (MoSCoW)

| Tabela | Prioridade | Para que serve |
| --- | --- | --- |
| `campus` | M | Cada unidade do IFTM |
| `curso` | M | Cursos de um campus |
| `turma` | M | Grupo de alunos de um curso em um ano letivo |
| `usuario` | M | Aluno, professor, servidor ou admin (login por e-mail) |
| `espaco` | M | Sala, laboratório, refeitório, elevador, rampa... |
| `aviso` | M | Núcleo do app: aula cancelada, sala trocada, comunicado |
| `dispositivo` | M | Token de push (Firebase) de cada celular |
| `notificacao` | M | Quem foi avisado de quê e se já leu |
| `confirmacao_aviso` | S | "Eu confirmo" de outros alunos |
| `relato_acessibilidade` | S | Elevador quebrado, rampa bloqueada |
| `horario_aula` | S | Tela "próxima aula e sala" |
| `cardapio` | S | Cardápio do refeitório por dia |
| `relato_fila` | S | Fila do refeitório relatada pelos alunos |

As tabelas **M** cobrem os cinco itens Must Have do MVP (login, publicar aviso, feed, push, acessibilidade básica do app). As **S** já estão no banco para não precisar refazer o schema depois, mas só entram no app se sobrar tempo. Itens **C** e **W** (ônibus, caronas, chat, mapa interno) ficaram de fora de propósito.

## Decisões de modelagem

- **`aviso.turma_id` opcional**: com turma, é só para aquela turma; sem turma, vale para o campus inteiro. Feed da turma T do campus C:
  `WHERE ativo AND campus_id = C AND (turma_id IS NULL OR turma_id = T) AND (expira_em IS NULL OR expira_em > CURRENT_TIMESTAMP)`
- **`espaco` único** para salas e para elevador/rampa: o mesmo cadastro serve para "sala trocada" e para os relatos de acessibilidade.
- **Regras no próprio banco** (`CHECK`): perfil e tipos válidos, só aluno tem turma, aviso não expira antes de ser criado, relato `RESOLVIDO` exige `resolvido_em`, `hora_fim > hora_inicio`, dia da semana de 1 a 7.
- **Sem duplicados**: e-mail, token de push, confirmação (um voto por usuário por aviso) e notificação (uma por usuário por aviso) são `UNIQUE`.
- **Exclusão em cascata**: apagar um `aviso` apaga também as linhas de `confirmacao_aviso` e `notificacao` dele (`ON DELETE CASCADE`), porque elas não fazem sentido sem o aviso. Assim, o CRUD exclui com um único `DELETE`.
- **Senha**: a coluna é `senha_hash`. Guarde sempre o hash BCrypt, nunca a senha. Todos os usuários de exemplo usam a senha `senha123`.
- **LGPD**: não há campo de deficiência ou laudo. O app de acessibilidade do MVP trata de **barreiras do espaço**, não de dados de saúde de pessoas.

## Como o `data.sql` foi escrito

- **Nenhum id é digitado à mão.** Os ids nascem do identity e as relações usam `(SELECT id FROM ... WHERE chave_natural)`. Assim, os registros que o app criar depois não colidem com o seed.
- Datas relativas a "agora" (`DATEADD` do H2): a demo sempre tem avisos do dia e cardápio de hoje e amanhã.
- Pessoas, e-mails e tokens são **fictícios**. Cursos, turmas e salas são ilustrativos.

## Como usar

1. Copie `schema.sql`, `data.sql` e `application.properties` para `src/main/resources/`.
2. Rode `./mvnw spring-boot:run`. Os scripts executam sozinhos.
3. Console do banco: <http://localhost:8080/h2-console> com URL `jdbc:h2:mem:ifagora`, usuário `sa`, senha vazia.

O `application.properties` define `spring.sql.init.encoding=UTF-8`. Sem isso, no Windows os acentos do `data.sql` podem sair quebrados.

## Cuidados ao criar as classes (Spring Data JDBC)

- Nomes seguem o padrão: classe `Aviso` ↔ tabela `aviso`; campo `criadoEm` ↔ coluna `criado_em`.
- Para referenciar outra tabela, use um campo `Long` (ex.: `Long campusId`) ou `AggregateReference<Campus, Long>`.
- Se mapear uma coleção filha dentro do agregado (ex.: `Set<ConfirmacaoAviso>` em `Aviso`), o Spring procura por padrão a coluna `aviso`, e não `aviso_id`. Use `@MappedCollection(idColumn = "aviso_id")`.
- Enums (`perfil`, `tipo`, `status`) são gravados como texto, e os valores dos enums Java devem ser iguais aos do `CHECK` (ex.: `AULA_CANCELADA`).
