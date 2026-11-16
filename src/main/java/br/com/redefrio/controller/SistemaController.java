package br.com.redefrio.controller;
import br.com.redefrio.service.RedeFrioService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class SistemaController {
    private final RedeFrioService service;
    public SistemaController(RedeFrioService service){this.service=service;}
    @GetMapping("/api/saude") public Map<String,String> saude(){
        return Map.of("projeto","Rede Frio","status","UP","ambiente","prototipo academico");
    }
    @GetMapping("/api/resumo") public Map<String,Long> resumo(){return service.resumo();}
}
