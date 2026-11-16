package br.com.redefrio.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="doacao")
public class Doacao {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="lote_id",nullable=false)
    private LoteAlimento lote;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="instituicao_destino_id",nullable=false)
    private Instituicao instituicaoDestino;
    @Column(name="quantidade_kg",nullable=false,precision=10,scale=2) private BigDecimal quantidadeKg;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=25)
    private StatusDoacao status=StatusDoacao.PENDENTE;
    @Column(name="criada_em",nullable=false) private LocalDateTime criadaEm;

    public Doacao() { }
    public Doacao(LoteAlimento lote, Instituicao destino, BigDecimal quantidadeKg){
        this.lote=lote; this.instituicaoDestino=destino; this.quantidadeKg=quantidadeKg;
    }
    @PrePersist public void antesDeSalvar(){if(criadaEm==null) criadaEm=LocalDateTime.now();}
    public void confirmar(){status=StatusDoacao.CONFIRMADA;}
    public void cancelar(){status=StatusDoacao.CANCELADA;}
    public Long getId(){return id;}
    public LoteAlimento getLote(){return lote;}
    public Instituicao getInstituicaoDestino(){return instituicaoDestino;}
    public BigDecimal getQuantidadeKg(){return quantidadeKg;}
    public StatusDoacao getStatus(){return status;}
    public LocalDateTime getCriadaEm(){return criadaEm;}
}
