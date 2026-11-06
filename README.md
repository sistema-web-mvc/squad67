IF Agora
Aplicativo que mostra, em tempo real, o que está acontecendo no campus do IFTM: aula cancelada, sala trocada, fila do refeitório, elevador quebrado e mais.

Projeto desenvolvido no workshop de apps para o ambiente do IFTM.

Equipe: Squad 67
O problema
Qual é o problema?
Os alunos do IFTM não sabem, na hora, o que está acontecendo no campus: aula cancelada, sala trocada, fila do refeitório, elevador quebrado ou ônibus atrasado.

Quem sofre com isso?
Estudantes de todos os cursos do IFTM, principalmente:

os que dependem de ônibus;
os que têm pouco intervalo entre as aulas;
os estudantes com deficiência, que são mais prejudicados quando um elevador ou uma rampa está fora de uso.
Também atrapalha professores e servidores, que precisam avisar mudanças a muita gente.

Como se resolve hoje?
Grupos de WhatsApp das turmas
Recados de colegas
Cartazes no mural
E-mail institucional
"Descobrir na hora" ao chegar na sala
As informações ficam espalhadas, chegam atrasadas e se perdem no meio das conversas.

Por que um app resolve?
O celular está sempre com o aluno e permite notificação na hora, filtrada por curso, turma e campus.
Junta tudo em um só lugar e mostra o que é de agora, não o histórico de mensagens.
Os próprios alunos podem relatar o que veem, como a fila do refeitório ou um elevador quebrado.
Uma tela em 30 segundos
Na tela principal, o aluno vê no topo um cartão com a próxima aula, a sala e o horário, com um aviso em destaque se algo mudou. Abaixo, cartões rápidos: Aula vaga ou sala trocada, Salas livres agora, Fila do refeitório, Próximo ônibus e Alertas de acessibilidade. Um botão grande + Relatar permite avisar um problema em dois toques. O app tem fonte ajustável e funciona com leitor de tela.

MVP (método MoSCoW adaptado)
Regra de ouro: os itens M sozinhos devem ser executáveis em 12 semanas.

Categoria	Funcionalidade	Justificativa
M	Login e perfil (campus, curso e turma)	Sem saber quem é o aluno, não dá para filtrar os avisos
M	Publicar aviso (aula cancelada, sala trocada)	É o núcleo do app: a informação precisa entrar para circular
M	Feed de avisos do dia filtrado por turma	Sem a lista de avisos, o app não tem valor
M	Notificação push dos avisos	É o diferencial do "agora": o aluno é avisado sem abrir o app
M	Acessibilidade básica (leitor de tela, contraste e fonte legível)	Custa pouco no início e é difícil de corrigir depois
S	Relatar problema de acessibilidade (elevador, rampa)	Muito útil, mas depende do núcleo de avisos já estar funcionando
S	Tela inicial com próxima aula e sala	Melhora muito, mas exige cadastro dos horários
S	Cardápio e fila do refeitório	Resolve uma dor diária, mas não é vital no início
S	Confirmação de avisos por outros alunos ("confirmo")	Aumenta a confiança nas informações
C	Salas livres agora	Legal, mas depende de dados de ocupação das salas
C	Horário do ônibus e caronas	Útil, mas é um problema separado do núcleo
C	Modo escuro e fonte ajustável pelo usuário	Conforto extra, não prioritário
W	Integração com o sistema acadêmico do IFTM	Exige autorização e acesso a dados da instituição
W	Chat em tempo real entre alunos	Infraestrutura complexa e exige moderação
W	Mapa interno do campus	Exige levantamento detalhado de cada campus
W	Versão web e IA avançada	Fora do escopo do trimestre
Legenda: M = Must Have, S = Should Have, C = Could Have, W = Won't Have.
