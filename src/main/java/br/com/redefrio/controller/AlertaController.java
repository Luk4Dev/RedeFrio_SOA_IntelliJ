package br.com.redefrio.controller;
import br.com.redefrio.dto.Respostas.AlertaView;
import br.com.redefrio.service.RedeFrioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/alertas")
public class AlertaController {
    private final RedeFrioService service;
    public AlertaController(RedeFrioService service){this.service=service;}
    @GetMapping public List<AlertaView> listar(){return service.listarAlertas();}
    @PatchMapping("/{id}/resolver") public AlertaView resolver(@PathVariable Long id){return service.resolverAlerta(id);}
}
