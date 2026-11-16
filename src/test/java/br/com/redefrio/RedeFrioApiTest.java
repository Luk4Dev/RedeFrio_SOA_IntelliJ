package br.com.redefrio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RedeFrioApiTest {
    @Autowired private MockMvc mvc;

    @Test void listaInstituicoesSimuladas() throws Exception {
        mvc.perform(get("/api/instituicoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].nome",not(emptyOrNullString())));
    }
    @Test void podeConsultarMigracoesESaldoInicial() throws Exception {
        mvc.perform(get("/api/lotes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidadeKg",is(80.0)))
                .andExpect(jsonPath("$.situacao",is("DISPONIVEL")));
    }
    @Test void leituraForaDaFaixaGeraAlerta() throws Exception {
        mvc.perform(post("/api/leituras").contentType(MediaType.APPLICATION_JSON)
                .content("{\"refrigeradorId\":1,\"temperaturaC\":9.5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alertaGerado",is(true)));
        mvc.perform(get("/api/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(1))));
    }
    @Test void leituraNormalNaoGeraAlerta() throws Exception {
        mvc.perform(post("/api/leituras").contentType(MediaType.APPLICATION_JSON)
                .content("{\"refrigeradorId\":1,\"temperaturaC\":3.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alertaGerado",is(false)));
    }
    @Test void reservaDoacaoDescontaSaldo() throws Exception {
        mvc.perform(post("/api/doacoes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"loteId\":1,\"instituicaoDestinoId\":2,\"quantidadeKg\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status",is("PENDENTE")));
        mvc.perform(get("/api/lotes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidadeKg",is(70.0)));
    }
    @Test void quantidadeAcimaDoSaldoRetornaConflito() throws Exception {
        mvc.perform(post("/api/doacoes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"loteId\":1,\"instituicaoDestinoId\":2,\"quantidadeKg\":500}"))
                .andExpect(status().isConflict());
    }
    @Test void entradaInvalidaRetorna400() throws Exception {
        mvc.perform(post("/api/leituras").contentType(MediaType.APPLICATION_JSON)
                .content("{\"refrigeradorId\":1,\"temperaturaC\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.temperaturaC",not(emptyOrNullString())));
    }
    @Test void loteInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/lotes/999999"))
                .andExpect(status().isNotFound());
    }
}
