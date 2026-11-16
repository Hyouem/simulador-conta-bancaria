package br.com.portfolio.banco;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public final class App {

    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, Conta> contas = new LinkedHashMap<>();

    public static void main(String[] args) {
        new App().executar();
    }

    private void executar() {
        while (true) {
            menu();

            if (!scanner.hasNextLine()) {
                System.out.println("Encerrado.");
                return;
            }

            String opcao = scanner.nextLine().trim();

            try {
                switch (opcao) {
                    case "1" -> criarConta();
                    case "2" -> depositar();
                    case "3" -> sacar();
                    case "4" -> transferir();
                    case "5" -> consultarSaldo();
                    case "6" -> consultarExtrato();
                    case "7" -> aplicarRendimento();
                    case "8" -> listarContas();
                    case "0" -> {
                        System.out.println("Encerrado.");
                        return;
                    }
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (IllegalArgumentException | IllegalStateException exception) {
                System.out.println("Erro: " + exception.getMessage());
            }
        }
    }

    private void menu() {
        System.out.println();
        System.out.println("=== SIMULADOR BANCARIO ===");
        System.out.println("1 - Criar conta");
        System.out.println("2 - Depositar");
        System.out.println("3 - Sacar");
        System.out.println("4 - Transferir");
        System.out.println("5 - Consultar saldo");
        System.out.println("6 - Consultar extrato");
        System.out.println("7 - Aplicar rendimento");
        System.out.println("8 - Listar contas");
        System.out.println("0 - Sair");
        System.out.print("Opcao: ");
    }

    private String ler(String mensagem) {
        System.out.print(mensagem);

        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("Entrada encerrada");
        }

        return scanner.nextLine().trim();
    }

    private BigDecimal lerValor() {
        String texto = ler("Valor (ex.: 100.50): ");

        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Valor monetario invalido");
        }
    }

    private Conta buscarConta(String mensagem) {
        String numero = ler(mensagem);
        Conta conta = contas.get(numero);

        if (conta == null) {
            throw new IllegalArgumentException("Conta nao encontrada: " + numero);
        }

        return conta;
    }

    private void criarConta() {
        String numero = ler("Numero: ");

        if (contas.containsKey(numero)) {
            throw new IllegalArgumentException("Numero ja cadastrado");
        }

        String titular = ler("Titular: ");
        String tipo = ler("Tipo (1=Corrente, 2=Poupanca): ");

        Conta conta = switch (tipo) {
            case "1" -> new ContaCorrente(numero, titular);
            case "2" -> new ContaPoupanca(numero, titular);
            default -> throw new IllegalArgumentException("Tipo invalido");
        };

        contas.put(conta.getNumero(), conta);
        System.out.println("Conta criada: " + conta.getNumero());
    }

    private void depositar() {
        Conta conta = buscarConta("Numero da conta: ");
        conta.depositar(lerValor());
        mostrarSaldo(conta);
    }

    private void sacar() {
        Conta conta = buscarConta("Numero da conta: ");
        conta.sacar(lerValor());
        mostrarSaldo(conta);
    }

    private void transferir() {
        Conta origem = buscarConta("Conta de origem: ");
        Conta destino = buscarConta("Conta de destino: ");

        origem.transferirPara(destino, lerValor());

        System.out.println("Transferencia realizada.");
        mostrarSaldo(origem);
    }

    private void consultarSaldo() {
        mostrarSaldo(buscarConta("Numero da conta: "));
    }

    private void mostrarSaldo(Conta conta) {
        System.out.println(
                "Conta " + conta.getNumero()
                + " | Saldo: R$ " + conta.getSaldo()
        );
    }

    private void consultarExtrato() {
        Conta conta = buscarConta("Numero da conta: ");

        if (conta.getExtrato().isEmpty()) {
            System.out.println("Extrato vazio.");
            return;
        }

        conta.getExtrato().forEach(transacao ->
                System.out.println(
                        transacao.tipo()
                        + " | R$ " + transacao.valor()
                        + " | Saldo: R$ " + transacao.saldoApos()
                        + " | " + transacao.descricao()
                )
        );
    }

    private void aplicarRendimento() {
        Conta conta = buscarConta("Numero da conta: ");

        if (!(conta instanceof ContaPoupanca poupanca)) {
            throw new IllegalArgumentException(
                    "Rendimento disponivel apenas para poupanca"
            );
        }

        BigDecimal rendimento = poupanca.aplicarRendimento();

        System.out.println("Rendimento: R$ " + rendimento);
        mostrarSaldo(poupanca);
    }

    private void listarContas() {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }

        contas.values().forEach(conta ->
                System.out.println(
                        conta.getNumero()
                        + " | " + conta.getTitular()
                        + " | " + conta.getClass().getSimpleName()
                        + " | R$ " + conta.getSaldo()
                )
        );
    }
}