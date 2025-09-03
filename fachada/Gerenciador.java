package fachada;

import dados.RepositorioTarefas;
import dados.RepositorioUsuarios;
import dados.RepositorioCategorias;
import negocio.NegocioTarefa;
import negocio.NegocioUsuario;
import negocio.NegocioSessao;
import negocio.CalculadoraEstatisticas;
import negocio.DadosEstatisticos;
import negocio.NegocioCategoria;
import negocio.entidade.*;
import negocio.excecao.categoria.*;
import negocio.excecao.sessao.*;
import negocio.excecao.tarefa.*;
import negocio.excecao.usuario.*;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.Map;

/**
 * Fachada que coordena as operações entre as camadas do sistema.
 * Responsável apenas por orquestrar chamadas, NÃO trata exceções.
 */
public class Gerenciador {

    private RepositorioTarefas repositorioTarefas;
    private RepositorioUsuarios repositorioUsuarios;
    private RepositorioCategorias repositorioCategorias;

    private NegocioSessao negocioSessao;
    private NegocioUsuario negocioUsuario;
    private NegocioTarefa negocioTarefa;
    private CalculadoraEstatisticas calculadoraEstatisticas;
    private NegocioCategoria negocioCategoria;

    public Gerenciador() throws TarefaException, UsuarioException, CategoriaException, SessaoException {

        this.repositorioTarefas = new RepositorioTarefas();
        this.repositorioUsuarios = new RepositorioUsuarios();
        this.repositorioCategorias = new RepositorioCategorias();

        this.negocioUsuario = new NegocioUsuario(repositorioUsuarios);
        this.negocioSessao = new NegocioSessao(negocioUsuario);
        this.negocioTarefa = new NegocioTarefa(repositorioTarefas, negocioSessao);
        this.calculadoraEstatisticas = new CalculadoraEstatisticas(repositorioTarefas);
        this.negocioCategoria = new NegocioCategoria(repositorioCategorias, negocioSessao, negocioTarefa);

        negocioCategoria.garantirCategoriasPadrao();
    }

    // ========================================
    // GERENCIAMENTO DE SESSÃO E AUTENTICAÇÃO
    // ========================================
    
    public boolean fazerLogin(String email, String senha) throws LoginJaAtivoException, EmailVazioException, SenhaVaziaException, UsuarioVazioException {
        return negocioSessao.autenticar(email, senha);
    }

    public void cadastrarUsuario(String nome, String email, String senha) 
            throws NomeVazioException, EmailVazioException, SenhaVaziaException, 
                   UsuarioExistenteException, SenhaTamanhoInvalidoException, 
                   NomeApenasLetrasException, NomeTamanhoInvalidoException, 
                   EmailFormatoInvalidoException, UsuarioVazioException {
        negocioUsuario.cadastrarUsuario(nome, email, senha);
    }

    public boolean estaLogado() {
        return negocioSessao.estaLogado();
    }

    public Usuario getUsuarioLogado() throws SessaoJaInativaException {
        return negocioSessao.getUsuarioLogado();
    }

    public void fazerLogout() throws SessaoJaInativaException {
        negocioSessao.logout();
    }

    public void alterarSenha(String senhaAtual, String novaSenha) 
            throws SenhaIncorretaException, SenhaVaziaException, SenhaTamanhoInvalidoException, 
                   SessaoJaInativaException, UsuarioVazioException {
        negocioSessao.alterarSenhaUsuarioLogado(senhaAtual, novaSenha);
    }

    public void excluirConta(String senhaConfirmacao) 
            throws SenhaIncorretaException, SessaoJaInativaException, SenhaVaziaException, UsuarioVazioException {
        negocioSessao.excluirContaUsuarioLogado(senhaConfirmacao);
    }

    public String solicitarRecuperacaoSenha(String email) 
            throws EmailVazioException, UsuarioNaoEncontradoException {
        return negocioUsuario.solicitarRecuperacaoSenha(email);
    }

