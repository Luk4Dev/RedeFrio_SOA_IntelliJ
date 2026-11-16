# Rede Frio — API REST (SOA & Web Services)

**FIAP · Global Solution CitizenTech 2026.2 · Engenharia de Software · Turma 3ESPA**

**Equipe**
- Diego Garcia Tosta — RM 556724
- Joud Jihad Jaber — RM 556482
- Lucca Pereira — RM 560731

## 1. Qual problema resolvemos?

A Rede Frio é uma solução CitizenTech para apoiar a redução do desperdício de alimentos aptos à doação por meio de uma rede de instituições, doadores e refrigeradores monitorados. A plataforma registra lotes e reservas de doação, acompanha temperaturas e gera alertas para avaliação por responsáveis humanos. A proposta prioriza o uso de refrigeradores existentes em entidades parceiras.

**Pessoas beneficiadas:** gestores de organizações sociais e bancos de alimentos, profissionais que organizam doações e, indiretamente, famílias atendidas. As instituições e dados incluídos no sistema são **100% simulados**: não há parceiros reais cadastrados neste protótipo.

**Importante:** um alerta de temperatura não determina, sozinho, se um alimento está apto ao consumo. A avaliação sanitária, coleta, transporte, conservação e distribuição dependem de responsáveis qualificados.

## 2. Tecnologias

- Java **21**
- Spring Boot **3.5.16** (API REST, Spring Web, Spring Data JPA, Bean Validation)
- **H2** em arquivo como banco de dados persistente (não exige instalar servidor de banco)
- **Flyway** para migrações versionadas (`V1`, `V2`, `V3`)
- JUnit 5 + Spring MockMvc para testes automatizados
- **Insomnia** para testes HTTP manuais; coleção pronta em `insomnia/`
- Maven para dependências e build

> Spring Boot 3.5 foi escolhido para manter o protótipo didático com as APIs tradicionais, não como proposta de implantação pública sem autenticação e endurecimento.

## 3. Como abrir no IntelliJ IDEA (passo a passo)

1. Extraia o ZIP. Selecione **File > Open** no IntelliJ e abra a pasta `RedeFrio_SOA_IntelliJ` (ou o arquivo `pom.xml`, escolhendo importar como projeto Maven).
2. Confirme que o **Project SDK é Java 21** em **File > Project Structure > Project**. O IntelliJ precisa de conexão com a internet na primeira abertura para baixar as bibliotecas do Maven.
3. Espere o Maven finalizar o carregamento (ícone de sincronização). Não é necessário instalar H2 separadamente.
4. Abra `src/main/java/br/com/redefrio/RedeFrioApplication.java` e clique no **triângulo verde** ao lado de `main` > **Run**.
5. Quando o console mostrar a inicialização, abra no navegador: **http://localhost:8080/api/saude**. Deve aparecer um JSON com `"status":"UP"`.
6. Teste **http://localhost:8080/api/instituicoes**. Três instituições simuladas já aparecem, criadas pela migração `V2`.
7. Importe a coleção `insomnia/RedeFrio_Insomnia.json` no **Insomnia** e execute as requisições. O roteiro está em `docs/ROTEIRO_INSOMNIA.md`.

**Se preferir o terminal** (com Java 21 e Maven instalados):

```bash
mvn spring-boot:run
```

O aplicativo responde na porta **8080**. Para encerrar, pare o processo pelo IntelliJ.

## 4. Banco de dados e migrações (rubrica: 3,0 + 2,5)

O H2 salva os dados no arquivo local `data/redefrio.mv.db`; o banco é criado no primeiro uso e **persiste ao reiniciar**. A pasta `data/` existe no ZIP, mas os arquivos de banco ficam fora do Git graças ao `.gitignore`.

As migrações versionadas ficam em `src/main/resources/db/migration/`:

| Versão | Responsabilidade |
|---|---|
| `V1__criar_estrutura.sql` | Cria 6 tabelas, chaves estrangeiras, verificações de integridade e estados permitidos |
| `V2__dados_simulados.sql` | Insere 3 instituições, 1 refrigerador e 1 lote **fictícios** |
| `V3__criar_indices.sql` | Adiciona índices para consultas, leituras e alertas |

`spring.jpa.hibernate.ddl-auto=validate` garante que o Hibernate **não substitui** o Flyway na criação das tabelas; ele verifica o esquema resultante. O histórico das migrações fica em `flyway_schema_history`.

**Para conferir o banco localmente:** acesse `http://localhost:8080/h2-console`, informe:

- JDBC URL: `jdbc:h2:file:./data/redefrio`
- User Name: `sa`
- Password: **deixe em branco**

Exemplo de SQL para mostrar evidência de migração:

```sql
SELECT installed_rank, version, description, success
FROM flyway_schema_history
ORDER BY installed_rank;

SELECT id, nome, tipo FROM instituicao;
SELECT id, quantidade_kg, situacao FROM lote_alimento;
```

