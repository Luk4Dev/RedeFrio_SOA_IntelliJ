package br.com.redefrio.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="alerta")
public class Alerta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="refrigerador_id",nullable=false)
    private Refrigerador refrigerador;
    @OneToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="leitura_id",nullable=false,unique=true)
    private LeituraTemperatura leitura;
    @Column(nullable=false,length=250) private String mensagem;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20)
    private StatusAlerta status=StatusAlerta.ABERTO;
    @Column(name="criado_em",nullable=false) private LocalDateTime criadoEm;
    @Column(name="resolvido_em") private LocalDateTime resolvidoEm;

    public Alerta(){ }
    public Alerta(Refrigerador refrigerador,LeituraTemperatura leitura,String mensagem){
        this.refrigerador=refrigerador; this.leitura=leitura; this.mensagem=mensagem;
    }
    @PrePersist public void antesDeSalvar(){if(criadoEm==null) criadoEm=LocalDateTime.now();}
    public void resolver(){status=StatusAlerta.RESOLVIDO;resolvidoEm=LocalDateTime.now();}
    public Long getId(){return id;}
    public Refrigerador getRefrigerador(){return refrigerador;}
    public LeituraTemperatura getLeitura(){return leitura;}
    public String getMensagem(){return mensagem;}
    public StatusAlerta getStatus(){return status;}
    public LocalDateTime getCriadoEm(){return criadoEm;}
    public LocalDateTime getResolvidoEm(){return resolvidoEm;}
}
