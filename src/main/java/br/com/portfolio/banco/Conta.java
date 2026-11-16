package br.com.portfolio.banco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Conta {
    private final String numero;
    private final String titular;
    private BigDecimal saldo = BigDecimal.ZERO.setScale(2);
    private final List<Transacao> extrato = new ArrayList<>();

    protected Conta(String numero, String titular) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Numero obrigatorio");
        }
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("Titular obrigatorio");
        }

        this.numero = numero.trim();
        this.titular = titular.trim();
    }

    public final String getNumero() {
        return numero;
    }

    public final String getTitular() {
        return titular;
    }

    public final BigDecimal getSaldo() {
        return saldo;
    }

    public final List<Transacao> getExtrato() {
        return List.copyOf(extrato);
    }

    public final void depositar(BigDecimal valor) {
        BigDecimal quantia = validarValor(valor);
        registrar(TipoTransacao.DEPOSITO, quantia, "Deposito");
    }

    public void sacar(BigDecimal valor) {
        BigDecimal quantia = validarValor(valor);
        debitar(TipoTransacao.SAQUE, quantia, "Saque");
    }

    public final void transferirPara(Conta destino, BigDecimal valor) {
        Objects.requireNonNull(destino, "Conta de destino obrigatoria");

        if (destino == this) {
            throw new IllegalArgumentException("Transferencia para a mesma conta");
        }

        BigDecimal quantia = validarValor(valor);

        debitar(
                TipoTransacao.TRANSFERENCIA_ENVIADA,
                quantia,
                "Transferencia para " + destino.numero
        );

        destino.registrar(
                TipoTransacao.TRANSFERENCIA_RECEBIDA,
                quantia,
                "Transferencia de " + numero
        );
    }

    protected final void debitar(
            TipoTransacao tipo,
            BigDecimal valor,
            String descricao
    ) {
        if (saldo.compareTo(valor) < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }

        registrar(tipo, valor.negate(), descricao);
    }

    protected final void creditar(
            TipoTransacao tipo,
            BigDecimal valor,
            String descricao
    ) {
        registrar(tipo, valor, descricao);
    }

    protected final BigDecimal validarValor(BigDecimal valor) {
        Objects.requireNonNull(valor, "Valor obrigatorio");

        BigDecimal quantia;

        try {
            quantia = valor.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException(
                    "Valor deve ter no maximo duas casas decimais",
                    exception
            );
        }

        if (quantia.signum() <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }

        return quantia;
    }

    private void registrar(
            TipoTransacao tipo,
            BigDecimal valor,
            String descricao
    ) {
        saldo = saldo.add(valor);

        extrato.add(new Transacao(
                LocalDateTime.now(),
                tipo,
                valor,
                saldo,
                descricao
        ));
    }
}