O console H2 e a autenticação simplificada são **somente para desenvolvimento local**.

## 5. API REST e estruturação do domínio (rubrica: 3,0)

| Método | Endpoint | O que faz |
|---|---|---|
| GET | `/api/saude` | Verifica execução da API |
| GET | `/api/resumo` | Contagem dos registros |
| GET / POST | `/api/instituicoes` | Consulta/cadastra instituição |
| GET / POST | `/api/refrigeradores` | Consulta/cadastra refrigerador |
| GET / POST | `/api/lotes` | Consulta/cadastra lote |
| GET | `/api/lotes/{id}` | Consulta o saldo de um lote |
| GET / POST | `/api/doacoes` | Consulta/reserva doação |
| PATCH | `/api/doacoes/{id}/confirmar` | Confirma doação pendente |
| PATCH | `/api/doacoes/{id}/cancelar` | Cancela reserva e devolve saldo |
| GET / POST | `/api/leituras` | Consulta/registra leitura do sensor |
| GET | `/api/alertas` | Lista alertas de temperatura |
| PATCH | `/api/alertas/{id}/resolver` | Registra resolução humana do alerta |

Respostas de criação usam HTTP **201**; entradas inválidas **400**, registro inexistente **404** e conflito de regra de negócio **409**.

### Exemplo — registrar leitura fora da faixa

`POST http://localhost:8080/api/leituras`

```json
{
  "refrigeradorId": 1,
  "temperaturaC": 9.50
}
```

O refrigerador fictício `RF-DEMO-001` aceita **1°C a 5°C**; a leitura 9,5°C grava a medição e gera um alerta para verificação humana. Confira com `GET /api/alertas`.

### Exemplo — reservar doação

`POST http://localhost:8080/api/doacoes`

```json
{
  "loteId": 1,
  "instituicaoDestinoId": 2,
  "quantidadeKg": 10.00
}
```

O lote demo começa com **80 kg**. Após a reserva, o saldo disponível cai para **70 kg** e a doação fica `PENDENTE`. Ao cancelar, o saldo é devolvido; ao confirmar, a reserva permanece consumida. Não é permitido reservar mais do que o disponível nem registrar doação com lote vencido.

## 6. Organização

```text
src/main/java/br/com/redefrio/
  controller/      Endpoints HTTP e códigos de resposta
  service/         Regras de negócio e transações
  domain/          Entidades JPA e estados de negócio
  repository/      Acesso a dados Spring Data JPA
  dto/             Objetos de entrada e saída (sem expor entidades)
  error/           Respostas padronizadas de erros
src/main/resources/db/migration/    SQLs versionados do Flyway
src/test/         Testes automatizados de integração
insomnia/         Coleção importável para testes manuais
docs/             Arquitetura, roteiro de evidências e guia GitHub
IA/               Registro de prompts do processo de desenvolvimento
```

Fluxo: `HTTP → Controller → Service → Repository → H2`. O serviço registra operações em transações e usa bloqueio pessimista para evitar reservas concorrentes acima do estoque.

## 7. Como rodar os testes

Pelo IntelliJ, clique com o botão direito em `src/test/java` > **Run All Tests**; ou use:

```bash
mvn clean test
```

Os testes verificam: dados das migrações, saldo, doação, regras de negócio, alertas, erros 400 e 404. O GitHub Actions (`.github/workflows/testes.yml`) executa `mvn clean verify` a cada envio ao GitHub. **Confirme a execução com o indicador verde no GitHub antes de entregar.**

## 8. Evidências e entrega

O professor pede **link do repositório GitHub, README e evidências de funcionamento**. Após executar os passos acima, faça capturas de tela **reais** mostrando:

1. Inicialização da API e status `UP`.
2. Resultado de `GET /api/instituicoes` e `GET /api/lotes/1` no Insomnia.
3. `POST /api/doacoes` retornando **201** e atualização do saldo.
4. `POST /api/leituras` fora da faixa e o alerta gerado.
5. Tabela `flyway_schema_history` no H2 mostrando `V1`, `V2`, `V3`.
6. Execução dos testes automatizados no IntelliJ ou na aba **Actions** no GitHub.

Salve os prints na pasta `evidencias/` e faça **Commit + Push** antes de copiar o link do repositório na tarefa de SOA do Teams. Há roteiro completo em `docs/ROTEIRO_INSOMNIA.md`.

## 9. Limites e continuidade

Este é um **protótipo educacional local**, com dados simulados. A API **não tem autenticação**, HTTPS ou autorização por papéis; não deve ser publicada abertamente na internet. Um cenário de produção exigiria PostgreSQL, gestão de segredos, contas e permissões, logs auditáveis, monitoramento, critérios sanitários acordados e consentimento para tratamento de dados. A integração com Mobile, IoT e ML é **prevista**, não apresentada como já implementada neste repositório.
