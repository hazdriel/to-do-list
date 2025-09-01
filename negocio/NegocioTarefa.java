package negocio;

import dados.*;
import java.util.List;
import java.util.ArrayList;
import negocio.entidade.*;
import java.time.LocalDateTime;

/**
 * Classe responsável pela lógica de negócio relacionada às tarefas.
 * Gerencia criação, modificação, listagem e controle de status das tarefas,
 * garantindo validações adequadas e tratamento de exceções.
 */
public class NegocioTarefa {
  private final RepositorioTarefas repositorioTarefas;
  private final NegocioSessao sessao;

  public NegocioTarefa(RepositorioTarefas repositorioTarefas, NegocioSessao sessao) 
      throws IllegalArgumentException {
    if (repositorioTarefas == null) {
      throw new IllegalArgumentException("Repositório de tarefas não pode ser nulo");
    }
    if (sessao == null) {
      throw new IllegalArgumentException("Sessão não pode ser nula");
    }
    
    this.repositorioTarefas = repositorioTarefas;
    this.sessao = sessao;
  }

  // MÉTODOS DE CRIAÇÃO DE TAREFAS
  
  public TarefaSimples criarTarefaSimples(String titulo, String descricao, Prioridade prioridade, 
                                          LocalDateTime prazo, Categoria categoria) 
      throws IllegalArgumentException, IllegalStateException {
    validarParametrosCriacao(titulo, prioridade);
    verificarSessaoAtiva();
    
    TarefaSimples tarefa = new TarefaSimples(titulo, descricao, prazo, prioridade, categoria, sessao.getUsuarioLogado());
    criarTarefa(tarefa);
    return tarefa;
  }

  public TarefaDelegavel criarTarefaDelegavel(String titulo, String descricao, Prioridade prioridade, 
                                              LocalDateTime prazo, Categoria categoria, Usuario responsavel) 
      throws IllegalArgumentException, IllegalStateException {
    validarParametrosCriacao(titulo, prioridade);
    validarResponsavel(responsavel);
    verificarSessaoAtiva();
    
    TarefaDelegavel tarefa = new TarefaDelegavel(titulo, descricao, prazo, prioridade, categoria, sessao.getUsuarioLogado(), responsavel);
    criarTarefa(tarefa);
    return tarefa;
  }

  public TarefaRecorrente criarTarefaRecorrente(String titulo, String descricao, Prioridade prioridade, 
                                                 LocalDateTime prazo, Categoria categoria, java.time.Period periodicidade) 
      throws IllegalArgumentException, IllegalStateException {
    validarParametrosCriacao(titulo, prioridade);
    validarPeriodicidade(periodicidade);
    verificarSessaoAtiva();
    
    TarefaRecorrente tarefa = new TarefaRecorrente(titulo, descricao, prazo, prioridade, categoria, sessao.getUsuarioLogado(), periodicidade);
    criarTarefa(tarefa);
    return tarefa;
  }

  public TarefaTemporizada criarTarefaTemporizada(String titulo, String descricao, Prioridade prioridade, 
                                                  LocalDateTime prazo, Categoria categoria, LocalDateTime prazoFinal, 
                                                  java.time.Duration estimativa) 
      throws IllegalArgumentException, IllegalStateException {
    validarParametrosCriacao(titulo, prioridade);
    validarParametrosTemporizacao(prazoFinal, estimativa);
    verificarSessaoAtiva();
    
    TarefaTemporizada tarefa = new TarefaTemporizada(titulo, descricao, prazo, prioridade, categoria, sessao.getUsuarioLogado(), prazoFinal, estimativa);
    criarTarefa(tarefa);
    return tarefa;
  }
  
  private void criarTarefa(TarefaAbstrata tarefa) throws IllegalArgumentException {
    if (tarefa == null) {
      throw new IllegalArgumentException("Tarefa não pode ser nula");
    }
    repositorioTarefas.inserirTarefa(tarefa);
  }

  // MÉTODOS DE LISTAGEM E CONSULTA
  
  public List<TarefaAbstrata> listarTarefas() throws IllegalStateException {
    verificarSessaoAtiva();
    return repositorioTarefas.listarTarefasPorUsuario(sessao.getUsuarioLogado());
  }

  public List<TarefaAbstrata> listarPorPrioridade(Prioridade prioridade) throws IllegalArgumentException, IllegalStateException {
    if (prioridade == null) {
      throw new IllegalArgumentException("Prioridade não pode ser nula");
    }
    verificarSessaoAtiva();
    return repositorioTarefas.listarPorPrioridade(prioridade, sessao.getUsuarioLogado());
  }

  public List<TarefaAbstrata> listarConcluidas() throws IllegalStateException {
    verificarSessaoAtiva();
    return repositorioTarefas.buscarConcluidas(sessao.getUsuarioLogado());
  }

  public List<TarefaAbstrata> listarPendentes() throws IllegalStateException {
    verificarSessaoAtiva();
    return repositorioTarefas.buscarPendentes(sessao.getUsuarioLogado());
  }