    public void recuperarSenha(String email, String codigo, String novaSenha) 
            throws EmailVazioException, UsuarioNaoEncontradoException, 
                   CodigoInvalidoException, SenhaVaziaException, SenhaTamanhoInvalidoException, UsuarioVazioException {
        negocioUsuario.recuperarSenha(email, codigo, novaSenha);
    }

    // ========================================
    // CRIAÇÃO DE TAREFAS
    // ========================================
    
    public void criarTarefaSimples(String titulo, String descricao, Prioridade prioridade, 
                                   LocalDateTime prazo, Categoria categoria) 
            throws TituloVazioException, PrioridadeVaziaException, SessaoJaInativaException, 
                   TarefaVaziaException, CriadorVazioException {
        negocioTarefa.criarTarefaSimples(titulo, descricao, prioridade, prazo, categoria);
    }

    public void criarTarefaDelegavel(String titulo, String descricao, Prioridade prioridade, 
                                     LocalDateTime prazo, Categoria categoria, Usuario responsavel) 
            throws TituloVazioException, PrioridadeVaziaException, SessaoJaInativaException, 
                   TarefaVaziaException, CriadorVazioException, DelegacaoResponsavelVazioException {
        negocioTarefa.criarTarefaDelegavel(titulo, descricao, prioridade, prazo, categoria, responsavel);
    }

    public void criarTarefaRecorrente(String titulo, String descricao, Prioridade prioridade, 
                                      LocalDateTime prazo, Categoria categoria, Period periodicidade) 
            throws TituloVazioException, PrioridadeVaziaException, SessaoJaInativaException, 
                   TarefaVaziaException, CriadorVazioException, RecorrentePeriodicidadeException {
        negocioTarefa.criarTarefaRecorrente(titulo, descricao, prioridade, prazo, categoria, periodicidade);
    }

    public void criarTarefaTemporizada(String titulo, String descricao, Prioridade prioridade, 
                                       LocalDateTime prazo, Categoria categoria, Duration duracaoSessao, 
                                       Duration duracaoPausa, int totalSessoes) 
            throws TituloVazioException, PrioridadeVaziaException, SessaoJaInativaException, 
                   TarefaVaziaException, CriadorVazioException {
        negocioTarefa.criarTarefaTemporizada(titulo, descricao, prioridade, prazo, categoria, duracaoSessao, duracaoPausa, totalSessoes);
    }

    // ========================================
    // CONSULTA E LISTAGEM DE TAREFAS
    // ========================================
    
    public List<TarefaAbstrata> listarTarefas() throws SessaoJaInativaException {
        return negocioTarefa.listarTarefas();
    }

    public TarefaAbstrata buscarTarefaPorId(String id) 
            throws TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, 
                   TarefaIDNaoPertenceException {
        if (id == null || id.trim().isEmpty()) {
            throw new TarefaIDVazioException();
        }
        return negocioTarefa.buscarTarefaPorId(id);
    }

    // Listagens por critério
    public List<TarefaAbstrata> listarPorPrioridade(Prioridade prioridade) 
            throws SessaoJaInativaException, PrioridadeVaziaException {
        return negocioTarefa.listarPorPrioridade(prioridade);
    }

    public List<TarefaAbstrata> listarPorCategoria(Categoria categoria) 
            throws SessaoJaInativaException, CategoriaVaziaException {
        return negocioTarefa.listarTarefasPorCategoria(categoria);
    }

    public List<TarefaAbstrata> listarPorTipo(String tipo) 
            throws SessaoJaInativaException, TipoVazioException {
        return negocioTarefa.listarPorTipo(tipo);
    }

    // Listagens por status
    public List<TarefaAbstrata> listarConcluidas() throws SessaoJaInativaException {
        return negocioTarefa.listarConcluidas();
    }

    public List<TarefaAbstrata> listarPendentes() throws SessaoJaInativaException {
        return negocioTarefa.listarPendentes();
    }

    public List<TarefaAbstrata> listarAtrasadas() throws SessaoJaInativaException {
        return negocioTarefa.listarAtrasadas();
    }

