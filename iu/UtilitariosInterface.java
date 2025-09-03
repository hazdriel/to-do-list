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

// Utilitários para interface de usuário
public final class UtilitariosInterface {

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
    private static final DateTimeFormatter FORMATO_DATA_HORA_ENTRADA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private UtilitariosInterface() {}

    public static int lerInteiro(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    return Integer.parseInt(input);
                } else {
                    System.out.println("❌ Entrada vazia. Por favor, digite um número inteiro.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Por favor, digite apenas um número inteiro.");
            }
        }
    }

    
    public static String lerString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }


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


    public static LocalDateTime lerDataHora(Scanner scanner) {
        while (true) {
            System.out.print("Digite a data e hora (dd/MM/yyyy HH:mm): ");
            String entrada = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(entrada, FORMATO_DATA_HORA_ENTRADA);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato de data e hora inválido. Tente novamente.");
            }
        }
    }

    public static void limparTela() {
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
    }

    public static void pressioneEnterParaContinuar(Scanner scanner) {
        System.out.print("\n\nPressione enter para continuar...");
        scanner.nextLine();
    }
    public static void exibirTarefas(List<TarefaAbstrata> tarefas, String titulo) {
        FormatadorTarefas.exibirListaResumida(tarefas, titulo);
    }

    public static void exibirTarefaResumo(TarefaAbstrata tarefa) {
        String status = tarefa.getStatus() == Status.CONCLUIDA ? "✔" : " ";
        System.out.printf("\nID: %s | %s [%s] [%s]\n",
                tarefa.getId(), tarefa.getTitulo(), tarefa.getPrioridade(), status);
        System.out.printf("  Prazo: %s\n", formatarDataHora(tarefa.getPrazo()));
        System.out.println("-".repeat(40));
    }

    public static void exibirTarefaDetalhada(TarefaAbstrata tarefa) {
        
        System.out.println("--- DETALHES DA TAREFA ---");
        System.out.printf("ID:          %s\n", tarefa.getId());
        System.out.printf("Título:      %s\n", tarefa.getTitulo());
        System.out.printf("Descrição:   %s\n", tarefa.getDescricao());
        System.out.printf("Prioridade:  %s\n", tarefa.getPrioridade());
        System.out.printf("Status:      %s\n", tarefa.getStatus());
        System.out.printf("Prazo:       %s\n", formatarDataHora(tarefa.getPrazo()));
        String categoriaNome = (tarefa.getCategoria() != null) ? tarefa.getCategoria().getNome() : "Não definida";
        System.out.printf("Categoria:   %s\n", categoriaNome);
        System.out.printf("Criador:     %s\n", tarefa.getCriador().getNome());
        System.out.println("=".repeat(26));
    }

    public static String formatarDataHora(LocalDateTime dataHora) {
        if (dataHora == null) return "N/A";
        return dataHora.format(FORMATO_DATA_HORA);
    }

    public static String formatarData(LocalDate data) {
        if (data == null) return "N/A";
        return data.format(FORMATO_DATA);
    }
 

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

    public static Duration lerDuracaoMinutos(Scanner scanner, String prompt, int valorPadrao, int minMinutos, int maxMinutos) {
        while (true) {
            System.out.print(prompt);
            String entrada = scanner.nextLine().trim();
            
            if (entrada.isEmpty()) {
                return Duration.ofMinutes(valorPadrao);
            }
            
            try {
                int minutos = Integer.parseInt(entrada);
                if (minutos >= minMinutos && minutos <= maxMinutos) {
                    return Duration.ofMinutes(minutos);
                } else {
                    System.out.printf("❌ Duração deve ser entre %d e %d minutos.\n", minMinutos, maxMinutos);
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Digite um número válido.");
            }
        }
    }

    public static String criarBarraProgresso(double progresso) {
        int barras = (int) (progresso / 5); // 20 barras = 100%
        barras = Math.min(20, Math.max(0, barras)); // Garante entre 0 e 20
        return "█".repeat(barras) + "░".repeat(20 - barras);
    }

}