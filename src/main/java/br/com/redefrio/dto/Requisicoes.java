package br.com.redefrio.dto;

import br.com.redefrio.domain.TipoInstituicao;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class Requisicoes {
    private Requisicoes() { }

    public record InstituicaoCriar(
            @NotBlank @Size(max=120) String nome,
            @NotNull TipoInstituicao tipo,
            @NotBlank @Size(max=80) String cidade,
            @NotBlank @Size(max=100) String bairro,
            @Size(max=120) String contato) { }

    public record RefrigeradorCriar(
            @NotBlank @Size(max=40) String codigo,
            @NotNull @Positive Long instituicaoId,
            @NotNull @DecimalMin("-40.00") @DecimalMax("50.00") BigDecimal temperaturaMinC,
            @NotNull @DecimalMin("-40.00") @DecimalMax("50.00") BigDecimal temperaturaMaxC) { }

    public record LoteCriar(
            @NotBlank @Size(max=160) String descricao,
            @NotBlank @Size(max=80) String categoria,
            @NotNull @DecimalMin("0.01") BigDecimal quantidadeKg,
            @NotNull @FutureOrPresent LocalDate validade,
            @NotNull @Positive Long instituicaoOrigemId) { }

    public record DoacaoCriar(
            @NotNull @Positive Long loteId,
            @NotNull @Positive Long instituicaoDestinoId,
            @NotNull @DecimalMin("0.01") BigDecimal quantidadeKg) { }

    public record LeituraCriar(
            @NotNull @Positive Long refrigeradorId,
            @NotNull @DecimalMin("-50.00") @DecimalMax("90.00") BigDecimal temperaturaC) { }
}
