-- Flyway V3: indices para consultas e rastreabilidade.
CREATE INDEX idx_refrigerador_instituicao ON refrigerador(instituicao_id);
CREATE INDEX idx_lote_origem_validade ON lote_alimento(instituicao_origem_id,validade);
CREATE INDEX idx_doacao_lote_status ON doacao(lote_id,status);
CREATE INDEX idx_leitura_refrigerador_data ON leitura_temperatura(refrigerador_id,registrada_em);
CREATE INDEX idx_alerta_status_data ON alerta(status,criado_em);