    // Listagens relacionadas a delegação
    public List<TarefaAbstrata> listarTarefasDelegadas() throws SessaoJaInativaException {
        return negocioTarefa.listarTarefasDelegadas();
    }
    
    public List<TarefaAbstrata> listarTarefasDelegadasParaUsuario() throws SessaoJaInativaException {
        return negocioTarefa.listarTarefasDelegadasParaUsuario();
    }
    
    public List<TarefaAbstrata> listarTarefasDoUsuario() throws SessaoJaInativaException {
        return negocioTarefa.listarTarefasDoUsuario();
    }
    
    public List<TarefaAbstrata> listarTarefasDelegadasPeloUsuario() throws SessaoJaInativaException {
        return negocioTarefa.listarTarefasDelegadas();
    }

    // ========================================
    // MODIFICAÇÃO DE TAREFAS
    // ========================================
    
    public void atualizarTarefa(String id, String novoTitulo, String novaDescricao, 
                               Prioridade novaPrioridade, LocalDateTime novoPrazo, Categoria novaCategoria) 
            throws TituloVazioException, DescricaoVaziaException, AtualizarTarefaException, 
                   PrazoPassadoException, PrazoInvalidoException, CategoriaVaziaException, 
                   TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, 
                   TarefaIDNaoPertenceException, TarefaVaziaException {
        if (id == null || id.trim().isEmpty()) {
            throw new TarefaIDVazioException();
        }
        negocioTarefa.atualizarTarefa(id, novoTitulo, novaDescricao, novaPrioridade, novoPrazo, novaCategoria);
    }

    public void removerTarefa(String id) 
            throws TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, 
                   TarefaIDNaoPertenceException {
        if (id == null || id.trim().isEmpty()) {
            throw new TarefaIDVazioException();
        }
        negocioTarefa.removerTarefa(id);
    }

    public boolean salvarTarefa(TarefaAbstrata tarefa) throws TituloVazioException, DescricaoVaziaException, 
            AtualizarTarefaException, PrazoPassadoException, PrazoInvalidoException, CategoriaVaziaException, 
            TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, 
            TarefaIDNaoPertenceException, TarefaVaziaException {
        if (tarefa == null) return false;
        atualizarTarefa(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getPrioridade(), tarefa.getPrazo(), tarefa.getCategoria());
        return true;
    }

    // ========================================
    // CONTROLE DE STATUS DE TAREFAS
    // ========================================
    
    public void concluirTarefa(String id) 
            throws ConclusaoInvalidaException, TarefaIDVazioException, SessaoJaInativaException, 
                   TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException, 
                   RecorrenteExecucaoException, AtualizarTarefaException, TarefaVaziaException {
        negocioTarefa.concluirTarefa(id);
    }

    public void iniciarTarefa(String id) 
            throws IniciacaoInvalidaException, TarefaIDVazioException, SessaoJaInativaException, 
                   TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException, TarefaVaziaException {
        negocioTarefa.iniciarTarefa(id);
    }

    public void cancelarTarefa(String id) 
            throws CancelamentoInvalidoException, TarefaIDVazioException, SessaoJaInativaException, 
                   TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException, TarefaVaziaException {
        negocioTarefa.cancelarTarefa(id);
    }

    public void delegarTarefa(String id, Usuario novoResponsavel) 
            throws DelegacaoStatusInvalidoException, DelegacaoMotivoException, DelegacaoRegistroInvalidoException, 
                   DelegacaoResponsavelInvalidoException, DelegacaoResponsavelVazioException,
                   TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException,
                   TarefaIDNaoPertenceException, DelegacaoInvalidaException, CriadorVazioException, TarefaVaziaException {
        negocioTarefa.delegarTarefa(id, novoResponsavel);
    }


    // ========================================
    // GERENCIAMENTO DE USUÁRIOS
    // ========================================
    
    public List<Usuario> listarUsuarios() {
        return negocioUsuario.listarTodos();
    }
    
