package br.com.portfolio.banco;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransacaoTest {

    @Test
    void criaTransacaoValida() {
        LocalDateTime data = LocalDateTime.now();

        Transacao transacao = new Transacao(
                data,
                TipoTransacao.DEPOSITO,
                new BigDecimal("10.00"),
                new BigDecimal("10.00"),
                "Deposito"
        );

        assertEquals(data, transacao.data());
        assertEquals(TipoTransacao.DEPOSITO, transacao.tipo());
    }

    @Test
    void rejeitaDataNula() {
        assertThrows(NullPointerException.class,
                () -> new Transacao(
                        null, TipoTransacao.DEPOSITO,
                        BigDecimal.ONE, BigDecimal.ONE, "Teste"));
    }
}