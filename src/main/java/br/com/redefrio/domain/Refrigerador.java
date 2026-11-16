package br.com.redefrio.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "refrigerador")
public class Refrigerador {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=40) private String codigo;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="instituicao_id", nullable=false)
    private Instituicao instituicao;
    @Column(name="temp_min_c", nullable=false, precision=5, scale=2) private BigDecimal temperaturaMinC;
    @Column(name="temp_max_c", nullable=false, precision=5, scale=2) private BigDecimal temperaturaMaxC;
    @Column(nullable=false) private boolean ativo = true;

    public Refrigerador() { }
    public Refrigerador(String codigo, Instituicao instituicao, BigDecimal minima, BigDecimal maxima) {
        this.codigo=codigo; this.instituicao=instituicao;
        this.temperaturaMinC=minima; this.temperaturaMaxC=maxima;
    }
    public Long getId(){return id;}
    public String getCodigo(){return codigo;}
    public Instituicao getInstituicao(){return instituicao;}
    public BigDecimal getTemperaturaMinC(){return temperaturaMinC;}
    public BigDecimal getTemperaturaMaxC(){return temperaturaMaxC;}
    public boolean isAtivo(){return ativo;}
}
