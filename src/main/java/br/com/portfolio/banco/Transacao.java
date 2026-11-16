package br.com.portfolio.banco;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record Transacao(
        LocalDateTime data,
        TipoTransacao tipo,
        BigDecimal valor,
        BigDecimal saldoApos,
        String descricao
) {
    public Transacao {
        Objects.requireNonNull(data, "Data obrigatoria");
        Objects.requireNonNull(tipo, "Tipo obrigatorio");
        Objects.requireNonNull(valor, "Valor obrigatorio");
        Objects.requireNonNull(saldoApos, "Saldo obrigatorio");
        Objects.requireNonNull(descricao, "Descricao obrigatoria");
    }
}