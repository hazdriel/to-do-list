package iu;

import fachada.Gerenciador;
import iu.relatorio.FormatadorRelatorio;
import negocio.DadosEstatisticos;
import negocio.DadosEstatisticos.TarefasAtencao;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

public final class InterfaceRelatorios {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    private final FormatadorRelatorio formatador;
    
    public InterfaceRelatorios(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
        this.formatador = new FormatadorRelatorio(); 
    }
    
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
    
    private void exibirRelatorioProdutividade() {
        UtilitariosInterface.limparTela();
        System.out.println("--- RELATÓRIO DE PRODUTIVIDADE ---");
        
        LocalDateTime dataFim = LocalDateTime.now();
        LocalDateTime dataInicio = dataFim.minusDays(30);
        
        DadosEstatisticos dados = gerenciador.obterEstatisticasProdutividade(dataInicio, dataFim);
        
        String relatorio = formatador.formatarRelatorioProdutividade(dados, gerenciador.getUsuarioLogado());
        System.out.println(relatorio);
    }
    
    private void exibirRelatorioStatus() {
        UtilitariosInterface.limparTela();
        System.out.println("--- RELATÓRIO DE STATUS ---");
        
        LocalDateTime dataFim = LocalDateTime.now();
        LocalDateTime dataInicio = dataFim.minusDays(30); // O período pode ser ajustado conforme a regra de negócio.
        
        DadosEstatisticos dados = gerenciador.obterEstatisticasProdutividade(dataInicio, dataFim);
        TarefasAtencao tarefasAtencao = gerenciador.obterTarefasQueNecessitamAtencao();
        
        String relatorio = formatador.formatarRelatorioStatus(dados, tarefasAtencao, gerenciador.getUsuarioLogado());
        System.out.println(relatorio);
    }
    
    private void exibirRelatorioTemporal() {
        UtilitariosInterface.limparTela();
        System.out.println("--- RELATÓRIO DE PRODUTIVIDADE POR PERÍODO ---");
        
        int dias = lerPeriodoDeDias();
        
        LocalDateTime dataFim = LocalDateTime.now();
        LocalDateTime dataInicio = dataFim.minusDays(dias);
        
        DadosEstatisticos dados = gerenciador.obterEstatisticasProdutividade(dataInicio, dataFim);
        Map<LocalDateTime, Long> produtividadeDiaria = gerenciador.obterProdutividadeTemporal(dataInicio, dataFim);
        
        String relatorio = formatador.formatarRelatorioTemporal(
                produtividadeDiaria, dados, gerenciador.getUsuarioLogado(), dataInicio, dias);
        System.out.println(relatorio);
    }
    private int lerPeriodoDeDias() {
        System.out.print("A partir de quantos dias deseja analisar? (1-365, padrão: 7): ");
        String entrada = scanner.nextLine();
        
        if (entrada.trim().isEmpty()) {
            return 7;
        }
        
        try {
            int dias = Integer.parseInt(entrada.trim());
            if (dias > 0 && dias <= 365) {
                return dias;
            } else {
                System.out.println("⚠️ Período inválido. Usando o padrão de 7 dias.");
                return 7;
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Número inválido. Usando o padrão de 7 dias.");
            return 7;
        }
    }

    public void exibirEstatisticasResumidas() {
        long total = gerenciador.listarTarefas().size();
        long concluidas = gerenciador.listarConcluidas().size();
        long pendentes = gerenciador.listarPendentes().size();
        long atrasadas = gerenciador.listarAtrasadas().size();

        System.out.printf("  - Total de tarefas:   %d\n", total);
        System.out.printf("  - Tarefas concluídas: %d\n", concluidas);
        System.out.printf("  - Tarefas pendentes:  %d\n", pendentes);
        System.out.printf("  - Tarefas atrasadas:  %d\n", atrasadas);
    }
}