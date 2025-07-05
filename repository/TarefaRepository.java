package repository;

import java.util.ArrayList;
import java.util.List;

import model.Tarefa;

public class TarefaRepository {
  private ArrayList<Tarefa> tarefas = new ArrayList<>();

  public void inserirTarefas(Tarefa tarefa) {
    tarefas.add(tarefa);
  }

  public Tarefa buscarTarefaPorID(String id) {
    for (Tarefa tarefa : tarefas) {
      if (tarefa.getID().equals(id)) {
        return tarefa;
      }
    }
    return null;
  }

  public List<Tarefa> buscarTarefasPorUsuario(String nome) {
    List<Tarefa> resultado = new ArrayList<>(); 
    for (Tarefa tarefa : tarefas) {
      if (tarefa.getCriador().getNome().equals(nome)) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

   public boolean atualizarTarefa(Tarefa tarefaAtualizada) {
        for (int i = 0; i < tarefas.size(); i++) {
            if (tarefas.get(i).getID().equals(tarefaAtualizada.getID())) {
                tarefas.set(i, tarefaAtualizada);
                return true;
            }
        }
        return false;
    }

  public boolean remover(String id) {
        return tarefas.removeIf(c -> c.getID().equals(id));
    }

}
