package iu;

import java.util.List;

import negocio.entidade.TarefaAntiga;

public class TelaTarefa {

    public static void exibirDetalhada(TarefaAntiga t) {
        System.out.println("ID: " + t.getID());
        System.out.println("Título: " + t.getTitulo());
        System.out.println("Descrição: " + t.getDescricao());
        System.out.println("Criador: " + (t.getCriador() != null ? t.getCriador().getNome() : "N/A"));
        System.out.println("Concluída: " + (t.isConcluida() ? "Sim" : "Não"));
        System.out.println("Prioridade: " + t.getPrioridade());
        System.out.println("Prazo: " + (t.getPrazo() != null ? t.getPrazo().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A"));
        System.out.println("Atrasada: " + (t.isAtrasada() ? "Sim" : "Não"));
    }

    public static void exibirResumo(TarefaAntiga t) {
        System.out.printf("(%s) %s [%s] %s\n",
            t.getID(),
            t.getTitulo(),
            t.getPrioridade(),
            t.isConcluida() ? "[✔]" : "[ ]"
        );
    }

    public static void exibirLista(List<TarefaAntiga> tarefas) {
        if (tarefas == null || tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
            return;
        }

        System.out.println("=== SUAS TAREFAS ===");
        for (TarefaAntiga t : tarefas) {
            exibirResumo(t);
            System.out.println("----------------------------");
        }
    }

     public static void exibirListaAtrasadas(List<TarefaAntiga> tarefas) {
        if (tarefas == null || tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa atrasada encontrada.");
            return;
        }

        System.out.println("=== TAREFAS ATRASADAS ===");
        for (TarefaAntiga t : tarefas) {
            exibirResumo(t);
            if (t.getPrazo() != null) {
                long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(t.getPrazo(), java.time.LocalDate.now());
                System.out.println("Atrasada há " + diasAtraso + " dias");
            }
            System.out.println("----------------------------");
        }
    }
}

   

   