package br.com.portfolio.banco;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ContaCorrenteTest {

    @Test
    void cobraTarifaNoSaque() {
        ContaCorrente conta = new ContaCorrente("001", "Ana");
        conta.depositar(new BigDecimal("100.00"));
        conta.sacar(new BigDecimal("20.00"));

        assertEquals(new BigDecimal("77.50"), conta.getSaldo());
        assertEquals(3, conta.getExtrato().size());
        assertEquals(TipoTransacao.SAQUE,
                conta.getExtrato().get(1).tipo());
        assertEquals(TipoTransacao.TARIFA,
                conta.getExtrato().get(2).tipo());
    }

    @Test
    void informaTarifaCorreta() {
        ContaCorrente conta = new ContaCorrente("001", "Ana");

        assertEquals(new BigDecimal("2.50"),
                conta.getTarifaSaque());
    }

    @Test
    void permiteSaqueQueConsomeTodoSaldo() {
        ContaCorrente conta = new ContaCorrente("001", "Ana");
        conta.depositar(new BigDecimal("12.50"));
        conta.sacar(new BigDecimal("10.00"));

        assertEquals(new BigDecimal("0.00"), conta.getSaldo());
    }

    @Test
    void rejeitaSaqueSemSaldoParaTarifa() {
        ContaCorrente conta = new ContaCorrente("001", "Ana");
        conta.depositar(new BigDecimal("10.00"));

        assertThrows(IllegalStateException.class,
                () -> conta.sacar(new BigDecimal("10.00")));

        assertEquals(new BigDecimal("10.00"), conta.getSaldo());
        assertEquals(1, conta.getExtrato().size());
    }

    @Test
    void rejeitaSaqueNegativo() {
        ContaCorrente conta = new ContaCorrente("001", "Ana");

        assertThrows(IllegalArgumentException.class,
                () -> conta.sacar(new BigDecimal("-1.00")));
    }

    @Test
    void rejeitaSaqueNulo() {
        ContaCorrente conta = new ContaCorrente("001", "Ana");

        assertThrows(NullPointerException.class,
                () -> conta.sacar(null));
    }
}