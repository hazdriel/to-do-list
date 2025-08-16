package dados;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import negocio.entidade.*;

import java.util.HashMap;


public class RepositorioTarefas {
  private Map<String, TarefaAntiga> tarefas = new HashMap<>();

  public void inserirTarefa(TarefaAntiga tarefa) {
    tarefas.put(tarefa.getID(), tarefa);
  }

  public TarefaAntiga buscarTarefaPorID(String id) {
    return tarefas.get(id);
  }

  public List<TarefaAntiga> listarTarefasPorUsuario(Usuario usuario) {
    List<TarefaAntiga> resultado = new ArrayList<>();
    for (TarefaAntiga tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario)) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

  public boolean atualizarTarefa(TarefaAntiga tarefaAtualizada) {
    if (tarefas.containsKey(tarefaAtualizada.getID())) {
      tarefas.put(tarefaAtualizada.getID(), tarefaAtualizada);
      return true;
    }
    return false;
  }

  public boolean remover(String id) {
    return tarefas.remove(id) != null;
  }

  public List<TarefaAntiga> buscarPorPrioridade(Usuario usuario, Prioridade prioridade) {
    List<TarefaAntiga> resultado = new ArrayList<>();
    for (TarefaAntiga tarefa : tarefas.values()) {
        if (tarefa.getCriador().equals(usuario) && tarefa.getPrioridade() == prioridade) {
            resultado.add(tarefa);
        }
    }
    return resultado;
  }

  public List<TarefaAntiga> buscarConcluidas(Usuario usuario) {
    List<TarefaAntiga> resultado = new ArrayList<>();
    for (TarefaAntiga tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario) && tarefa.isConcluida()) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

   public List<TarefaAntiga> buscarPendentes(Usuario usuario) {
    List<TarefaAntiga> resultado = new ArrayList<>();
    for (TarefaAntiga tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario) && !tarefa.isConcluida()) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

  public List<TarefaAntiga> buscarAtrasadas(Usuario usuario) {
    List<TarefaAntiga> resultado = new ArrayList<>();
    for (TarefaAntiga tarefa : tarefas.values()) {
        if (tarefa.getCriador().equals(usuario) && tarefa.isAtrasada()) {
            resultado.add(tarefa);
        }
    }
    return resultado;
}
}
