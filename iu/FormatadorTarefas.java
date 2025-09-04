package iu;

import negocio.entidade.TarefaAbstrata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Utilitário para formatação e exibição de tarefas


public final class FormatadorTarefas {

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private FormatadorTarefas() {

    }

        public static void exibirListaResumida(List<TarefaAbstrata> tarefas, String titulo) {
        if (tarefas == null || tarefas.isEmpty()) {
            System.out.println("\nNenhuma tarefa encontrada para esta visualização.");
            return;
        }

        tarefas = tarefas.stream()
            .sorted(Comparator.comparing(TarefaAbstrata::getId))
            .collect(Collectors.toList());

        System.out.println("\n--- " + titulo.toUpperCase() + " ---");
        for (TarefaAbstrata tarefa : tarefas) {
            exibirResumo(tarefa);
            System.out.println("-".repeat(40));
        }
    }

    
    public static void exibirResumo(TarefaAbstrata tarefa) {
        String statusConclusao = tarefa.estaFinalizada() ? "[✔]" : "[ ]";
        System.out.printf("ID: %-5s | %s [%s] %s\n",
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getPrioridade(),
                statusConclusao
        );
        System.out.println("  Tipo: " + tarefa.getTipo() + " | Status: " + tarefa.getStatus());
        if (tarefa.getPrazo() != null) {
            System.out.println("  Prazo: " + formatarDataHora(tarefa.getPrazo()));
        }
    }

    
    public static void exibirDetalhada(TarefaAbstrata tarefa) {
        System.out.println("\n--- DETALHES DA TAREFA ---");
        System.out.printf("ID:          %s\n", tarefa.getId());
        System.out.printf("Título:      %s\n", tarefa.getTitulo());
        System.out.printf("Descrição:   %s\n", tarefa.getDescricao());
        System.out.printf("Tipo:        %s\n", tarefa.getTipo());
        System.out.printf("Status:      %s\n", tarefa.getStatus());
        System.out.printf("Prioridade:  %s\n", tarefa.getPrioridade());
        System.out.printf("Categoria:   %s\n", (tarefa.getCategoria() != null ? tarefa.getCategoria().getNome() : "Não definida"));
        System.out.printf("Criador:     %s\n", tarefa.getCriador().getNome());
        System.out.printf("Responsável: %s\n", tarefa.getResponsavel().getNome());
        
        if (tarefa.getDataCriacao() != null) {
            System.out.printf("Data de Criação: %s\n", formatarDataHora(tarefa.getDataCriacao()));
        }
        if (tarefa.getPrazo() != null) {
            System.out.printf("Prazo Final:     %s\n", formatarDataHora(tarefa.getPrazo()));
        }
        if (tarefa.estaAtrasada()) {
            System.out.println("\n⚠️  ATENÇÃO: TAREFA ATRASADA!");
        }
    }
    
    public static void exibirEstatisticas(List<TarefaAbstrata> todas, List<TarefaAbstrata> concluidas,
                                          List<TarefaAbstrata> pendentes, List<TarefaAbstrata> atrasadas) {
        System.out.println("\n--- ESTATÍSTICAS GERAIS ---");
        long total = (todas != null) ? todas.size() : 0;
        long nConcluidas = (concluidas != null) ? concluidas.size() : 0;
        long nPendentes = (pendentes != null) ? pendentes.size() : 0;
        long nAtrasadas = (atrasadas != null) ? atrasadas.size() : 0;

        System.out.printf("Total de tarefas:   %d\n", total);
        System.out.printf("Tarefas concluídas: %d (%s%%)\n", nConcluidas, calcularPorcentagem(nConcluidas, total));
        System.out.printf("Tarefas pendentes:  %d (%s%%)\n", nPendentes, calcularPorcentagem(nPendentes, total));
        System.out.printf("Tarefas atrasadas:  %d (%s%%)\n", nAtrasadas, calcularPorcentagem(nAtrasadas, total));
    }

    
    public static void exibirListaAtrasadas(List<TarefaAbstrata> tarefasAtrasadas) {
        if (tarefasAtrasadas == null || tarefasAtrasadas.isEmpty()) {
            System.out.println("\n🎉 Nenhuma tarefa atrasada. Bom trabalho!");
            return;
        }

        tarefasAtrasadas = tarefasAtrasadas.stream()
            .sorted(Comparator.comparing(TarefaAbstrata::getId))
            .collect(Collectors.toList());

        System.out.println("\n--- TAREFAS ATRASADAS ---");
        for (TarefaAbstrata tarefa : tarefasAtrasadas) {
            exibirResumo(tarefa);
            if (tarefa.getPrazo() != null) {
                long diasAtraso = ChronoUnit.DAYS.between(tarefa.getPrazo().toLocalDate(), LocalDate.now());
                
                if (diasAtraso > 1) {
                    System.out.printf("  Status: Atrasada há %d dias.\n", diasAtraso);
                } else if (diasAtraso == 1) {
                    System.out.println("  Status: Atrasada há 1 dia.");
                } else {
                    System.out.println("  Status: O prazo termina hoje.");
                }
            }
            System.out.println("-".repeat(40));
        }
    }

        public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora != null ? dataHora.format(FORMATO_DATA_HORA) : "N/A";
    }

    public static String formatarData(LocalDate data) {
        return data != null ? data.format(FORMATO_DATA) : "N/A";
    }

    private static String calcularPorcentagem(long parte, long total) {
        if (total == 0) {
            return "0.0";
        }
        double porcentagem = (double) parte / total * 100;
        return String.format("%.1f", porcentagem);
    }
    
}