package iu.relatorio;

import negocio.entidade.*;
import negocio.DadosEstatisticos;
import negocio.DadosEstatisticos.TarefasAtencao;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

// Formatador para geração de relatórios em texto
public class FormatadorRelatorio {
    
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public String formatarRelatorioProdutividade(DadosEstatisticos dados, Usuario usuario) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("               📊 RELATÓRIO DE PRODUTIVIDADE               \n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("👤 Usuário: ").append(usuario.getNome()).append("\n");
        sb.append("📅 Período: ").append(dados.getDataInicio().format(FORMATO_DATA_HORA));
        sb.append(" até ").append(dados.getDataFim().format(FORMATO_DATA_HORA)).append("\n");
        sb.append("🕒 Gerado em: ").append(LocalDateTime.now().format(FORMATO_DATA_HORA)).append("\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");
        
        sb.append(criarSeparadorComTitulo("📊 RESUMO GERAL"));
        sb.append("• Total de Tarefas Criadas: ").append(dados.getTotalTarefas()).append("\n");
        sb.append("• Tarefas Concluídas: ").append(dados.getTarefasConcluidas()).append("\n");
        sb.append("• Taxa de Conclusão: ").append(formatarPorcentagem(dados.getTaxaConclusao())).append("\n");
        sb.append("• Tarefas Canceladas: ").append(dados.getTarefasCanceladas()).append("\n");
        sb.append("• Tarefas Pendentes: ").append(dados.getTarefasPendentes()).append("\n");
        sb.append("• Tarefas em Progresso: ").append(dados.getTarefasEmProgresso()).append("\n\n");
        
        if (!dados.getDistribuicaoPorCategoria().isEmpty()) {
            sb.append(criarSeparadorComTitulo("📂 DISTRIBUIÇÃO POR CATEGORIA"));
            for (Map.Entry<Categoria, Integer> entry : dados.getDistribuicaoPorCategoria().entrySet()) {
                double percentual = (double) entry.getValue() / dados.getTotalTarefas() * 100;
                sb.append("• ").append(entry.getKey().getNome()).append(": ")
                  .append(entry.getValue()).append(" (").append(formatarPorcentagem(percentual)).append(")\n");
            }
            sb.append("\n");
        }
          
        if (!dados.getDistribuicaoPorPrioridade().isEmpty()) {
            sb.append(criarSeparadorComTitulo("⚡ DISTRIBUIÇÃO POR PRIORIDADE"));
            for (Map.Entry<Prioridade, Integer> entry : dados.getDistribuicaoPorPrioridade().entrySet()) {
                double percentual = (double) entry.getValue() / dados.getTotalTarefas() * 100;
                sb.append("• ").append(entry.getKey().name()).append(": ")
                  .append(entry.getValue()).append(" (").append(formatarPorcentagem(percentual)).append(")\n");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    // Formata relatório de status
    public String formatarRelatorioStatus(DadosEstatisticos dados, TarefasAtencao tarefasAtencao, Usuario usuario) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                  📋 RELATÓRIO DE STATUS                   \n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("👤 Usuário: ").append(usuario.getNome()).append("\n");
        sb.append("🕒 Gerado em: ").append(LocalDateTime.now().format(FORMATO_DATA_HORA)).append("\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");
        
        // Visão geral atual
        sb.append(criarSeparadorComTitulo("📈 VISÃO GERAL ATUAL"));
        sb.append("┌─────────────────┬──────────┬────────────┐\n");
        sb.append("│ Status          │ Qtd      │ Percentual │\n");
        sb.append("├─────────────────┼──────────┼────────────┤\n");
        
        Map<Status, Integer> distribuicao = dados.getDistribuicaoPorStatus();
        for (Status status : Status.values()) {
            int qtd = distribuicao.getOrDefault(status, 0);
            double percentual = dados.getTotalTarefas() > 0 ? (double) qtd / dados.getTotalTarefas() * 100 : 0;
            sb.append(String.format("│ %-15s │ %8d │ %9s │\n", 
                formatarNomeStatus(status), qtd, formatarPorcentagem(percentual)));
        }
        sb.append("└─────────────────┴──────────┴────────────┘\n\n");
        
        sb.append(formatarTarefasAtencao(tarefasAtencao));
        
        return sb.toString();
    }

    public String formatarRelatorioTemporal(Map<LocalDateTime, Long> produtividadeDiaria, 
                                           DadosEstatisticos dados, 
                                           Usuario usuario,
                                           LocalDateTime dataInicio,
                                           int diasPeriodo) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("               ⏰ RELATÓRIO TEMPORAL                       \n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("👤 Usuário: ").append(usuario.getNome()).append("\n");
        sb.append("📅 Período: ").append(diasPeriodo).append(" dias\n");
        sb.append("🕒 Gerado em: ").append(LocalDateTime.now().format(FORMATO_DATA_HORA)).append("\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");
        
        sb.append(criarSeparadorComTitulo("📈 PRODUTIVIDADE DIÁRIA"));
        
        LocalDateTime dataAtual = dataInicio;
        long maxTarefas = produtividadeDiaria.values().stream().mapToLong(Long::longValue).max().orElse(1);
        
        for (int i = 0; i < diasPeriodo; i++) {
            String diaSemana = dataAtual.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.of("pt", "BR"));
            String dataFormatada = dataAtual.format(FORMATO_DATA);
            
            LocalDateTime diaChave = dataAtual.toLocalDate().atStartOfDay();
            long tarefasDia = produtividadeDiaria.getOrDefault(diaChave, 0L);
            
            String barra = criarBarraProgresso((int)tarefasDia, (int)maxTarefas, 20);
            
            sb.append(String.format("%s (%s): %s %d %s\n", 
                dataFormatada, diaSemana, barra, tarefasDia, 
                tarefasDia == 1 ? "concluída" : "concluídas"));
                
            dataAtual = dataAtual.plusDays(1);
        }
        
        sb.append("\n").append(criarSeparadorComTitulo("📊 ESTATÍSTICAS DO PERÍODO"));
        long totalConcluidas = produtividadeDiaria.values().stream().mapToLong(Long::longValue).sum();
        double mediaDiaria = (double) totalConcluidas / diasPeriodo;
        
        sb.append("• Total Concluídas no Período: ").append(totalConcluidas).append(" tarefas\n");
        sb.append("• Média Diária: ").append(String.format("%.1f", mediaDiaria)).append(" tarefas/dia\n");
        sb.append("• Taxa de Conclusão Geral: ").append(formatarPorcentagem(dados.getTaxaConclusao())).append("\n\n");
        
        return sb.toString();
    }
    
    
    private String formatarTarefasAtencao(TarefasAtencao tarefasAtencao) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(criarSeparadorComTitulo("⚠️ TAREFAS QUE PRECISAM DE ATENÇÃO"));
        
        if (!tarefasAtencao.getAtrasadas().isEmpty()) {
            sb.append("🔴 ATRASADAS (").append(tarefasAtencao.getAtrasadas().size()).append("):\n");
            for (TarefaAbstrata tarefa : tarefasAtencao.getAtrasadas().subList(0, Math.min(5, tarefasAtencao.getAtrasadas().size()))) {
                long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                    tarefa.getPrazo(), LocalDateTime.now());
                sb.append("• [").append(tarefa.getId().substring(0, Math.min(4, tarefa.getId().length()))).append("] ")
                  .append(tarefa.getTitulo()).append(" - Atraso: ").append(diasAtraso).append(" dias\n");
            }
            if (tarefasAtencao.getAtrasadas().size() > 5) {
                sb.append("  ... e mais ").append(tarefasAtencao.getAtrasadas().size() - 5).append(" tarefas\n");
            }
            sb.append("\n");
        }
        

