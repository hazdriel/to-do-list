package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import model.*;


public class TarefaRepository {
  private Map<String, Tarefa> tarefas = new HashMap<>();

  public void inserirTarefa(Tarefa tarefa) {
    tarefas.put(tarefa.getID(), tarefa);
  }

  public Tarefa buscarTarefaPorID(String id) {
    return tarefas.get(id);
  }

  public List<Tarefa> listarTarefasPorUsuario(Usuario usuario) {
    List<Tarefa> resultado = new ArrayList<>();
    for (Tarefa tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario)) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

  public boolean atualizarTarefa(Tarefa tarefaAtualizada) {
    if (tarefas.containsKey(tarefaAtualizada.getID())) {
      tarefas.put(tarefaAtualizada.getID(), tarefaAtualizada);
      return true;
    }
    return false;
  }

  public boolean remover(String id) {
    return tarefas.remove(id) != null;
  }

  public List<Tarefa> buscarPorPrioridade(Usuario usuario, Prioridade prioridade) {
    List<Tarefa> resultado = new ArrayList<>();
    for (Tarefa tarefa : tarefas.values()) {
        if (tarefa.getCriador().equals(usuario) && tarefa.getPrioridade() == prioridade) {
            resultado.add(tarefa);
        }
    }
    return resultado;
  }

  public List<Tarefa> buscarConcluidas(Usuario usuario) {
    List<Tarefa> resultado = new ArrayList<>();
    for (Tarefa tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario) && tarefa.isConcluida()) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

   public List<Tarefa> buscarPendentes(Usuario usuario) {
    List<Tarefa> resultado = new ArrayList<>();
    for (Tarefa tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario) && !tarefa.isConcluida()) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

  public List<Tarefa> buscarAtrasadas(Usuario usuario) {
    List<Tarefa> resultado = new ArrayList<>();
    for (Tarefa tarefa : tarefas.values()) {
        if (tarefa.getCriador().equals(usuario) && tarefa.isAtrasada()) {
            resultado.add(tarefa);
        }
    }
    return resultado;
}
}
