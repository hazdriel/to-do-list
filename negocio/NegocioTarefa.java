package negocio;

import dados.*;
import java.util.List;
import java.util.ArrayList;
import negocio.entidade.*;
import negocio.excecao.categoria.CategoriaVaziaException;
import negocio.excecao.sessao.SessaoJaInativaException;
import negocio.excecao.sessao.SessaoNulaException;
import negocio.excecao.tarefa.*;

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
          throws SessaoNulaException, RepositorioTarefasVazioException {
    if (repositorioTarefas == null) {
      throw new RepositorioTarefasVazioException();
    }
    if (sessao == null) {
      throw new SessaoNulaException();
    }
    
    this.repositorioTarefas = repositorioTarefas;
    this.sessao = sessao;
  }

  // MÉTODOS DE CRIAÇÃO DE TAREFAS
  
  public TarefaSimples criarTarefaSimples(String titulo, String descricao, Prioridade prioridade, 
                                          LocalDateTime prazo, Categoria categoria)
          throws TituloVazioException, PrioridadeVaziaException, SessaoJaInativaException, TarefaVaziaException, CriadorVazioException {
    validarParametrosCriacao(titulo, prioridade);
    verificarSessaoAtiva();
    
    TarefaSimples tarefa = new TarefaSimples(titulo, descricao, prazo, prioridade, categoria, sessao.getUsuarioLogado());
    criarTarefa(tarefa);
    return tarefa;
  }

  public TarefaDelegavel criarTarefaDelegavel(String titulo, String descricao, Prioridade prioridade, 
                                              LocalDateTime prazo, Categoria categoria, Usuario responsavel)
          throws TituloVazioException, PrioridadeVaziaException, DelegacaoResponsavelVazioException, SessaoJaInativaException, CriadorVazioException, TarefaVaziaException {
    validarParametrosCriacao(titulo, prioridade);
    validarResponsavel(responsavel);
    verificarSessaoAtiva();
    
    TarefaDelegavel tarefa = new TarefaDelegavel(titulo, descricao, prazo, prioridade, categoria, sessao.getUsuarioLogado(), responsavel);
    criarTarefa(tarefa);
    return tarefa;
  }

  public TarefaRecorrente criarTarefaRecorrente(String titulo, String descricao, Prioridade prioridade, 
                                                 LocalDateTime prazo, Categoria categoria, Usuario responsavel, java.time.Period periodicidade)
          throws TituloVazioException, PrioridadeVaziaException, RecorrentePeriodicidadeException, SessaoJaInativaException, TarefaVaziaException, CriadorVazioException, DelegacaoResponsavelVazioException {
    validarParametrosCriacao(titulo, prioridade);
    validarResponsavel(responsavel);
    validarPeriodicidade(periodicidade);
    verificarSessaoAtiva();
    
    TarefaRecorrente tarefa = new TarefaRecorrente(titulo, descricao, prazo, prioridade, categoria, sessao.getUsuarioLogado(), responsavel, periodicidade);
    criarTarefa(tarefa);
    return tarefa;
  }

  public TarefaTemporizada criarTarefaTemporizada(String titulo, String descricao, Prioridade prioridade, 
                                                  LocalDateTime prazo, Categoria categoria, java.time.Duration duracaoSessao, 
                                                  java.time.Duration duracaoPausa, int totalSessoes) 
      throws TituloVazioException, PrioridadeVaziaException, SessaoJaInativaException, TarefaVaziaException, CriadorVazioException {
    validarParametrosCriacao(titulo, prioridade);
    verificarSessaoAtiva();
    
    TarefaTemporizada tarefa = new TarefaTemporizada(titulo, descricao, prazo, prioridade, categoria, sessao.getUsuarioLogado(), duracaoSessao, duracaoPausa, totalSessoes);
    criarTarefa(tarefa);
    return tarefa;
  }
  
  private void criarTarefa(TarefaAbstrata tarefa) throws TarefaVaziaException {
    if (tarefa == null) {
      throw new TarefaVaziaException();
    }
    repositorioTarefas.inserirTarefa(tarefa);
  }

  // MÉTODOS DE LISTAGEM E CONSULTA
  
  public List<TarefaAbstrata> listarTarefas() throws SessaoJaInativaException {
    verificarSessaoAtiva();
    return repositorioTarefas.listarTarefasPorUsuario(sessao.getUsuarioLogado());
  }

  public List<TarefaAbstrata> listarPorPrioridade(Prioridade prioridade) throws SessaoJaInativaException, PrioridadeVaziaException {
    if (prioridade == null) {
      throw new PrioridadeVaziaException();
    }
    verificarSessaoAtiva();
    return repositorioTarefas.listarPorPrioridade(prioridade, sessao.getUsuarioLogado());
  }

  public List<TarefaAbstrata> listarConcluidas() throws SessaoJaInativaException {
    verificarSessaoAtiva();
    return repositorioTarefas.buscarConcluidas(sessao.getUsuarioLogado());
  }

  public List<TarefaAbstrata> listarPendentes() throws SessaoJaInativaException {
    verificarSessaoAtiva();
    return repositorioTarefas.buscarPendentes(sessao.getUsuarioLogado());
  }

  public List<TarefaAbstrata> listarAtrasadas() throws SessaoJaInativaException {
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

  public List<TarefaAbstrata> listarPorTipo(String tipo) throws SessaoJaInativaException, TipoVazioException {
    if (tipo == null || tipo.trim().isEmpty()) {
      throw new TipoVazioException();
    }
    verificarSessaoAtiva();
    return repositorioTarefas.listarPorTipo(tipo, sessao.getUsuarioLogado());
  }
  
  public List<TarefaAbstrata> listarTarefasPorCategoria(Categoria categoria) throws CategoriaVaziaException, SessaoJaInativaException {
    if (categoria == null) {
      throw new CategoriaVaziaException();
    }
    verificarSessaoAtiva();
    return repositorioTarefas.listarPorCategoria(categoria, sessao.getUsuarioLogado());
  }

  // MÉTODOS ESPECÍFICOS PARA DELEGAÇÃO
  
  public void delegarTarefa(String id, Usuario novoResponsavel) throws DelegacaoStatusInvalidoException, DelegacaoMotivoException, DelegacaoRegistroInvalidoException, DelegacaoResponsavelInvalidoException, DelegacaoResponsavelVazioException,
          TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException,
          TarefaIDNaoPertenceException, DelegacaoInvalidaException, CriadorVazioException, TarefaVaziaException {
    TarefaAbstrata tarefa = validarTarefaDoUsuario(id);
    validarResponsavel(novoResponsavel);

    if (!(tarefa instanceof Delegavel)) {
      throw new DelegacaoInvalidaException();
    }
    
    Delegavel tarefaDelegavel = (Delegavel) tarefa;
    tarefaDelegavel.delegarPara(novoResponsavel);
    try {
        repositorioTarefas.atualizarTarefa(tarefa);
    } catch (TarefaVaziaException e) {
        throw new TarefaVaziaException();
    }
  }
  
  public List<TarefaAbstrata> listarTarefasDelegadas() throws SessaoJaInativaException {
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
  
  public List<TarefaAbstrata> listarTarefasDelegadasParaUsuario() throws SessaoJaInativaException {
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
  
  public List<TarefaAbstrata> listarTarefasDoUsuario() throws SessaoJaInativaException {
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




  // MÉTODOS DE CONTROLE DE STATUS
  
  public void iniciarTarefa(String id) throws IniciacaoInvalidaException, TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException, TarefaVaziaException {
    TarefaAbstrata tarefa = validarTarefaDoUsuario(id);
    tarefa.iniciar();
    try {
        repositorioTarefas.atualizarTarefa(tarefa);
    } catch (TarefaVaziaException e) {
        throw new TarefaVaziaException();
    }
  }

  public void cancelarTarefa(String id) throws CancelamentoInvalidoException, TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException, TarefaVaziaException {
    TarefaAbstrata tarefa = validarTarefaDoUsuario(id);
    tarefa.cancelar();
    try {
        repositorioTarefas.atualizarTarefa(tarefa);
    } catch (TarefaVaziaException e) {
        throw new TarefaVaziaException();
    }
  }
  
  public void concluirTarefa(String id) throws ConclusaoInvalidaException, TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException, RecorrenteExecucaoException, AtualizarTarefaException, TarefaVaziaException {
    TarefaAbstrata tarefa = validarTarefaDoUsuario(id);
    tarefa.concluir();
    try {
        repositorioTarefas.atualizarTarefa(tarefa);
        
        if (tarefa instanceof TarefaRecorrente) {
            criarNovaTarefaRecorrente((TarefaRecorrente) tarefa);
        }
    } catch (TarefaVaziaException e) {
        throw new TarefaVaziaException();
    }
  }

  // MÉTODOS DE MODIFICAÇÃO DE TAREFAS
  
  public void atualizarTarefa(String id, String novoTitulo, String novaDescricao, 
                               Prioridade novaPrioridade, LocalDateTime novoPrazo, Categoria novaCategoria)
          throws TituloVazioException, DescricaoVaziaException, AtualizarTarefaException, PrazoPassadoException, PrazoInvalidoException, CategoriaVaziaException, TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException, TarefaVaziaException {
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

    try {
        repositorioTarefas.atualizarTarefa(tarefa);
    } catch (TarefaVaziaException e) {
        throw new TarefaVaziaException();
    }
  }
  
  public void removerTarefa(String id) throws TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException {
    validarTarefaDoUsuario(id);
    repositorioTarefas.removerTarefa(id);
  }
  
  // MÉTODOS DE VALIDAÇÃO E AUXILIARES
  
  public TarefaAbstrata buscarTarefaPorId(String id) throws TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException {
    return validarTarefaDoUsuario(id);
  }
  
  private TarefaAbstrata validarTarefaDoUsuario(String id) throws TarefaIDVazioException, TarefaIDNaoEncontradaException, SessaoJaInativaException, TarefaIDNaoPertenceException {
    if (id == null || id.trim().isEmpty()) {
      throw new TarefaIDVazioException();
    }
    
    verificarSessaoAtiva();
    
    TarefaAbstrata tarefa = repositorioTarefas.buscarTarefaPorID(id);
    if (tarefa == null) {
      throw new TarefaIDNaoEncontradaException(id);
    }

    if (!tarefa.getCriador().equals(sessao.getUsuarioLogado())) {
      throw new TarefaIDNaoPertenceException(id);
    }

    return tarefa;
  }

  // MÉTODOS DE CONVENIÊNCIA

  public boolean podeRemoverCategoria(Categoria categoria) throws CategoriaVaziaException, SessaoJaInativaException {
    if (categoria == null) {
      throw new CategoriaVaziaException();
    }
    verificarSessaoAtiva();
    List<TarefaAbstrata> tarefasComCategoria = repositorioTarefas.listarPorCategoria(categoria, sessao.getUsuarioLogado());
    return tarefasComCategoria.isEmpty();
  }

  // MÉTODOS PRIVADOS DE VALIDAÇÃO
  
  private void validarParametrosCriacao(String titulo, Prioridade prioridade) throws TituloVazioException, PrioridadeVaziaException {
    if (titulo == null || titulo.trim().isEmpty()) {
      throw new TituloVazioException();
    }
    if (prioridade == null) {
      throw new PrioridadeVaziaException();
    }
  }

  private void validarResponsavel(Usuario responsavel) throws DelegacaoResponsavelVazioException {
    if (responsavel == null) {
      throw new DelegacaoResponsavelVazioException();
    }
  }

  private void validarPeriodicidade(java.time.Period periodicidade) throws RecorrentePeriodicidadeException {
    if (periodicidade == null) {
      throw new RecorrentePeriodicidadeException();
    }
  }

  private void verificarSessaoAtiva() throws SessaoJaInativaException {
    if (!sessao.estaLogado()) {
      throw new SessaoJaInativaException();
    }
  }
  
  private void criarNovaTarefaRecorrente(TarefaRecorrente tarefaOriginal) throws TarefaVaziaException {
    try {
        LocalDateTime proximaData = LocalDateTime.now().plus(tarefaOriginal.getPeriodicidade());
        
        TarefaRecorrente novaTarefa = new TarefaRecorrente(
            tarefaOriginal.getTitulo(),
            tarefaOriginal.getDescricao(),
            proximaData,
            tarefaOriginal.getPrioridade(),
            tarefaOriginal.getCategoria(),
            tarefaOriginal.getCriador(),
            tarefaOriginal.getResponsavelOriginal(),
            tarefaOriginal.getPeriodicidade()
        );
        
        repositorioTarefas.inserirTarefa(novaTarefa);
    } catch (Exception e) {
        throw new TarefaVaziaException();
    }
  }
  
  // CONTROLE DE SESSÕES POMODORO
  
  public List<TarefaTemporizada> listarTarefasTemporizadas() {
    try {
        verificarSessaoAtiva();
        List<TarefaAbstrata> todasTarefas = repositorioTarefas.listarTarefasPorUsuario(sessao.getUsuarioLogado());
    List<TarefaTemporizada> tarefasTemporizadas = new ArrayList<>();
    
    for (TarefaAbstrata tarefa : todasTarefas) {
      if (tarefa instanceof TarefaTemporizada) {
        TarefaTemporizada tarefaTemp = (TarefaTemporizada) tarefa;
        // Só adiciona se NÃO estiver completa
        if (!tarefaTemp.isCompleta()) {
          tarefasTemporizadas.add(tarefaTemp);
        }
      }
    }
    
    return tarefasTemporizadas;
    } catch (SessaoJaInativaException e) {
        throw new IllegalStateException(e.getMessage());
    }
  }
  
  public void iniciarSessaoPomodoro(String idTarefa)  {
    TarefaTemporizada tarefa = validarTarefaTemporizada(idTarefa);
    tarefa.iniciarSessao();
    try {
        repositorioTarefas.atualizarTarefa(tarefa);
    } catch (TarefaVaziaException e) {
        throw new IllegalStateException(e.getMessage());
    }
  }
  
  public void pausarSessaoPomodoro(String idTarefa) {
    TarefaTemporizada tarefa = validarTarefaTemporizada(idTarefa);
    tarefa.pausarSessao();
    try {
        repositorioTarefas.atualizarTarefa(tarefa);
    } catch (TarefaVaziaException e) {
        throw new IllegalStateException(e.getMessage());
    }
  }
  
  public void retomarSessaoPomodoro(String idTarefa) {
    TarefaTemporizada tarefa = validarTarefaTemporizada(idTarefa);
    tarefa.retomarSessao();
    try {
        repositorioTarefas.atualizarTarefa(tarefa);
    } catch (TarefaVaziaException e) {
        throw new IllegalStateException(e.getMessage());
    }
  }
  
  public void concluirSessaoPomodoro(String idTarefa) {
    TarefaTemporizada tarefa = validarTarefaTemporizada(idTarefa);
    tarefa.concluirSessao();
    try {
        repositorioTarefas.atualizarTarefa(tarefa);
    } catch (TarefaVaziaException e) {
        throw new IllegalStateException(e.getMessage());
    }
  }
  
  private TarefaTemporizada validarTarefaTemporizada(String id) {
    try {
        TarefaAbstrata tarefa = validarTarefaDoUsuario(id);
    
    if (!(tarefa instanceof TarefaTemporizada)) {
      throw new IllegalArgumentException("Tarefa não é do tipo Temporizada");
    }
    
    return (TarefaTemporizada) tarefa;
    } catch (Exception e) {
        throw new IllegalArgumentException(e.getMessage());
    }
  }
}