  public List<TarefaAbstrata> listarAtrasadas() throws IllegalStateException {
    verificarSessaoAtiva();
    List<TarefaAbstrata> pendentes = repositorioTarefas.buscarPendentes(sessao.getUsuarioLogado());
    
    List<TarefaAbstrata> atrasadas = new ArrayList<>();
    LocalDateTime agora = LocalDateTime.now();
    
    for (TarefaAbstrata tarefa : pendentes) {
      if (tarefa.getPrazo() != null && tarefa.getPrazo().isBefore(agora)) {
        atrasadas.add(tarefa);
      }
    }
    
    return atrasadas;
  }

  public List<TarefaAbstrata> listarPorTipo(String tipo) throws IllegalArgumentException, IllegalStateException {
    if (tipo == null || tipo.trim().isEmpty()) {
      throw new IllegalArgumentException("Tipo não pode ser nulo ou vazio");
    }
    verificarSessaoAtiva();
    return repositorioTarefas.listarPorTipo(tipo, sessao.getUsuarioLogado());
  }
  
  public List<TarefaAbstrata> listarTarefasPorCategoria(Categoria categoria) throws IllegalArgumentException, IllegalStateException {
    if (categoria == null) {
      throw new IllegalArgumentException("Categoria não pode ser nula");
    }
    verificarSessaoAtiva();
    return repositorioTarefas.listarPorCategoria(categoria, sessao.getUsuarioLogado());
  }

  // MÉTODOS ESPECÍFICOS PARA DELEGAÇÃO
  
  public void delegarTarefa(String id, Usuario novoResponsavel) throws IllegalArgumentException, IllegalStateException {
    TarefaAbstrata tarefa = validarTarefaDoUsuario(id);
    validarResponsavel(novoResponsavel);

    if (!(tarefa instanceof Delegavel)) {
      throw new IllegalStateException("Tarefa não pode ser delegada");
    }
    
    Delegavel tarefaDelegavel = (Delegavel) tarefa;
    tarefaDelegavel.delegarPara(novoResponsavel);
    repositorioTarefas.atualizarTarefa(tarefa);
  }
  
  public List<TarefaAbstrata> listarTarefasDelegadas() throws IllegalStateException {
    verificarSessaoAtiva();
    Usuario usuarioLogado = sessao.getUsuarioLogado();
    List<TarefaAbstrata> todasTarefas = repositorioTarefas.listarTarefasPorUsuario(usuarioLogado);
    
    List<TarefaAbstrata> delegadas = new ArrayList<>();
    for (TarefaAbstrata tarefa : todasTarefas) {
      if (tarefa instanceof Delegavel) {
        Delegavel tarefaDelegavel = (Delegavel) tarefa;
        if (tarefaDelegavel.getResponsaveis().size() > 1) {
          delegadas.add(tarefa);
        }
      }
    }
    return delegadas;
  }
  
  public List<TarefaAbstrata> listarTarefasDelegadasParaUsuario() throws IllegalStateException {
    verificarSessaoAtiva();
    Usuario usuarioLogado = sessao.getUsuarioLogado();
    List<TarefaAbstrata> todasTarefas = repositorioTarefas.listarTodas();
    
    List<TarefaAbstrata> delegadasParaMim = new ArrayList<>();
    for (TarefaAbstrata tarefa : todasTarefas) {
      if (tarefa instanceof Delegavel) {
        Delegavel tarefaDelegavel = (Delegavel) tarefa;
        if (tarefaDelegavel.ehResponsavel(usuarioLogado) && !tarefa.getCriador().equals(usuarioLogado)) {
          delegadasParaMim.add(tarefa);
        }
      }
    }
    return delegadasParaMim;
  }
  
  public List<TarefaAbstrata> listarTarefasDoUsuario() throws IllegalStateException {
    verificarSessaoAtiva();
    Usuario usuarioLogado = sessao.getUsuarioLogado();
    List<TarefaAbstrata> todasTarefas = repositorioTarefas.listarTodas();
    
    List<TarefaAbstrata> minhasTarefas = new ArrayList<>();
    for (TarefaAbstrata tarefa : todasTarefas) {
      if (tarefa instanceof Delegavel) {
        Delegavel tarefaDelegavel = (Delegavel) tarefa;
        if (tarefaDelegavel.ehResponsavel(usuarioLogado)) {
          minhasTarefas.add(tarefa);
        }
      }
    }
    return minhasTarefas;
  }

  // MÉTODOS ESPECÍFICOS PARA TEMPORIZAÇÃO
  
