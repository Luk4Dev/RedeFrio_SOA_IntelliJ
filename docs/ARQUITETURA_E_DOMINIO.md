# Decisões técnicas — Rede Frio SOA

## Problema e solução

Parte dos alimentos ainda aproveitáveis deixa de ser distribuída por falhas de coordenação, visibilidade de estoque e cadeia fria. A Rede Frio fornece um backend para registrar instituições, refrigeradores, lotes e movimentações de doações, além de leituras de sensores com alertas. O protótipo oferece ferramentas a operadores sociais; não faz decisão sanitária autônoma.

## Regras essenciais

- Instituições podem ser `DOADORA`, `RECEPTORA` ou `AMBAS`.
- Lotes são cadastrados por instituição com função doadora e validade não passada.
- A reserva de doação é **transacional** e abate a quantidade disponível, impedindo reservas duplicadas acima do saldo por bloqueio pessimista do lote.
- O cancelamento de uma reserva pendente repõe a quantidade; confirmação transforma a reserva em movimento confirmado.
- Registro de leitura fora dos limites configurados cria `Alerta` aberto, sem liberar ou reprovar alimentos automaticamente.
- O operador pode marcar um alerta como resolvido após atendimento humano.
- Requisições inválidas têm código 400; ausências 404; conflitos de negócio 409.

## Modelo relacional

```text
Instituicao 1 -------- N Refrigerador
Instituicao 1 -------- N LoteAlimento (origem)
LoteAlimento 1 ------- N Doacao
Instituicao 1 -------- N Doacao (destino)
Refrigerador 1 ------- N LeituraTemperatura
LeituraTemperatura 1 - 0..1 Alerta
Refrigerador 1 ------- N Alerta
```

`Doacao` vincula a organização que recebe ao lote de origem. A quantidade física restante do lote é diferenciada do volume já reservado. Um registro no banco não prova execução física da coleta: a confirmação é uma ação explícita do operador.

## Camadas

| Camada | Pacote | Responsabilidade |
|---|---|---|
| Entrada | `controller/` | REST, validação `@Valid`, status HTTP |
| Aplicação | `service/` | Transações, domínio e verificação de regras |
| Persistência | `repository/` | Repositórios Spring Data JPA |
| Dados | `domain/` + `db/migration/` | Entidades mapeadas e evolução de esquema com Flyway |
| Contratos | `dto/` | Requests e responses estáveis |
| Erros | `error/` | Tratamento uniforme de exceções |

## Integração futura com o ecossistema Rede Frio

- **Physical Computing:** ESP32 pode enviar medições de temperatura a `POST /api/leituras` por um gateway; ainda não há integração real no projeto de SOA.
- **Mobile:** o app Expo pode listar lotes e registrar solicitações de doação. A autenticação precisaria ser adicionada para uso real.
- **Machine Learning:** análises podem consumir dados agregados de estoque e doações, sob critérios de minimização de dados.
- **Operating Systems:** uma evolução pode hospedar a aplicação ou fachada web em IIS no Windows Server; este repositório atualmente executa via servidor Java embutido.

## Decisões conscientes

- **H2 em arquivo:** inicialização simples no IntelliJ e demonstração inequívoca de conexão/persistência; não é escolha final para operações de produção.
- **Flyway com três versões:** histórico auditável de criação, carga inicial fictícia e índices. O JPA usa `validate`, não `create`.
- **DTOs separados das entidades:** evita expor detalhes de persistência e referências cíclicas ao serializar respostas.
- **Sem dados de cidadãos:** dados simulados evitam tratar informações pessoais reais na atividade acadêmica.
- **Sem aprovação automática de alimentos:** temperatura do sensor sozinha não é laudo sanitário, e definição da faixa é uma configuração de demonstração, não norma sanitária universal.
