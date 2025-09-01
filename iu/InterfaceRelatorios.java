package iu;

import fachada.Gerenciador;
import iu.relatorio.FormatadorRelatorio;
import negocio.DadosEstatisticos;
import negocio.DadosEstatisticos.TarefasAtencao;
import negocio.excecao.sessao.SessaoJaInativaException;
import negocio.excecao.usuario.UsuarioVazioException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;


/**
 * Módulo da interface de utilizador responsável por gerar e exibir relatórios.
 * Atua como um controlador que solicita os dados ao Gerenciador e delega a
 * formatação para a classe FormatadorRelatorio.
 */
public final class InterfaceRelatorios {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    private final FormatadorRelatorio formatador;
    
    /**
     * Construtor que recebe as dependências necessárias.
     * @param scanner A instância compartilhada do Scanner para entrada do utilizador.
     * @param gerenciador A fachada do sistema para acesso aos dados.
     */
    public InterfaceRelatorios(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
        this.formatador = new FormatadorRelatorio(); // O formatador é específico deste módulo.
    }
    
    /**
     * Exibe o menu de relatórios em um loop, permitindo que o utilizador
     * visualize vários relatórios até decidir voltar.
     */
    public void exibirMenuRelatorios() {
        boolean executando = true;
        while (executando) {
            UtilitariosInterface.limparTela();
            System.out.println("--- 📊 RELATÓRIOS E ESTATÍSTICAS ---");
            System.out.println("1 -> Relatório de Produtividade (Últimos 30 dias)");
            System.out.println("2 -> Relatório de Status das Tarefas");
            System.out.println("3 -> Relatório de Produtividade por Período");
            System.out.println("0 -> Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            
            int opcao = UtilitariosInterface.lerInteiro(scanner);
            
            switch (opcao) {
                case 1 -> exibirRelatorioProdutividade();
                case 2 -> exibirRelatorioStatus();
                case 3 -> exibirRelatorioTemporal();
                case 0 -> {
                    System.out.println("\nVoltando ao menu principal...");
                    executando = false;
                }
                default -> System.out.println("❌ Opção inválida.");
            }

            if (executando) {
                UtilitariosInterface.pressioneEnterParaContinuar(scanner);
            }
        }
    }
    
    /**
     * Gera e exibe o relatório de produtividade dos últimos 30 dias.
     */
    private void exibirRelatorioProdutividade() {
        try{
            UtilitariosInterface.limparTela();
            System.out.println("--- RELATÓRIO DE PRODUTIVIDADE ---");
        
            LocalDateTime dataFim = LocalDateTime.now();
            LocalDateTime dataInicio = dataFim.minusDays(30);

            DadosEstatisticos dados = null;
            dados = gerenciador.obterEstatisticasProdutividade(dataInicio, dataFim);

            String relatorio = null;
            relatorio = formatador.formatarRelatorioProdutividade(dados, gerenciador.getUsuarioLogado());
            System.out.println(relatorio);
        } catch (SessaoJaInativaException e) {
            System.out.println("\n❌ Erro ao gerar relatório de produtividade: " + e.getMessage());
        } catch (UsuarioVazioException e) {
            System.out.println("\n❌ Erro ao gerar relatório de produtividade: " + e.getMessage());
        }
    }
    
    /**
     * Gera e exibe um relatório sobre o status geral das tarefas.
     */
    private void exibirRelatorioStatus() {
        try {

            UtilitariosInterface.limparTela();
            System.out.println("--- RELATÓRIO DE STATUS ---");

            LocalDateTime dataFim = LocalDateTime.now();
            LocalDateTime dataInicio = dataFim.minusDays(30); // O período pode ser ajustado conforme a regra de negócio.

            DadosEstatisticos dados = gerenciador.obterEstatisticasProdutividade(dataInicio, dataFim);
            TarefasAtencao tarefasAtencao = gerenciador.obterTarefasQueNecessitamAtencao();

            String relatorio = formatador.formatarRelatorioStatus(dados, tarefasAtencao, gerenciador.getUsuarioLogado());
            System.out.println(relatorio);

        } catch (SessaoJaInativaException e) {
            System.out.println("\n❌ Erro ao exibir relatório de status: " + e.getMessage());
        } catch (UsuarioVazioException e) {
            System.out.println("\n❌ Erro ao exibir relatório de status: " + e.getMessage());
        }
    }
    
    /**
     * Gera e exibe um relatório de produtividade para um período de dias personalizado.
     */
    private void exibirRelatorioTemporal() {
        try {
            UtilitariosInterface.limparTela();
            System.out.println("--- RELATÓRIO DE PRODUTIVIDADE POR PERÍODO ---");

            int dias = lerPeriodoDeDias(); // Lógica de leitura extraída para um método auxiliar.

            LocalDateTime dataFim = LocalDateTime.now();
            LocalDateTime dataInicio = dataFim.minusDays(dias);

            DadosEstatisticos dados = gerenciador.obterEstatisticasProdutividade(dataInicio, dataFim);
            Map<LocalDateTime, Long> produtividadeDiaria = gerenciador.obterProdutividadeTemporal(dataInicio, dataFim);

            String relatorio = formatador.formatarRelatorioTemporal(
                    produtividadeDiaria, dados, gerenciador.getUsuarioLogado(), dataInicio, dias);
            System.out.println(relatorio);
        } catch (SessaoJaInativaException e) {
            System.out.println("\n❌ Erro ao exibir relatório por período: " + e.getMessage());
        } catch (UsuarioVazioException e) {
            System.out.println("\n❌ Erro ao exibir relatório por período: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para ler e validar o número de dias para o relatório temporal.
     * @return O número de dias, validado entre 1 e 365.
     */
    private int lerPeriodoDeDias() {
        System.out.print("Quantos dias para trás deseja analisar? (1-365, padrão: 7): ");
        String entrada = scanner.nextLine();
        
        if (entrada.trim().isEmpty()) {
            return 7; // Retorna o valor padrão se a entrada for vazia.
        }
        
        try {
            int dias = Integer.parseInt(entrada.trim());
            if (dias > 0 && dias <= 365) {
                return dias;
            } else {
                System.out.println("⚠️ Período inválido. A usar o padrão de 7 dias.");
                return 7;
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Número inválido. A usar o padrão de 7 dias.");
            return 7;
        }
    }

    /**
     * Exibe um resumo rápido das estatísticas de tarefas.
     * Este método é chamado por outras partes da UI, como o perfil do utilizador.
     */
    public void exibirEstatisticasResumidas() {
        try {
            long total = gerenciador.listarTarefas().size();
            long concluidas = gerenciador.listarConcluidas().size();
            long pendentes = gerenciador.listarPendentes().size();
            long atrasadas = gerenciador.listarAtrasadas().size();

            System.out.printf("  - Total de tarefas:   %d\n", total);
            System.out.printf("  - Tarefas concluídas: %d\n", concluidas);
            System.out.printf("  - Tarefas pendentes:  %d\n", pendentes);
            System.out.printf("  - Tarefas atrasadas:  %d\n", atrasadas);
        } catch (SessaoJaInativaException e) {
            System.out.println("\n❌ Erro ao exibir estatísticas: " + e.getMessage());
        }
    }
}
