package br.com.redefrio.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lote_alimento")
public class LoteAlimento {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=160) private String descricao;
    @Column(nullable=false, length=80) private String categoria;
    @Column(name="quantidade_kg",nullable=false,precision=10,scale=2) private BigDecimal quantidadeKg;
    @Column(nullable=false) private LocalDate validade;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="instituicao_origem_id",nullable=false)
    private Instituicao instituicaoOrigem;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=25)
    private SituacaoLote situacao = SituacaoLote.DISPONIVEL;
    @Column(name="criado_em",nullable=false) private LocalDateTime criadoEm;

    public LoteAlimento() { }
    public LoteAlimento(String descricao,String categoria,BigDecimal quantidadeKg,LocalDate validade,Instituicao origem){
        this.descricao=descricao; this.categoria=categoria; this.quantidadeKg=quantidadeKg;
        this.validade=validade; this.instituicaoOrigem=origem;
    }
    @PrePersist public void antesDeSalvar(){if(criadoEm==null) criadoEm=LocalDateTime.now();}
    public void reservar(BigDecimal quantidade){
        quantidadeKg=quantidadeKg.subtract(quantidade);
        if(quantidadeKg.signum()==0) situacao=SituacaoLote.ESGOTADO;
    }
    public void devolver(BigDecimal quantidade){
        quantidadeKg=quantidadeKg.add(quantidade);
        situacao=SituacaoLote.DISPONIVEL;
    }
    public Long getId(){return id;}
    public String getDescricao(){return descricao;}
    public String getCategoria(){return categoria;}
    public BigDecimal getQuantidadeKg(){return quantidadeKg;}
    public LocalDate getValidade(){return validade;}
    public Instituicao getInstituicaoOrigem(){return instituicaoOrigem;}
    public SituacaoLote getSituacao(){return situacao;}
    public LocalDateTime getCriadoEm(){return criadoEm;}
}
