package iu;

import negocio.entidade.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


/**
 * Classe utilitária final com métodos estáticos comuns para todas as classes de interface.
 * Centraliza funcionalidades repetitivas para evitar duplicação de código,
 * garantir consistência visual e aumentar a robustez da entrada de dados.
 */
public final class UtilitariosInterface {

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor privado para impedir a instanciação desta classe utilitária.
     */
    private UtilitariosInterface() {}

    // =================================================================================
    // MÉTODOS DE LEITURA DE DADOS (Robustos)
    // =================================================================================

    /**
     * Lê um número inteiro do console de forma segura. Este método lida com entradas
     * inválidas (que não são números) e consome o caractere de nova linha pendente,
     * simplificando o código nas outras classes.
     *
     * @param scanner A instância do Scanner a ser usada.
     * @return O número inteiro lido.
     */
    public static int lerInteiro(Scanner scanner) {
        while (true) {
            System.out.print("➡️ ");
            if (scanner.hasNextInt()) {
                int numero = scanner.nextInt();
                scanner.nextLine(); // << PONTO CRÍTICO: Consome o '\n' deixado pelo nextInt().
                return numero;
            } else {
                System.out.println("❌ Entrada inválida. Por favor, digite apenas um número inteiro.");
                scanner.nextLine(); // Descarta a entrada inválida.
            }
        }
    }

    /**
     * Lê uma linha de texto do console, removendo espaços em branco no início e no fim.
     * @param scanner A instância do Scanner a ser usada.
     * @param prompt A mensagem a ser exibida para o utilizador.
     * @return A string lida.
     */
    public static String lerString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Lê uma Prioridade do utilizador, insistindo até que uma opção válida seja fornecida.
     * @param scanner A instância do Scanner a ser usada.
     * @return A Prioridade escolhida.
     */
    public static Prioridade lerPrioridade(Scanner scanner) {
        while (true) {
            System.out.print("Defina a prioridade (BAIXA, MEDIA, ALTA): ");
            String entrada = scanner.nextLine().trim().toUpperCase();
            try {
                return Prioridade.valueOf(entrada);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Prioridade inválida. Tente novamente.");
            }
        }
    }

    /**
     * Lê uma data e hora do utilizador, insistindo até que o formato esteja correto.
     * @param scanner A instância do Scanner a ser usada.
     * @return O LocalDateTime lido.
     */
    public static LocalDateTime lerDataHora(Scanner scanner) {
        while (true) {
            System.out.print("Digite a data e hora (dd/MM/yyyy HH:mm): ");
            String entrada = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(entrada, FORMATO_DATA_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato de data e hora inválido. Tente novamente.");
            }
        }
    }

    // ... (Outros métodos de leitura como lerDuracao e lerPeriodicidade seguiriam o mesmo padrão de loop)

    // =================================================================================
    // MÉTODOS DE EXPERIÊNCIA DO UTILIZADOR (UX)
    // =================================================================================
    
    /**
     * Limpa a tela do console imprimindo várias linhas em branco.
     */
    public static void limparTela() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Pausa a execução e aguarda o utilizador pressionar Enter para continuar.
     * @param scanner A instância do Scanner a ser usada.
     */
    public static void pressioneEnterParaContinuar(Scanner scanner) {
        System.out.print("\n_Pressione ENTER para continuar..._");
        scanner.nextLine();
    }

    // =================================================================================
    // MÉTODOS DE EXIBIÇÃO
    // =================================================================================

    /**
     * Exibe uma lista de tarefas com um título.
     * @param tarefas A lista de tarefas a ser exibida.
     * @param titulo O título a ser mostrado.
     */
    public static void exibirTarefas(List<TarefaAbstrata> tarefas, String titulo) {
        limparTela();
        System.out.println("\n--- " + titulo.toUpperCase() + " ---");
        if (tarefas == null || tarefas.isEmpty()) {
            System.out.println("\n📭 Nenhuma tarefa encontrada nesta categoria.");
            return;
        }
        for (TarefaAbstrata tarefa : tarefas) {
            exibirTarefaResumo(tarefa);
        }
    }