  public void registrarTrabalho(String id, java.time.Duration duracao) throws IllegalArgumentException, IllegalStateException {
    TarefaAbstrata tarefa = validarTarefaDoUsuario(id);
    
    if (!(tarefa instanceof TarefaTemporizada)) {
      throw new IllegalStateException("Tarefa não é temporizada");
    }
    
    if (duracao == null || duracao.isNegative() || duracao.isZero()) {
      throw new IllegalArgumentException("Duração deve ser positiva");
    }

    TarefaTemporizada tarefaTemp = (TarefaTemporizada) tarefa;
    tarefaTemp.registrarTrabalho(duracao);
    repositorioTarefas.atualizarTarefa(tarefa);
  }

  // MÉTODOS DE CONTROLE DE STATUS
  
  public void iniciarTarefa(String id) throws IllegalArgumentException, IllegalStateException {
    TarefaAbstrata tarefa = validarTarefaDoUsuario(id);
    tarefa.iniciar();
    repositorioTarefas.atualizarTarefa(tarefa);
  }

  public void cancelarTarefa(String id) throws IllegalArgumentException, IllegalStateException {
    TarefaAbstrata tarefa = validarTarefaDoUsuario(id);
    tarefa.cancelar();
    repositorioTarefas.atualizarTarefa(tarefa);
  }
  
  public void concluirTarefa(String id) throws IllegalArgumentException, IllegalStateException {
    TarefaAbstrata tarefa = validarTarefaDoUsuario(id);
    tarefa.concluir();
    repositorioTarefas.atualizarTarefa(tarefa);
  }

  // MÉTODOS DE MODIFICAÇÃO DE TAREFAS
  
  public void atualizarTarefa(String id, String novoTitulo, String novaDescricao, 
                               Prioridade novaPrioridade, LocalDateTime novoPrazo, Categoria novaCategoria) 
      throws IllegalArgumentException, IllegalStateException {
    TarefaAbstrata tarefa = validarTarefaDoUsuario(id);

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

    if (novaCategoria != null) {
        tarefa.setCategoria(novaCategoria);
    }

    repositorioTarefas.atualizarTarefa(tarefa);
  }
  
  public void removerTarefa(String id) throws IllegalArgumentException, IllegalStateException {
    validarTarefaDoUsuario(id);
    repositorioTarefas.removerTarefa(id);
  }
  
  // MÉTODOS DE VALIDAÇÃO E AUXILIARES
  
  public TarefaAbstrata buscarTarefaPorId(String id) throws IllegalArgumentException, IllegalStateException {
    return validarTarefaDoUsuario(id);
  }
  
  private TarefaAbstrata validarTarefaDoUsuario(String id) throws IllegalArgumentException, IllegalStateException {
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("ID da tarefa não pode ser nulo ou vazio");
    }
    
    verificarSessaoAtiva();
    
    TarefaAbstrata tarefa = repositorioTarefas.buscarTarefaPorID(id);
    if (tarefa == null) {
      throw new IllegalArgumentException("Tarefa com ID '" + id + "' não encontrada");
    }

    if (!tarefa.getCriador().equals(sessao.getUsuarioLogado())) {
      throw new IllegalStateException("Tarefa não pertence ao usuário logado");
    }

    return tarefa;
  }

  // MÉTODOS DE CONVENIÊNCIA

  public boolean podeRemoverCategoria(Categoria categoria) throws IllegalArgumentException, IllegalStateException {
    if (categoria == null) {
      throw new IllegalArgumentException("Categoria não pode ser nula");
    }
    verificarSessaoAtiva();
    List<TarefaAbstrata> tarefasComCategoria = repositorioTarefas.listarPorCategoria(categoria, sessao.getUsuarioLogado());
    return tarefasComCategoria.isEmpty();
  }

  // MÉTODOS PRIVADOS DE VALIDAÇÃO
  
  private void validarParametrosCriacao(String titulo, Prioridade prioridade) throws IllegalArgumentException {
    if (titulo == null || titulo.trim().isEmpty()) {
      throw new IllegalArgumentException("Título não pode ser nulo ou vazio");
    }
    if (prioridade == null) {
      throw new IllegalArgumentException("Prioridade não pode ser nula");
    }
  }

  private void validarResponsavel(Usuario responsavel) throws IllegalArgumentException {
    if (responsavel == null) {
      throw new IllegalArgumentException("Responsável não pode ser nulo");
    }
  }

  private void validarPeriodicidade(java.time.Period periodicidade) throws IllegalArgumentException {
    if (periodicidade == null) {
      throw new IllegalArgumentException("Periodicidade não pode ser nula");
    }
  }

  private void validarParametrosTemporizacao(LocalDateTime prazoFinal, java.time.Duration estimativa) throws IllegalArgumentException {
    if (prazoFinal == null) {
      throw new IllegalArgumentException("Prazo final não pode ser nulo");
    }
    if (estimativa == null || estimativa.isNegative() || estimativa.isZero()) {
      throw new IllegalArgumentException("Estimativa deve ser positiva");
    }
  }

  private void verificarSessaoAtiva() throws IllegalStateException {
    if (!sessao.estaLogado()) {
      throw new IllegalStateException("Usuário deve estar logado");
    }
  }
}