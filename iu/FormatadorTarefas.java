package iu;

import negocio.entidade.TarefaAbstrata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Classe utilitária final responsável por formatar e exibir informações de tarefas no console.
 * Todos os métodos são estáticos, e a classe não pode ser instanciada nem estendida.
 */
public final class FormatadorTarefas {

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor privado para impedir a instanciação desta classe utilitária.
     */
    private FormatadorTarefas() {
        // Esta classe não deve ser instanciada.
    }

    /**
     * Exibe uma lista de tarefas em um formato resumido e legível.
     * @param tarefas A lista de tarefas a ser exibida.
     * @param titulo O título que será apresentado acima da lista.
     */
    public static void exibirListaResumida(List<TarefaAbstrata> tarefas, String titulo) {
        if (tarefas == null || tarefas.isEmpty()) {
            System.out.println("\nNenhuma tarefa encontrada para esta visualização.");
            return;
        }

        System.out.println("\n--- " + titulo.toUpperCase() + " ---");
        for (TarefaAbstrata tarefa : tarefas) {
            exibirResumo(tarefa);
            System.out.println("-".repeat(40));
        }
    }

    /**
     * Exibe o resumo de uma única tarefa, com suas informações mais importantes.
     * @param tarefa A tarefa a ser resumida.
     */
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

    /**
     * Exibe um relatório completo e detalhado de uma única tarefa.
     * @param tarefa A tarefa a ser detalhada.
     */
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
    
    /**
     * Exibe um resumo estatístico simples sobre as listas de tarefas fornecidas.
     * @param todas A lista completa de tarefas.
     * @param concluidas A lista de tarefas concluídas.
     * @param pendentes A lista de tarefas pendentes.
     * @param atrasadas A lista de tarefas atrasadas.
     */
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

    /**
     * Exibe uma lista específica de tarefas atrasadas, com detalhes sobre o atraso.
     * @param tarefasAtrasadas A lista de tarefas com prazo vencido.
     */
    public static void exibirListaAtrasadas(List<TarefaAbstrata> tarefasAtrasadas) {
        if (tarefasAtrasadas == null || tarefasAtrasadas.isEmpty()) {
            System.out.println("\n🎉 Nenhuma tarefa atrasada. Bom trabalho!");
            return;
        }

        System.out.println("\n--- TAREFAS ATRASADAS ---");
        for (TarefaAbstrata tarefa : tarefasAtrasadas) {
            exibirResumo(tarefa);
            if (tarefa.getPrazo() != null) {
                long diasAtraso = ChronoUnit.DAYS.between(tarefa.getPrazo().toLocalDate(), LocalDate.now());
                
                // Lógica para exibir a mensagem correta ("dia" vs "dias")
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

    /**
     * Formata um objeto LocalDateTime para uma String legível.
     * @param dataHora O objeto LocalDateTime a ser formatado.
     * @return A data e hora formatada ou "N/A" se for nulo.
     */
    public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora != null ? dataHora.format(FORMATO_DATA_HORA) : "N/A";
    }

    /**
     * Formata um objeto LocalDate para uma String legível.
     * @param data O objeto LocalDate a ser formatado.
     * @return A data formatada ou "N/A" se for nulo.
     */
    public static String formatarData(LocalDate data) {
        return data != null ? data.format(FORMATO_DATA) : "N/A";
    }

    /**
     * Método auxiliar privado para calcular a porcentagem de forma segura.
     */
    private static String calcularPorcentagem(long parte, long total) {
        if (total == 0) {
            return "0.0";
        }
        double porcentagem = (double) parte / total * 100;
        return String.format("%.1f", porcentagem);
    }
    
}