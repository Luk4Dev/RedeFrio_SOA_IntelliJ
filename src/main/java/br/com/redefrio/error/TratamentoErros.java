package br.com.redefrio.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice
public class TratamentoErros {
    public record Erro(Instant momento,int status,String erro,String mensagem,Map<String,String> campos){ }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Erro> naoEncontrado(RecursoNaoEncontradoException ex){
        return resposta(HttpStatus.NOT_FOUND,ex.getMessage(),Map.of());
    }
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Erro> regra(RegraNegocioException ex){
        return resposta(HttpStatus.CONFLICT,ex.getMessage(),Map.of());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Erro> validacao(MethodArgumentNotValidException ex){
        Map<String,String> campos = new TreeMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> campos.put(e.getField(),e.getDefaultMessage()));
        return resposta(HttpStatus.BAD_REQUEST,"Dados inválidos. Confira os campos.",campos);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Erro> jsonInvalido(HttpMessageNotReadableException ex){
        return resposta(HttpStatus.BAD_REQUEST,"JSON inválido, campo desconhecido ou valor não permitido.",Map.of());
    }
    private ResponseEntity<Erro> resposta(HttpStatus status,String msg,Map<String,String> campos){
        return ResponseEntity.status(status).body(new Erro(Instant.now(),status.value(),status.getReasonPhrase(),msg,campos));
    }
}
