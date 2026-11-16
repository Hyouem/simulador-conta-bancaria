package br.com.portfolio.banco;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ContaPoupancaTest {

    @Test
    void saqueNaoCobraTarifa() {
        ContaPoupanca conta = new ContaPoupanca("001", "Ana");
        conta.depositar(new BigDecimal("100.00"));
        conta.sacar(new BigDecimal("20.00"));

        assertEquals(new BigDecimal("80.00"), conta.getSaldo());
        assertEquals(2, conta.getExtrato().size());
    }

    @Test
    void rejeitaSaqueSemSaldo() {
        ContaPoupanca conta = new ContaPoupanca("001", "Ana");

        assertThrows(IllegalStateException.class,
                () -> conta.sacar(new BigDecimal("1.00")));
    }

    @Test
    void aplicaRendimentoDeMeioPorCento() {
        ContaPoupanca conta = new ContaPoupanca("001", "Ana");
        conta.depositar(new BigDecimal("1000.00"));

        BigDecimal rendimento = conta.aplicarRendimento();

        assertEquals(new BigDecimal("5.00"), rendimento);
        assertEquals(new BigDecimal("1005.00"), conta.getSaldo());
        assertEquals(TipoTransacao.RENDIMENTO,
                conta.getExtrato().get(1).tipo());
    }

    @Test
    void rendimentoZeroNaoCriaTransacao() {
        ContaPoupanca conta = new ContaPoupanca("001", "Ana");

        assertEquals(new BigDecimal("0.00"),
                conta.aplicarRendimento());
        assertTrue(conta.getExtrato().isEmpty());
    }

    @Test
    void arredondaRendimentoComHalfEven() {
        ContaPoupanca conta = new ContaPoupanca("001", "Ana");
        conta.depositar(new BigDecimal("1.00"));

        assertEquals(new BigDecimal("0.00"),
                conta.aplicarRendimento());
        assertEquals(1, conta.getExtrato().size());
    }
}