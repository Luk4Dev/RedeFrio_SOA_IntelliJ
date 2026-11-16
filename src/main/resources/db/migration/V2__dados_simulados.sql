-- Flyway V2: exemplos FICTICIOS exclusivamente para demonstracao academica.
-- NAO sao estabelecimentos, parceiros ou contatos reais da Rede Frio.
INSERT INTO instituicao (nome,tipo,cidade,bairro,contato,ativo)
VALUES ('Mercado Exemplo Rede Frio','DOADORA','São Paulo','Centro',NULL,TRUE);
INSERT INTO instituicao (nome,tipo,cidade,bairro,contato,ativo)
VALUES ('Cozinha Comunitária Exemplo','RECEPTORA','São Paulo','Centro',NULL,TRUE);
INSERT INTO instituicao (nome,tipo,cidade,bairro,contato,ativo)
VALUES ('Banco de Alimentos Exemplo','AMBAS','São Paulo','Centro',NULL,TRUE);

INSERT INTO refrigerador (codigo,instituicao_id,temp_min_c,temp_max_c,ativo)
VALUES ('RF-DEMO-001',2,1.00,5.00,TRUE);

INSERT INTO lote_alimento (descricao,categoria,quantidade_kg,validade,instituicao_origem_id,situacao,criado_em)
VALUES ('Hortifruti refrigerado de demonstração','Hortifruti',80.00,DATE '2027-12-31',1,'DISPONIVEL',CURRENT_TIMESTAMP);
