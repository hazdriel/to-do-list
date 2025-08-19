package negocio;

import java.util.List;

import dados.RepositorioTarefas;
import dados.RepositorioUsuarios;
import negocio.entidade.*;
import negocio.excecao.tarefa.*;

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

  public void criarTarefa(String titulo, String descricao, Prioridade prioridade, LocalDate prazo, Categoria categoria) throws TituloVazioException, CategoriaVaziaException, PrazoInvalidoException {
    Usuario criador = sessao.getUsuarioLogado();

    Tarefa.validarTitulo(titulo);
    Tarefa.validarCategoria(categoria);
    TarefaAntiga novaTarefa = new TarefaAntiga(titulo, descricao, prazo, prioridade, categoria, criador);

    repositorioTarefas.inserirTarefa(novaTarefa);
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

  private TarefaAntiga validarTarefaDoUsuarioID(String id) throws TarefaIDNaoEncontradaException, TarefaIDNaoPertece {
    TarefaAntiga tarefa = repositorioTarefas.buscarTarefaPorID(id);
    if (tarefa == null) {
        throw new TarefaIDNaoEncontradaException(id);
    }

    if (!tarefa.getCriador().equals(sessao.getUsuarioLogado())) {
        throw new TarefaIDNaoPertece(id);
    }
    return tarefa;
  }

  public TarefaAntiga buscarTarefaPorId(String id) throws TarefaIDNaoPertece, TarefaIDNaoEncontradaException {
    return validarTarefaDoUsuarioID(id);
  }

  private TarefaAntiga validarTarefaDoUsuarioTitulo(String titulo) throws TarefaTituloNaoEncontradaException {
    TarefaAntiga tarefa = repositorioTarefas.buscarTarefaPorTitulo(titulo);
    if (tarefa == null) {
      throw new TarefaTituloNaoEncontradaException(titulo);
    }

    return tarefa;
  }

  public TarefaAntiga buscarTarefaPorTitulo(String id) throws TarefaTituloNaoEncontradaException {
    return validarTarefaDoUsuarioTitulo(id);
  }

  public boolean removerTarefa(String id) throws TarefaIDNaoPertece, TarefaIDNaoEncontradaException {
    TarefaAntiga tarefa = validarTarefaDoUsuarioID(id);
    repositorioTarefas.remover(id);
    return true;
  }

  public boolean concluirTarefa(String id) throws TarefaIDNaoPertece, TarefaIDNaoEncontradaException {
    TarefaAntiga tarefa = validarTarefaDoUsuarioID(id);
    tarefa.concluirTarefa();
    return true;
  }

  public boolean atualizarTarefa(String id, String novoTitulo, String novaDescricao, Categoria novaCategoria, Prioridade novaPrioridade, LocalDate novoPrazo)
          throws TarefaIDNaoPertece, TarefaIDNaoEncontradaException, TituloVazioException, CategoriaVaziaException, PrazoInvalidoException {
    TarefaAntiga tarefa = validarTarefaDoUsuarioID(id);

    if (novoTitulo != null) tarefa.setTitulo(novoTitulo);
    if (novaDescricao != null) tarefa.setDescricao(novaDescricao);
    if (novaCategoria != null) tarefa.setCategoria(novaCategoria);
    if (novaPrioridade != null) tarefa.setPrioridade(novaPrioridade);
    if (novoPrazo != null) tarefa.setPrazo(novoPrazo);

    repositorioTarefas.atualizarTarefa(tarefa);
    return true;
  }

}
