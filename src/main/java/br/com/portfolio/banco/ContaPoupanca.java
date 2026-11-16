package br.com.portfolio.banco;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ContaPoupanca extends Conta {
    private static final BigDecimal TAXA_RENDIMENTO =
            new BigDecimal("0.005");

    public ContaPoupanca(String numero, String titular) {
        super(numero, titular);
    }

    public BigDecimal aplicarRendimento() {
        BigDecimal rendimento = getSaldo()
                .multiply(TAXA_RENDIMENTO)
                .setScale(2, RoundingMode.HALF_EVEN);

        if (rendimento.signum() > 0) {
            creditar(
                    TipoTransacao.RENDIMENTO,
                    rendimento,
                    "Rendimento da poupanca"
            );
        }

        return rendimento;
    }
}