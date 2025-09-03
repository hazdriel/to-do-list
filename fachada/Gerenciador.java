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

    public Gerenciador() {
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
    
    public boolean fazerLogin(String email, String senha) {
        return negocioSessao.autenticar(email, senha);
    }

    public void cadastrarUsuario(String nome, String email, String senha) 
            throws IllegalArgumentException {
        negocioUsuario.cadastrarUsuario(nome, email, senha);
    }

    public boolean estaLogado() {
        return negocioSessao.estaLogado();
    }

    public Usuario getUsuarioLogado() {
        return negocioSessao.getUsuarioLogado();
    }

    public void fazerLogout() {
        negocioSessao.logout();
    }

    public void alterarSenha(String senhaAtual, String novaSenha) 
            throws IllegalArgumentException, IllegalStateException {
        negocioSessao.alterarSenhaUsuarioLogado(senhaAtual, novaSenha);
    }

    public void excluirConta(String senhaConfirmacao) 
            throws IllegalArgumentException, IllegalStateException {
        negocioSessao.excluirContaUsuarioLogado(senhaConfirmacao);
    }

    // ========================================
    // CRIAÇÃO DE TAREFAS
    // ========================================
    
    public void criarTarefaSimples(String titulo, String descricao, Prioridade prioridade, 
                                   LocalDateTime prazo, Categoria categoria) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.criarTarefaSimples(titulo, descricao, prioridade, prazo, categoria);
    }

    public void criarTarefaDelegavel(String titulo, String descricao, Prioridade prioridade, 
                                     LocalDateTime prazo, Categoria categoria, Usuario responsavel) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.criarTarefaDelegavel(titulo, descricao, prioridade, prazo, categoria, responsavel);
    }

    public void criarTarefaRecorrente(String titulo, String descricao, Prioridade prioridade, 
                                      LocalDateTime prazo, Categoria categoria, Period periodicidade) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.criarTarefaRecorrente(titulo, descricao, prioridade, prazo, categoria, periodicidade);
    }

    public void criarTarefaTemporizada(String titulo, String descricao, Prioridade prioridade, 
                                       LocalDateTime prazo, Categoria categoria, Duration duracaoSessao, 
                                       Duration duracaoPausa, int totalSessoes) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.criarTarefaTemporizada(titulo, descricao, prioridade, prazo, categoria, duracaoSessao, duracaoPausa, totalSessoes);
    }

    // ========================================
    // CONSULTA E LISTAGEM DE TAREFAS
    // ========================================
    
    public List<TarefaAbstrata> listarTarefas() {
        return negocioTarefa.listarTarefas();
    }

    public TarefaAbstrata buscarTarefaPorId(String id) 
            throws IllegalArgumentException {
        return negocioTarefa.buscarTarefaPorId(id);
    }

    // Listagens por critério
    public List<TarefaAbstrata> listarPorPrioridade(Prioridade prioridade) 
            throws IllegalArgumentException {
        return negocioTarefa.listarPorPrioridade(prioridade);
    }

    public List<TarefaAbstrata> listarPorCategoria(Categoria categoria) 
            throws IllegalArgumentException {
        return negocioTarefa.listarTarefasPorCategoria(categoria);
    }

    public List<TarefaAbstrata> listarPorTipo(String tipo) 
            throws IllegalArgumentException {
        return negocioTarefa.listarPorTipo(tipo);
    }

    // Listagens por status
    public List<TarefaAbstrata> listarConcluidas() {
        return negocioTarefa.listarConcluidas();
    }

    public List<TarefaAbstrata> listarPendentes() {
        return negocioTarefa.listarPendentes();
    }

    public List<TarefaAbstrata> listarAtrasadas() {
        return negocioTarefa.listarAtrasadas();
    }

    // Listagens relacionadas a delegação
    public List<TarefaAbstrata> listarTarefasDelegadas() {
        return negocioTarefa.listarTarefasDelegadas();
    }
    
    public List<TarefaAbstrata> listarTarefasDelegadasParaUsuario() {
        return negocioTarefa.listarTarefasDelegadasParaUsuario();
    }
    
    public List<TarefaAbstrata> listarTarefasDoUsuario() {
        return negocioTarefa.listarTarefasDoUsuario();
    }
    
    public List<TarefaAbstrata> listarTarefasDelegadasPeloUsuario() {
        return negocioTarefa.listarTarefasDelegadas();
    }

    // ========================================
    // MODIFICAÇÃO DE TAREFAS
    // ========================================
    
    public void atualizarTarefa(String id, String novoTitulo, String novaDescricao, 
                               Prioridade novaPrioridade, LocalDateTime novoPrazo, Categoria novaCategoria) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.atualizarTarefa(id, novoTitulo, novaDescricao, novaPrioridade, novoPrazo, novaCategoria);
    }

    public void removerTarefa(String id) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.removerTarefa(id);
    }

    // ========================================
    // CONTROLE DE STATUS DE TAREFAS
    // ========================================
    
    public void concluirTarefa(String id) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.concluirTarefa(id);
    }

    public void iniciarTarefa(String id) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.iniciarTarefa(id);
    }

    public void cancelarTarefa(String id) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.cancelarTarefa(id);
    }

    public void delegarTarefa(String id, Usuario novoResponsavel) 
            throws IllegalArgumentException, IllegalStateException {
        negocioTarefa.delegarTarefa(id, novoResponsavel);
    }



    // ========================================
    // GERENCIAMENTO DE USUÁRIOS
    // ========================================
    
    public List<Usuario> listarUsuarios() {
        return negocioUsuario.listarTodos();
    }
    
    public Usuario buscarUsuarioPorId(String id) 
            throws IllegalArgumentException {
        return negocioUsuario.buscarUsuarioPorId(id);
    }
    
    public Usuario buscarUsuarioPorEmail(String email) 
            throws IllegalArgumentException {
        return negocioUsuario.buscarUsuarioPorEmail(email);
    }

    // ========================================
    // GERENCIAMENTO DE CATEGORIAS
    // ========================================
  
    public List<Categoria> listarCategorias() {
        return negocioCategoria.listarCategoriasDoUsuario();
    }
    
    public Categoria criarCategoria(String nome) 
            throws IllegalArgumentException {
        return negocioCategoria.criarCategoria(nome);
    }

    public void removerCategoria(String nomeCategoria) 
            throws IllegalArgumentException, IllegalStateException {
        negocioCategoria.removerCategoria(nomeCategoria);
    }

    public boolean isCategoriaPadrao(String nomeCategoria) {
        return negocioCategoria.isCategoriaPadrao(nomeCategoria);
    }
    
    // ========================================
    // RELATÓRIOS E ESTATÍSTICAS
    // ========================================
    
    public DadosEstatisticos obterEstatisticasProdutividade(LocalDateTime dataInicio, LocalDateTime dataFim) 
            throws IllegalArgumentException {
        return calculadoraEstatisticas.calcularEstatisticas(negocioSessao.getUsuarioLogado(), dataInicio, dataFim);
    }
    
    public DadosEstatisticos.TarefasAtencao obterTarefasQueNecessitamAtencao() {
        DadosEstatisticos dados = calculadoraEstatisticas.calcularEstatisticas(negocioSessao.getUsuarioLogado(), null, null);
        return dados.getTarefasAtencao();
    }
    
    public Map<LocalDateTime, Long> obterProdutividadeTemporal(LocalDateTime dataInicio, LocalDateTime dataFim) 
            throws IllegalArgumentException {
        return calculadoraEstatisticas.contarTarefasConcluidasPorDia(negocioSessao.getUsuarioLogado(), dataInicio, dataFim);
    }
    
    public long contarTarefasConcluidasPorCategoria(Categoria categoria) 
            throws IllegalArgumentException {
        return calculadoraEstatisticas.contarTarefasConcluidasPorCategoria(negocioSessao.getUsuarioLogado(), categoria);
    }
    
    public long contarTarefasConcluidasPorPrioridade(Prioridade prioridade) 
            throws IllegalArgumentException {
        return calculadoraEstatisticas.contarTarefasConcluidasPorPrioridade(negocioSessao.getUsuarioLogado(), prioridade);
    }

    // ========================================
    // CONTROLE DE SESSÕES POMODORO
    // ========================================
    
    public List<TarefaTemporizada> listarTarefasTemporizadas() {
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
