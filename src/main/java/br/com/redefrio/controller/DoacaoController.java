package br.com.redefrio.controller;
import br.com.redefrio.dto.Requisicoes;
import br.com.redefrio.dto.Respostas.DoacaoView;
import br.com.redefrio.service.RedeFrioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/doacoes")
public class DoacaoController {
    private final RedeFrioService service;
    public DoacaoController(RedeFrioService service){this.service=service;}
    @GetMapping public List<DoacaoView> listar(){return service.listarDoacoes();}
    @PostMapping public ResponseEntity<DoacaoView> criar(@Valid @RequestBody Requisicoes.DoacaoCriar r){
        DoacaoView criada=service.criarDoacao(r);
        return ResponseEntity.created(URI.create("/api/doacoes/"+criada.id())).body(criada);
    }
    @PatchMapping("/{id}/confirmar") public DoacaoView confirmar(@PathVariable Long id){return service.confirmarDoacao(id);}
    @PatchMapping("/{id}/cancelar") public DoacaoView cancelar(@PathVariable Long id){return service.cancelarDoacao(id);}
}
