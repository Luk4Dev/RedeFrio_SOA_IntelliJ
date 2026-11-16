package br.com.redefrio.controller;
import br.com.redefrio.dto.Requisicoes;
import br.com.redefrio.dto.Respostas.LoteView;
import br.com.redefrio.service.RedeFrioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/lotes")
public class LoteController {
    private final RedeFrioService service;
    public LoteController(RedeFrioService service){this.service=service;}
    @GetMapping public List<LoteView> listar(){return service.listarLotes();}
    @GetMapping("/{id}") public LoteView buscar(@PathVariable Long id){return service.buscarLote(id);}
    @PostMapping public ResponseEntity<LoteView> criar(@Valid @RequestBody Requisicoes.LoteCriar r){
        LoteView criado=service.criarLote(r);
        return ResponseEntity.created(URI.create("/api/lotes/"+criado.id())).body(criado);
    }
}
