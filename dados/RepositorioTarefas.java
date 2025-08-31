package dados;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import negocio.entidade.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RepositorioTarefas {
  private Map<String, TarefaAbstrata> tarefas = new HashMap<>();

  public void inserirTarefa(TarefaAbstrata tarefa) {
    tarefas.put(tarefa.getId(), tarefa);
  }

  public TarefaAbstrata buscarTarefaPorID(String id) {
    return tarefas.get(id);
  }

  public List<TarefaAbstrata> listarTarefasPorUsuario(Usuario usuario) {
    List<TarefaAbstrata> resultado = new ArrayList<>();
    for (TarefaAbstrata tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario)) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

  public boolean atualizarTarefa(TarefaAbstrata tarefaAtualizada) {
    if (tarefas.containsKey(tarefaAtualizada.getId())) {
      tarefas.put(tarefaAtualizada.getId(), tarefaAtualizada);
      return true;
    }
    return false;
  }

  public boolean remover(String id) {
    return tarefas.remove(id) != null;
  }

  public List<TarefaAbstrata> buscarPorPrioridade(Usuario usuario, Prioridade prioridade) {
    List<TarefaAbstrata> resultado = new ArrayList<>();
    for (TarefaAbstrata tarefa : tarefas.values()) {
        if (tarefa.getCriador().equals(usuario) && tarefa.getPrioridade() == prioridade) {
            resultado.add(tarefa);
        }
    }
    return resultado;
  }

  public List<TarefaAbstrata> buscarConcluidas(Usuario usuario) {
    List<TarefaAbstrata> resultado = new ArrayList<>();
    for (TarefaAbstrata tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario) && tarefa.estaConcluida()) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

   public List<TarefaAbstrata> buscarPendentes(Usuario usuario) {
    List<TarefaAbstrata> resultado = new ArrayList<>();
    for (TarefaAbstrata tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario) && tarefa.estaPendente()) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

  public List<TarefaAbstrata> buscarAtrasadas(Usuario usuario) {
    List<TarefaAbstrata> resultado = new ArrayList<>();
    for (TarefaAbstrata tarefa : tarefas.values()) {
        if (tarefa.getCriador().equals(usuario) && tarefa.estaAtrasada()) {
            resultado.add(tarefa);
        }
    }
    return resultado;
  }

  // Novos métodos para as funcionalidades específicas
  public List<TarefaAbstrata> buscarPorTipo(Usuario usuario, String tipo) {
    List<TarefaAbstrata> resultado = new ArrayList<>();
    for (TarefaAbstrata tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario) && tarefa.getTipo().equals(tipo)) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

  public List<TarefaAbstrata> buscarDelegadasParaUsuario(Usuario usuario) {
    List<TarefaAbstrata> resultado = new ArrayList<>();
    for (TarefaAbstrata tarefa : tarefas.values()) {
      if (tarefa.getResponsavel().equals(usuario) && !tarefa.getCriador().equals(usuario)) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

  public List<TarefaAbstrata> buscarPorCategoria(Usuario usuario, Categoria categoria) {
    List<TarefaAbstrata> resultado = new ArrayList<>();
    for (TarefaAbstrata tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario) && tarefa.getCategoria() == categoria) {
        resultado.add(tarefa);
      }
    }
    return resultado;
  }

  public List<Categoria> buscarCategoriasDoUsuario(Usuario usuario) {
    Set<Categoria> categorias = new HashSet<>();
    for (TarefaAbstrata tarefa : tarefas.values()) {
      if (tarefa.getCriador().equals(usuario) && tarefa.getCategoria() != null) {
        categorias.add(tarefa.getCategoria());
      }
    }
    return new ArrayList<>(categorias);
  }
}