        if (!tarefasAtencao.getVencemHoje().isEmpty()) {
            sb.append("🟡 VENCEM HOJE (").append(tarefasAtencao.getVencemHoje().size()).append("):\n");
            for (TarefaAbstrata tarefa : tarefasAtencao.getVencemHoje().subList(0, Math.min(5, tarefasAtencao.getVencemHoje().size()))) {
                sb.append("• [").append(tarefa.getId().substring(0, Math.min(4, tarefa.getId().length()))).append("] ")
                  .append(tarefa.getTitulo()).append(" - Prazo: ").append(tarefa.getPrazo().format(FORMATO_DATA)).append("\n");
            }
            sb.append("\n");
        }
        

        if (!tarefasAtencao.getVencemAmanha().isEmpty()) {
            sb.append("🟠 VENCEM AMANHÃ (").append(tarefasAtencao.getVencemAmanha().size()).append("):\n");
            for (TarefaAbstrata tarefa : tarefasAtencao.getVencemAmanha().subList(0, Math.min(5, tarefasAtencao.getVencemAmanha().size()))) {
                sb.append("• [").append(tarefa.getId().substring(0, Math.min(4, tarefa.getId().length()))).append("] ")
                  .append(tarefa.getTitulo()).append(" - Prazo: ").append(tarefa.getPrazo().format(FORMATO_DATA)).append("\n");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    private String criarSeparadorComTitulo(String titulo) {
        return "📊 " + titulo + "\n" + "─".repeat(60) + "\n";
    }
    
    private String formatarPorcentagem(double valor) {
        return String.format("%.1f%%", valor);
    }
    
    private String formatarNomeStatus(Status status) {
        return switch (status) {
            case PENDENTE -> "PENDENTE";
            case EM_PROGRESSO -> "EM_PROGRESSO";
            case CONCLUIDA -> "CONCLUIDA";
            case CANCELADA -> "CANCELADA";
            default -> status.name();
        };
    }
    
    private String criarBarraProgresso(int valor, int maximo, int largura) {
        if (maximo == 0) return "░".repeat(largura);
        
        int preenchido = (int) Math.round((double) valor / maximo * largura);
        return "█".repeat(preenchido) + "░".repeat(largura - preenchido);
    }
}
