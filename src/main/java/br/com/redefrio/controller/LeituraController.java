package br.com.redefrio.controller;
import br.com.redefrio.dto.Requisicoes;
import br.com.redefrio.dto.Respostas.LeituraView;
import br.com.redefrio.service.RedeFrioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/leituras")
public class LeituraController {
    private final RedeFrioService service;
    public LeituraController(RedeFrioService service){this.service=service;}
    @GetMapping public List<LeituraView> listar(){return service.listarLeituras();}
    @PostMapping public ResponseEntity<LeituraView> registrar(@Valid @RequestBody Requisicoes.LeituraCriar r){
        LeituraView criada=service.registrarLeitura(r);
        return ResponseEntity.created(URI.create("/api/leituras/"+criada.id())).body(criada);
    }
}
