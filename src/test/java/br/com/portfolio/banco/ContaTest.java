package br.com.portfolio.banco;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ContaTest {

    private Conta novaConta() {
        return new ContaPoupanca("001", "Ana");
    }

    @Test
    void iniciaComSaldoZero() {
        assertEquals(new BigDecimal("0.00"), novaConta().getSaldo());
    }

    @Test
    void normalizaNumeroETitular() {
        Conta conta = new ContaPoupanca(" 001 ", " Ana ");
        assertEquals("001", conta.getNumero());
        assertEquals("Ana", conta.getTitular());
    }

    @Test
    void rejeitaNumeroNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ContaPoupanca(null, "Ana"));
    }

    @Test
    void rejeitaNumeroEmBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> new ContaPoupanca(" ", "Ana"));
    }

    @Test
    void rejeitaTitularNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ContaPoupanca("001", null));
    }

    @Test
    void rejeitaTitularEmBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> new ContaPoupanca("001", " "));
    }

    @Test
    void depositaERegistraExtrato() {
        Conta conta = novaConta();
        conta.depositar(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
        assertEquals(1, conta.getExtrato().size());
        assertEquals(TipoTransacao.DEPOSITO,
                conta.getExtrato().get(0).tipo());
    }

    @Test
    void rejeitaDepositoZero() {
        assertThrows(IllegalArgumentException.class,
                () -> novaConta().depositar(BigDecimal.ZERO));
    }

    @Test
    void rejeitaDepositoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> novaConta().depositar(new BigDecimal("-1")));
    }

    @Test
    void rejeitaDepositoNulo() {
        assertThrows(NullPointerException.class,
                () -> novaConta().depositar(null));
    }

    @Test
    void rejeitaFracaoDeCentavo() {
        assertThrows(IllegalArgumentException.class,
                () -> novaConta().depositar(new BigDecimal("1.001")));
    }

    @Test
    void aceitaZerosDecimaisAdicionais() {
        Conta conta = novaConta();
        conta.depositar(new BigDecimal("10.000"));

        assertEquals(new BigDecimal("10.00"), conta.getSaldo());
    }

    @Test
    void transfereEntreContas() {
        Conta origem = novaConta();
        Conta destino = new ContaPoupanca("002", "Bruno");

        origem.depositar(new BigDecimal("100.00"));
        origem.transferirPara(destino, new BigDecimal("40.00"));

        assertEquals(new BigDecimal("60.00"), origem.getSaldo());
        assertEquals(new BigDecimal("40.00"), destino.getSaldo());
        assertEquals(TipoTransacao.TRANSFERENCIA_ENVIADA,
                origem.getExtrato().get(1).tipo());
        assertEquals(TipoTransacao.TRANSFERENCIA_RECEBIDA,
                destino.getExtrato().get(0).tipo());
    }

    @Test
    void rejeitaTransferenciaSemSaldo() {
        Conta origem = novaConta();
        Conta destino = new ContaPoupanca("002", "Bruno");

        assertThrows(IllegalStateException.class,
                () -> origem.transferirPara(
                        destino, new BigDecimal("10.00")));

        assertEquals(new BigDecimal("0.00"), destino.getSaldo());
        assertTrue(destino.getExtrato().isEmpty());
    }

    @Test
    void rejeitaTransferenciaParaSi() {
        Conta conta = novaConta();

        assertThrows(IllegalArgumentException.class,
                () -> conta.transferirPara(
                        conta, new BigDecimal("10.00")));
    }

    @Test
    void rejeitaDestinoNulo() {
        assertThrows(NullPointerException.class,
                () -> novaConta().transferirPara(
                        null, new BigDecimal("10.00")));
    }

    @Test
    void extratoNaoPermiteAlteracaoExterna() {
        Conta conta = novaConta();
        conta.depositar(new BigDecimal("10.00"));

        assertThrows(UnsupportedOperationException.class,
                () -> conta.getExtrato().clear());
    }
}