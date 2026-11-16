package br.com.redefrio.service;

import br.com.redefrio.domain.*;
import br.com.redefrio.dto.Requisicoes;
import br.com.redefrio.dto.Respostas.*;
import br.com.redefrio.error.RecursoNaoEncontradoException;
import br.com.redefrio.error.RegraNegocioException;
import br.com.redefrio.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class RedeFrioService {
    private final InstituicaoRepository instituicoes;
    private final RefrigeradorRepository refrigeradores;
    private final LoteAlimentoRepository lotes;
    private final DoacaoRepository doacoes;
    private final LeituraTemperaturaRepository leituras;
    private final AlertaRepository alertas;

    public RedeFrioService(InstituicaoRepository instituicoes, RefrigeradorRepository refrigeradores,
                           LoteAlimentoRepository lotes, DoacaoRepository doacoes,
                           LeituraTemperaturaRepository leituras, AlertaRepository alertas){
        this.instituicoes=instituicoes; this.refrigeradores=refrigeradores; this.lotes=lotes;
        this.doacoes=doacoes; this.leituras=leituras; this.alertas=alertas;
    }

    @Transactional(readOnly=true)
    public List<InstituicaoView> listarInstituicoes(){
        return instituicoes.findAll().stream().map(InstituicaoView::de).toList();
    }
    @Transactional
    public InstituicaoView criarInstituicao(Requisicoes.InstituicaoCriar r){
        return InstituicaoView.de(instituicoes.save(new Instituicao(r.nome().trim(),r.tipo(),r.cidade().trim(),
                r.bairro().trim(),r.contato())));
    }
    @Transactional(readOnly=true)
    public List<RefrigeradorView> listarRefrigeradores(){
        return refrigeradores.findAll().stream().map(RefrigeradorView::de).toList();
    }
    @Transactional
    public RefrigeradorView criarRefrigerador(Requisicoes.RefrigeradorCriar r){
        if(r.temperaturaMinC().compareTo(r.temperaturaMaxC())>=0)
            throw new RegraNegocioException("A temperatura mínima deve ser menor que a máxima.");
        if(refrigeradores.existsByCodigo(r.codigo()))
            throw new RegraNegocioException("Já existe um refrigerador com esse código.");
        Instituicao instituicao=instituicao(r.instituicaoId());
        if(!instituicao.isAtivo()) throw new RegraNegocioException("A instituição está inativa.");
        return RefrigeradorView.de(refrigeradores.save(new Refrigerador(r.codigo().trim(),instituicao,
                r.temperaturaMinC(),r.temperaturaMaxC())));
    }
    @Transactional(readOnly=true)
    public List<LoteView> listarLotes(){
        return lotes.findAll().stream().map(LoteView::de).toList();
    }
    @Transactional(readOnly=true)
    public LoteView buscarLote(Long id){
        return LoteView.de(lote(id));
    }
    @Transactional
    public LoteView criarLote(Requisicoes.LoteCriar r){
        Instituicao origem=instituicao(r.instituicaoOrigemId());
        if(!origem.isAtivo() || origem.getTipo()==TipoInstituicao.RECEPTORA)
            throw new RegraNegocioException("A origem deve ser uma instituição doadora ativa.");
        return LoteView.de(lotes.save(new LoteAlimento(r.descricao().trim(),r.categoria().trim(),
                r.quantidadeKg(),r.validade(),origem)));
    }
    @Transactional(readOnly=true)
    public List<DoacaoView> listarDoacoes(){
        return doacoes.findAll().stream().map(DoacaoView::de).toList();
    }
    @Transactional
    public DoacaoView criarDoacao(Requisicoes.DoacaoCriar r){
        // Bloqueio pessimista evita que dois pedidos reservem mais do que o saldo disponível.
        LoteAlimento lote=lotes.buscarComBloqueio(r.loteId()).orElseThrow(() ->
                new RecursoNaoEncontradoException("Lote não encontrado: "+r.loteId()));
        Instituicao destino=instituicao(r.instituicaoDestinoId());
        if(!destino.isAtivo() || destino.getTipo()==TipoInstituicao.DOADORA)
            throw new RegraNegocioException("O destino deve ser uma instituição receptora ativa.");
        if(lote.getInstituicaoOrigem().getId().equals(destino.getId()))
            throw new RegraNegocioException("Origem e destino não podem ser a mesma instituição.");
        if(lote.getValidade().isBefore(LocalDate.now()))
            throw new RegraNegocioException("Lote com validade vencida não pode ser reservado.");
        if(lote.getQuantidadeKg().compareTo(r.quantidadeKg())<0)
            throw new RegraNegocioException("Saldo insuficiente no lote para a reserva.");
        lote.reservar(r.quantidadeKg());
        return DoacaoView.de(doacoes.save(new Doacao(lote,destino,r.quantidadeKg())));
    }
    @Transactional
    public DoacaoView confirmarDoacao(Long id){
        Doacao d=doacaoComBloqueio(id);
        if(d.getStatus()!=StatusDoacao.PENDENTE)
            throw new RegraNegocioException("Só é possível confirmar uma doação pendente.");
        if(d.getLote().getValidade().isBefore(LocalDate.now()))
            throw new RegraNegocioException("Doação pendente de lote vencido requer avaliação humana; confirmação bloqueada.");
        d.confirmar();
        return DoacaoView.de(d);
    }
    @Transactional
    public DoacaoView cancelarDoacao(Long id){
        Doacao d=doacaoComBloqueio(id);
        if(d.getStatus()!=StatusDoacao.PENDENTE)
            throw new RegraNegocioException("Só é possível cancelar uma doação pendente.");
        LoteAlimento lote=lotes.buscarComBloqueio(d.getLote().getId()).orElseThrow(() ->
                new RecursoNaoEncontradoException("Lote da doação não encontrado."));
        lote.devolver(d.getQuantidadeKg());
        d.cancelar();
        return DoacaoView.de(d);
    }
    @Transactional(readOnly=true)
    public List<LeituraView> listarLeituras(){
        return leituras.findAll().stream().map(l -> LeituraView.de(l,false)).toList();
    }
    @Transactional
    public LeituraView registrarLeitura(Requisicoes.LeituraCriar r){
        Refrigerador refrigerador=refrigeradores.findById(r.refrigeradorId()).orElseThrow(() ->
                new RecursoNaoEncontradoException("Refrigerador não encontrado: "+r.refrigeradorId()));
        if(!refrigerador.isAtivo()) throw new RegraNegocioException("Refrigerador inativo.");
        LeituraTemperatura leitura=leituras.save(new LeituraTemperatura(refrigerador,r.temperaturaC()));
        boolean foraDaFaixa=r.temperaturaC().compareTo(refrigerador.getTemperaturaMinC())<0 ||
                r.temperaturaC().compareTo(refrigerador.getTemperaturaMaxC())>0;
        if(foraDaFaixa){
            String msg="Temperatura fora da faixa configurada em "+refrigerador.getCodigo()+
                    ": "+r.temperaturaC().toPlainString()+" °C. Verificação humana necessária.";
            alertas.save(new Alerta(refrigerador,leitura,msg));
        }
        // Alerta indica desvio do sensor; não atesta segurança sanitária do alimento.
        return LeituraView.de(leitura,foraDaFaixa);
    }
    @Transactional(readOnly=true)
    public List<AlertaView> listarAlertas(){
        return alertas.findAll().stream().map(AlertaView::de).toList();
    }
    @Transactional
    public AlertaView resolverAlerta(Long id){
        Alerta alerta=alertas.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Alerta não encontrado: "+id));
        if(alerta.getStatus()==StatusAlerta.RESOLVIDO)
            throw new RegraNegocioException("Alerta já foi resolvido.");
        alerta.resolver();
        return AlertaView.de(alerta);
    }
    @Transactional(readOnly=true)
    public Map<String,Long> resumo(){
        return Map.of("instituicoes",instituicoes.count(),"refrigeradores",refrigeradores.count(),
                "lotes",lotes.count(),"doacoes",doacoes.count(),"leituras",leituras.count(),
                "alertas",alertas.count());
    }

    private Instituicao instituicao(Long id){
        return instituicoes.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Instituição não encontrada: "+id));
    }
    private LoteAlimento lote(Long id){
        return lotes.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Lote não encontrado: "+id));
    }
    private Doacao doacaoComBloqueio(Long id){
        return doacoes.buscarComBloqueio(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Doação não encontrada: "+id));
    }
}
