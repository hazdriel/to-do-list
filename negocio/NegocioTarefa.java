package negocio;

import dados.*;

import java.util.List;

import dados.RepositorioTarefas;
import dados.RepositorioUsuarios;
import negocio.entidade.*;

import java.time.LocalDate;

public class NegocioTarefa {
  private RepositorioTarefas repositorioTarefas;
  private RepositorioUsuarios repositorioUsuarios;
  private NegocioSessao sessao;

  public NegocioTarefa(RepositorioTarefas repositorioTarefas, RepositorioUsuarios repositorioUsuarios, NegocioSessao sessao) {
    this.repositorioUsuarios = repositorioUsuarios;
    this.repositorioTarefas = repositorioTarefas;
    this.sessao = sessao;
  }

  public void criarTarefa(String titulo, String descricao, Prioridade prioridade, LocalDate prazo) {
    if (titulo == null || titulo.trim().isEmpty()) {
      System.out.println("O título não pode ser vazio.");
      return;
    }

    if (descricao == null || descricao.trim().isEmpty()) {
      System.out.println("A descrição não pode ser vazia.");
      return;
    }

    TarefaAntiga novaTarefa = new TarefaAntiga(titulo, descricao, sessao.getUsuarioLogado());
    novaTarefa.setPrioridade(prioridade != null ? prioridade : Prioridade.BAIXA);
    novaTarefa.setPrazo(prazo);

    repositorioTarefas.inserirTarefa(novaTarefa);
    System.out.println("Tarefa criada com sucesso!");
  }

  public List<TarefaAntiga> listarTarefas() {
    Usuario usuario = sessao.getUsuarioLogado();
    return repositorioTarefas.listarTarefasPorUsuario(usuario);
  }

  public List<TarefaAntiga> listarPorPrioridade(Prioridade prioridade) {
    Usuario usuario = sessao.getUsuarioLogado();
    return repositorioTarefas.buscarPorPrioridade(usuario, prioridade);
  }

  public List<TarefaAntiga> listarConcluidas() {
    Usuario usuario = sessao.getUsuarioLogado();
    return repositorioTarefas.buscarConcluidas(usuario);
  }

  public List<TarefaAntiga> listarPendentes() {
    Usuario usuario = sessao.getUsuarioLogado();
    return repositorioTarefas.buscarPendentes(usuario);
  }

  public List<TarefaAntiga> listarAtrasadas() {
    Usuario usuario = sessao.getUsuarioLogado();
    return repositorioTarefas.buscarAtrasadas(usuario);
  }

  private TarefaAntiga validarTarefaDoUsuario(String id) {
    TarefaAntiga tarefa = repositorioTarefas.buscarTarefaPorID(id);

    if (tarefa == null) {
        System.out.println("Tarefa não encontrada.");
        return null;
    }

    if (!tarefa.getCriador().equals(sessao.getUsuarioLogado())) {
        System.out.println("Você não tem permissão para acessar esta tarefa.");
        return null;
    }

    return tarefa;
  }

  public TarefaAntiga buscarTarefaPorId(String id) {
    return validarTarefaDoUsuario(id);
  }

  public boolean removerTarefa(String id) {
    TarefaAntiga tarefa = validarTarefaDoUsuario(id);
    if (tarefa == null) return false;

    repositorioTarefas.remover(id);
    return true;
  }

  public boolean concluirTarefa(String id) {
    TarefaAntiga tarefa = validarTarefaDoUsuario(id);
    if (tarefa == null) return false;

    tarefa.concluirTarefa();
    return true;
  }

  public boolean atualizarTarefa(String id, String novoTitulo, String novaDescricao, Prioridade novaPrioridade, LocalDate novoPrazo) {
    TarefaAntiga tarefa = validarTarefaDoUsuario(id);
    if (tarefa == null) return false;

    if (novoTitulo != null && !novoTitulo.trim().isEmpty()) {
        tarefa.setTitulo(novoTitulo);
    }

    if (novaDescricao != null && !novaDescricao.trim().isEmpty()) {
        tarefa.setDescricao(novaDescricao);
    }

    if (novaPrioridade != null) {
        tarefa.setPrioridade(novaPrioridade);
    }

    if (novoPrazo != null) {
        tarefa.setPrazo(novoPrazo);
    }

    System.out.println("Tarefa atualizada com sucesso!");
    repositorioTarefas.atualizarTarefa(tarefa);
    return true;
  }
}
