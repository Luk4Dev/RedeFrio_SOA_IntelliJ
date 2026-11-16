package br.com.redefrio.controller;
import br.com.redefrio.dto.Requisicoes;
import br.com.redefrio.dto.Respostas.RefrigeradorView;
import br.com.redefrio.service.RedeFrioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/refrigeradores")
public class RefrigeradorController {
    private final RedeFrioService service;
    public RefrigeradorController(RedeFrioService service){this.service=service;}
    @GetMapping public List<RefrigeradorView> listar(){return service.listarRefrigeradores();}
    @PostMapping public ResponseEntity<RefrigeradorView> criar(@Valid @RequestBody Requisicoes.RefrigeradorCriar r){
        RefrigeradorView criado=service.criarRefrigerador(r);
        return ResponseEntity.created(URI.create("/api/refrigeradores/"+criado.id())).body(criado);
    }
}