    /**
     * Exibe os detalhes resumidos de uma tarefa.
     * @param tarefa A tarefa a ser exibida.
     */
    public static void exibirTarefaResumo(TarefaAbstrata tarefa) {
        String status = tarefa.getStatus() == Status.CONCLUIDA ? "✔" : " ";
        System.out.printf("\nID: %s | %s [%s] [%s]\n",
                tarefa.getId(), tarefa.getTitulo(), tarefa.getPrioridade(), status);
        System.out.printf("  Prazo: %s\n", formatarDataHora(tarefa.getPrazo()));
        System.out.println("-".repeat(40));
    }

    /**
     * Exibe todos os detalhes de uma tarefa de forma organizada.
     * @param tarefa A tarefa a ser detalhada.
     */
    public static void exibirTarefaDetalhada(TarefaAbstrata tarefa) {
        limparTela();
        System.out.println("--- DETALHES DA TAREFA ---");
        System.out.printf("ID:          %s\n", tarefa.getId());
        System.out.printf("Título:      %s\n", tarefa.getTitulo());
        System.out.printf("Descrição:   %s\n", tarefa.getDescricao());
        System.out.printf("Prioridade:  %s\n", tarefa.getPrioridade());
        System.out.printf("Status:      %s\n", tarefa.getStatus());
        System.out.printf("Prazo:       %s\n", formatarDataHora(tarefa.getPrazo()));
        
        // Adiciona uma verificação para evitar NullPointerException se a categoria for nula
        String categoriaNome = (tarefa.getCategoria() != null) ? tarefa.getCategoria().getNome() : "Não definida";
        System.out.printf("Categoria:   %s\n", categoriaNome);
        
        System.out.printf("Criador:     %s\n", tarefa.getCriador().getNome());
        // (outros detalhes específicos de cada tipo de tarefa)
        System.out.println("=".repeat(26));
    }

    // =================================================================================
    // MÉTODOS DE FORMATAÇÃO
    // =================================================================================

    /**
     * Formata um objeto LocalDateTime para uma String padronizada.
     * @param dataHora O LocalDateTime a ser formatado.
     * @return A String formatada ou "N/A" se a entrada for nula.
     */
    public static String formatarDataHora(LocalDateTime dataHora) {
        if (dataHora == null) return "N/A";
        return dataHora.format(FORMATO_DATA_HORA);
    }

    /**
     * Formata um objeto LocalDate para uma String padronizada.
     * @param data O LocalDate a ser formatado.
     * @return A String formatada ou "N/A" se a entrada for nula.
     */
    public static String formatarData(LocalDate data) {
        if (data == null) return "N/A";
        return data.format(FORMATO_DATA);
    }
 

/**
 * Lê uma Periodicidade do utilizador, insistindo até que uma opção válida seja fornecida.
 * @param scanner A instância do Scanner a ser usada.
 * @return O Periodo escolhido.
 */
public static Period lerPeriodicidade(Scanner scanner) {
    while (true) {
        System.out.print("Defina a periodicidade (DIARIA, SEMANAL, MENSAL): ");
        String entrada = scanner.nextLine().trim().toUpperCase();
        
        switch (entrada) {
            case "DIARIA":
                return Period.ofDays(1);
            case "SEMANAL":
                return Period.ofWeeks(1);
            case "MENSAL":
                return Period.ofMonths(1);
            default:
                System.out.println("❌ Periodicidade inválida. Por favor, escolha uma das opções listadas.");
        }
    }
}

/**
 * Lê uma Duração em horas do utilizador, insistindo até que um número válido seja fornecido.
 * @param scanner A instância do Scanner a ser usada.
 * @return A Duração lida.
 */
public static Duration lerDuracao(Scanner scanner) {
        while (true) {
            System.out.print("Defina a duração estimada em horas: ");
            try {
                int horas = Integer.parseInt(scanner.nextLine().trim());
                if (horas > 0) {
                    return Duration.ofHours(horas);
                } else {
                    System.out.println("❌ A duração deve ser um número positivo. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Por favor, digite apenas o número de horas.");
            }
        }
    }

}