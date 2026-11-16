package br.com.portfolio.banco;

import java.math.BigDecimal;

public final class ContaCorrente extends Conta {
    private static final BigDecimal TARIFA_SAQUE =
            new BigDecimal("2.50");

    public ContaCorrente(String numero, String titular) {
        super(numero, titular);
    }

    @Override
    public void sacar(BigDecimal valor) {
        BigDecimal quantia = validarValor(valor);
        BigDecimal total = quantia.add(TARIFA_SAQUE);

        if (getSaldo().compareTo(total) < 0) {
            throw new IllegalStateException(
                    "Saldo insuficiente para saque e tarifa"
            );
        }

        debitar(TipoTransacao.SAQUE, quantia, "Saque conta corrente");
        debitar(TipoTransacao.TARIFA, TARIFA_SAQUE, "Tarifa de saque");
    }

    public BigDecimal getTarifaSaque() {
        return TARIFA_SAQUE;
    }
}