package br.com.redefrio.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="leitura_temperatura")
public class LeituraTemperatura {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="refrigerador_id",nullable=false)
    private Refrigerador refrigerador;
    @Column(name="temperatura_c",nullable=false,precision=5,scale=2) private BigDecimal temperaturaC;
    @Column(name="registrada_em",nullable=false) private LocalDateTime registradaEm;

    public LeituraTemperatura(){ }
    public LeituraTemperatura(Refrigerador refrigerador,BigDecimal temperaturaC){
        this.refrigerador=refrigerador; this.temperaturaC=temperaturaC;
    }
    @PrePersist public void antesDeSalvar(){if(registradaEm==null) registradaEm=LocalDateTime.now();}
    public Long getId(){return id;}
    public Refrigerador getRefrigerador(){return refrigerador;}
    public BigDecimal getTemperaturaC(){return temperaturaC;}
    public LocalDateTime getRegistradaEm(){return registradaEm;}
}