    public Usuario buscarUsuarioPorId(String id) 
            throws IDUsuarioVazio, UsuarioNaoEncontradoException {
        return negocioUsuario.buscarUsuarioPorId(id);
    }
    
    public Usuario buscarUsuarioPorEmail(String email) 
            throws EmailVazioException, UsuarioNaoEncontradoException {
        return negocioUsuario.buscarUsuarioPorEmail(email);
    }

    // ========================================
    // GERENCIAMENTO DE CATEGORIAS
    // ========================================
  
    public List<Categoria> listarCategorias() throws SessaoJaInativaException {
        return negocioCategoria.listarCategoriasDoUsuario();
    }
    
    public Categoria criarCategoria(String nome) 
            throws CategoriaVaziaException, SessaoJaInativaException, CriadorVazioException {
        return negocioCategoria.criarCategoria(nome);
    }

    public void removerCategoria(String nomeCategoria) 
            throws CategoriaVaziaException, CategoriaNaoEncontrada, CategoriaNaoPertenceException, 
                   CategoriaAtivaRemocaoException, SessaoJaInativaException, RepositorioCategoriaRemocaoException {
        negocioCategoria.removerCategoria(nomeCategoria);
    }

    public boolean isCategoriaPadrao(String nomeCategoria) throws CategoriaVaziaException {
        return negocioCategoria.isCategoriaPadrao(nomeCategoria);
    }
    
    // ========================================
    // RELATÓRIOS E ESTATÍSTICAS
    // ========================================
    
    public DadosEstatisticos obterEstatisticasProdutividade(LocalDateTime dataInicio, LocalDateTime dataFim) 
            throws SessaoJaInativaException, UsuarioVazioException {
        return calculadoraEstatisticas.calcularEstatisticas(negocioSessao.getUsuarioLogado(), dataInicio, dataFim);
    }
    
    public DadosEstatisticos.TarefasAtencao obterTarefasQueNecessitamAtencao() throws SessaoJaInativaException, UsuarioVazioException {
        DadosEstatisticos dados = calculadoraEstatisticas.calcularEstatisticas(negocioSessao.getUsuarioLogado(), null, null);
        return dados.getTarefasAtencao();
    }
    
    public Map<LocalDateTime, Long> obterProdutividadeTemporal(LocalDateTime dataInicio, LocalDateTime dataFim) 
            throws SessaoJaInativaException, UsuarioVazioException {
        return calculadoraEstatisticas.contarTarefasConcluidasPorDia(negocioSessao.getUsuarioLogado(), dataInicio, dataFim);
    }
    
    public long contarTarefasConcluidasPorCategoria(Categoria categoria) 
            throws SessaoJaInativaException, UsuarioVazioException {
        return calculadoraEstatisticas.contarTarefasConcluidasPorCategoria(negocioSessao.getUsuarioLogado(), categoria);
    }
    
    public long contarTarefasConcluidasPorPrioridade(Prioridade prioridade) 
            throws SessaoJaInativaException, UsuarioVazioException {
        return calculadoraEstatisticas.contarTarefasConcluidasPorPrioridade(negocioSessao.getUsuarioLogado(), prioridade);
    }

    // ========================================
    // CONTROLE DE SESSÕES POMODORO
    // ========================================
    
    public List<TarefaTemporizada> listarTarefasTemporizadas() throws SessaoJaInativaException {
        return negocioTarefa.listarTarefasTemporizadas();
    }
    
    public void iniciarSessaoPomodoro(String idTarefa) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.iniciarSessaoPomodoro(idTarefa);
    }
    
    public void pausarSessaoPomodoro(String idTarefa) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.pausarSessaoPomodoro(idTarefa);
    }
    
    public void retomarSessaoPomodoro(String idTarefa) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.retomarSessaoPomodoro(idTarefa);
    }
    
    public void concluirSessaoPomodoro(String idTarefa) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.concluirSessaoPomodoro(idTarefa);
    }
}
