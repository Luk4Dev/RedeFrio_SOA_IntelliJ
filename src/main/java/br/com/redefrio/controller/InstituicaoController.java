package br.com.redefrio.controller;
import br.com.redefrio.dto.Requisicoes;
import br.com.redefrio.dto.Respostas.InstituicaoView;
import br.com.redefrio.service.RedeFrioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/instituicoes")
public class InstituicaoController {
    private final RedeFrioService service;
    public InstituicaoController(RedeFrioService service){this.service=service;}
    @GetMapping public List<InstituicaoView> listar(){return service.listarInstituicoes();}
    @PostMapping public ResponseEntity<InstituicaoView> criar(@Valid @RequestBody Requisicoes.InstituicaoCriar r){
        InstituicaoView criado=service.criarInstituicao(r);
        return ResponseEntity.created(URI.create("/api/instituicoes/"+criado.id())).body(criado);
    }
}
