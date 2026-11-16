package br.com.redefrio.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "instituicao")
public class Instituicao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=120) private String nome;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private TipoInstituicao tipo;
    @Column(nullable=false, length=80) private String cidade;
    @Column(nullable=false, length=100) private String bairro;
    @Column(length=120) private String contato;
    @Column(nullable=false) private boolean ativo = true;

    public Instituicao() { }
    public Instituicao(String nome, TipoInstituicao tipo, String cidade, String bairro, String contato) {
        this.nome = nome; this.tipo = tipo; this.cidade = cidade;
        this.bairro = bairro; this.contato = contato;
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public TipoInstituicao getTipo() { return tipo; }
    public String getCidade() { return cidade; }
    public String getBairro() { return bairro; }
    public String getContato() { return contato; }
    public boolean isAtivo() { return ativo; }
}
