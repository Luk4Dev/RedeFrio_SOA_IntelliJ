# Testes manuais no Insomnia — passo a passo

## Preparação

1. Abra e execute `RedeFrioApplication` no IntelliJ.
2. Abra o Insomnia > **Import** > escolha `insomnia/RedeFrio_Insomnia.json`.
3. Confira que o ambiente usa `base_url=http://localhost:8080`.
4. O banco é inicializado com **três instituições fictícias**, um refrigerador `RF-DEMO-001` e um lote de 80 kg.

**Atenção:** cada POST grava dados persistentes no H2. Os IDs de novas doações/alertas mudam conforme você testa. Não presuma que o primeiro registro sempre continua com ID 1 depois de vários testes.

## Casos para registrar em prints

| # | Requisição no Insomnia | Esperado | Evidência sugerida |
|---|---|---|---|
| 1 | `01 - Saúde` | HTTP 200, status UP | API executando |
| 2 | `02 - Instituições` | HTTP 200, lista de instituições | Dados do banco |
| 3 | `03 - Refrigeradores` | HTTP 200, RF-DEMO-001 | Relação com instituição |
| 4 | `04 - Lotes` | HTTP 200, lote demo | Estoque antes da reserva |
| 5 | `05 - Registrar doação (10 kg)` | HTTP 201, PENDENTE | API POST + transação |
| 6 | `06 - Saldo do lote 1` | HTTP 200, quantidade reduzida em 10 kg | Persistência e regra |
| 7 | `07 - Exceder saldo do lote` | HTTP 409 | Regra de negócio |
| 8 | `08 - Leitura fora da faixa (9,5°C)` | HTTP 201, alertaGerado=true | Lógica do sensor |
| 9 | `09 - Alertas` | HTTP 200, alerta ABERTO | Banco e associação |
| 10 | `10 - Leitura normal (3°C)` | HTTP 201, alertaGerado=false | Limite configurado |
| 11 | `11 - Entrada inválida` | HTTP 400 | Bean Validation |
| 12 | `12 - Lote inexistente` | HTTP 404 | Tratamento de erro |
| 13 | `13 - Resumo` | HTTP 200, contagens | Serviço de consulta |

## Exemplo de doação no Insomnia

**POST** `/api/doacoes`, body JSON:

```json
{
  "loteId": 1,
  "instituicaoDestinoId": 2,
  "quantidadeKg": 10.00
}
```

Depois consulte `GET /api/lotes/1`. Antes da primeira reserva: **80.00**. Depois: **70.00**. Se repetir a operação, o saldo reduzirá novamente (é um novo pedido).

A doação retorna um `id` próprio. Para confirmar ou cancelar, substitua `{id}` pelo **ID real retornado**:

- `PATCH /api/doacoes/ID_REAL/confirmar`
- `PATCH /api/doacoes/ID_REAL/cancelar`

Os exemplos da coleção usam o ID **1** apenas como referência para a primeira execução de um banco zerado.

## Evidência das migrações

No H2 Console (`http://localhost:8080/h2-console`), conexão `jdbc:h2:file:./data/redefrio`, usuário `sa`, senha vazia:

```sql
SELECT installed_rank,version,description,success
FROM flyway_schema_history ORDER BY installed_rank;
```

A consulta deve mostrar **V1, V2 e V3** marcadas com sucesso. Tire captura após verificar. Também rode `SELECT * FROM doacao;` e `SELECT * FROM alerta;`.

## Execução dos testes automatizados

Abra a classe `src/test/java/br/com/redefrio/RedeFrioApiTest.java` no IntelliJ e clique em Run. Capture o resultado **real**. A execução depende de Java 21, Maven e download inicial das dependências.

## Problemas comuns

- **Porta 8080 ocupada:** encerre a outra API ou mude `server.port` em `application.properties` e o ambiente no Insomnia.
- **Maven não baixou:** clique no botão de Reload Maven Projects e verifique sua conexão.
- **Credenciais do H2 Console:** use `sa`, senha em branco, e a URL igual à do `application.properties`.
- **Migração quebrada:** não altere scripts `V1`, `V2` ou `V3` depois de executados. Crie `V4__nome.sql` para mudar o esquema. No protótipo local, só se quiser reiniciar os dados, pare a API e exclua `data/redefrio.mv.db` e `data/redefrio.trace.db` antes de iniciar (isso apaga tudo, preserve evidências primeiro).
