package br.com.redefrio.dto;

import br.com.redefrio.domain.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Respostas {
    private Respostas(){ }

    public record InstituicaoView(Long id,String nome,TipoInstituicao tipo,String cidade,String bairro,String contato,boolean ativo){
        public static InstituicaoView de(Instituicao i){
            return new InstituicaoView(i.getId(),i.getNome(),i.getTipo(),i.getCidade(),i.getBairro(),i.getContato(),i.isAtivo());
        }
    }
    public record RefrigeradorView(Long id,String codigo,Long instituicaoId,String instituicaoNome,
                                   BigDecimal temperaturaMinC,BigDecimal temperaturaMaxC,boolean ativo){
        public static RefrigeradorView de(Refrigerador r){
            return new RefrigeradorView(r.getId(),r.getCodigo(),r.getInstituicao().getId(),r.getInstituicao().getNome(),
                    r.getTemperaturaMinC(),r.getTemperaturaMaxC(),r.isAtivo());
        }
    }
    public record LoteView(Long id,String descricao,String categoria,BigDecimal quantidadeKg,
                           LocalDate validade,Long instituicaoOrigemId,SituacaoLote situacao){
        public static LoteView de(LoteAlimento l){
            return new LoteView(l.getId(),l.getDescricao(),l.getCategoria(),l.getQuantidadeKg(),
                    l.getValidade(),l.getInstituicaoOrigem().getId(),l.getSituacao());
        }
    }
    public record DoacaoView(Long id,Long loteId,Long instituicaoDestinoId,BigDecimal quantidadeKg,
                             StatusDoacao status,LocalDateTime criadaEm){
        public static DoacaoView de(Doacao d){
            return new DoacaoView(d.getId(),d.getLote().getId(),d.getInstituicaoDestino().getId(),
                    d.getQuantidadeKg(),d.getStatus(),d.getCriadaEm());
        }
    }
    public record LeituraView(Long id,Long refrigeradorId,BigDecimal temperaturaC,LocalDateTime registradaEm,
                              boolean alertaGerado){
        public static LeituraView de(LeituraTemperatura l,boolean alerta){
            return new LeituraView(l.getId(),l.getRefrigerador().getId(),l.getTemperaturaC(),l.getRegistradaEm(),alerta);
        }
    }
    public record AlertaView(Long id,Long refrigeradorId,Long leituraId,String mensagem,
                             StatusAlerta status,LocalDateTime criadoEm,LocalDateTime resolvidoEm){
        public static AlertaView de(Alerta a){
            return new AlertaView(a.getId(),a.getRefrigerador().getId(),a.getLeitura().getId(),
                    a.getMensagem(),a.getStatus(),a.getCriadoEm(),a.getResolvidoEm());
        }
    }
}
