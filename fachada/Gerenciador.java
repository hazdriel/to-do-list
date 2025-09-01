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

// Classe fachada que coordena as operações entre as camadas do sistema

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

    // GERENCIAMENTO DE SESSÃO E AUTENTICAÇÃO
    
    public boolean fazerLogin(String email, String senha) throws SessaoJaAtivoException, EmailVazioException, SenhaVaziaException, UsuarioVazioException {
        return negocioSessao.autenticar(email, senha);
    }

    public void cadastrarUsuario(String nome, String email, String senha) throws EmailVazioException, SenhaTamanhoInvalidoException, UsuarioExistenteException, NomeApenasLetrasException, SenhaVaziaException, NomeTamanhoInvalidoException, NomeVazioException, EmailFormatoInvalidoException {
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

    public void alterarSenha(String senhaAtual, String novaSenha) throws SessaoJaInativaException, SenhaTamanhoInvalidoException, SenhaIncorretaException, SenhaVaziaException, UsuarioVazioException {
        negocioSessao.alterarSenhaUsuarioLogado(senhaAtual, novaSenha);
    }

    public void excluirConta(String senhaConfirmacao) throws SessaoJaInativaException, SenhaIncorretaException, SenhaVaziaException, UsuarioVazioException {
        negocioSessao.excluirContaUsuarioLogado(senhaConfirmacao);
    }

    // CRIAÇÃO DE TAREFAS
    
    public void criarTarefaSimples(String titulo, String descricao, Prioridade prioridade, 
                                   LocalDateTime prazo, Categoria categoria) throws CriadorVazioException, SessaoJaInativaException, TarefaVaziaException, TituloVazioException, PrioridadeVaziaException {
        negocioTarefa.criarTarefaSimples(titulo, descricao, prioridade, prazo, categoria);
    }

    public void criarTarefaDelegavel(String titulo, String descricao, Prioridade prioridade, 
                                     LocalDateTime prazo, Categoria categoria, Usuario responsavel) throws CriadorVazioException, SessaoJaInativaException, TarefaVaziaException, TituloVazioException, DelegacaoResponsavelVazioException, PrioridadeVaziaException {
        negocioTarefa.criarTarefaDelegavel(titulo, descricao, prioridade, prazo, categoria, responsavel);
    }

    public void criarTarefaRecorrente(String titulo, String descricao, Prioridade prioridade, 
                                      LocalDateTime prazo, Categoria categoria, Period periodicidade) throws CriadorVazioException, SessaoJaInativaException, TarefaVaziaException, RecorrentePeriodicidadeException, TituloVazioException, PrioridadeVaziaException {
        negocioTarefa.criarTarefaRecorrente(titulo, descricao, prioridade, prazo, categoria, periodicidade);
    }

    public void criarTarefaTemporizada(String titulo, String descricao, Prioridade prioridade, 
                                       LocalDateTime prazo, Categoria categoria, LocalDateTime prazoFinal, 
                                       Duration estimativa) throws CriadorVazioException, SessaoJaInativaException, PrazoInvalidoException, TarefaVaziaException, TemporizadaEstimativaException, TituloVazioException, PrazoVazioException, PrioridadeVaziaException {
        negocioTarefa.criarTarefaTemporizada(titulo, descricao, prioridade, prazo, categoria, prazoFinal, estimativa);
    }

    // CONSULTA E LISTAGEM DE TAREFAS
    
    public List<TarefaAbstrata> listarTarefas() throws SessaoJaInativaException {
        return negocioTarefa.listarTarefas();
    }

    public TarefaAbstrata buscarTarefa(String id) throws TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException {
        return negocioTarefa.buscarTarefaPorId(id);
    }

    // Listagens por critério
    public List<TarefaAbstrata> listarPorPrioridade(Prioridade prioridade) throws SessaoJaInativaException, PrioridadeVaziaException {
        return negocioTarefa.listarPorPrioridade(prioridade);
    }

    public List<TarefaAbstrata> listarPorCategoria(Categoria categoria) throws CategoriaVaziaException, SessaoJaInativaException {
        return negocioTarefa.listarTarefasPorCategoria(categoria);
    }

    public List<TarefaAbstrata> listarPorTipo(String tipo) throws SessaoJaInativaException, TipoVazioException {
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

    // MODIFICAÇÃO DE TAREFAS

    public boolean atualizarTarefa(String id, String novoTitulo, String novaDescricao,
                                   Prioridade novaPrioridade, LocalDateTime novoPrazo, Categoria novaCategoria) throws TituloVazioException, DescricaoVaziaException, AtualizarTarefaException, PrazoPassadoException, PrazoInvalidoException, CategoriaVaziaException,
            TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException {
        negocioTarefa.atualizarTarefa(id, novoTitulo, novaDescricao, novaPrioridade, novoPrazo, novaCategoria);
        return true;
    }

    public boolean removerTarefa(String id) throws TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException {
        negocioTarefa.removerTarefa(id);
        return true;
    }

    public boolean salvarTarefa(TarefaAbstrata tarefa) throws TituloVazioException, DescricaoVaziaException, AtualizarTarefaException, PrazoPassadoException, PrazoInvalidoException, CategoriaVaziaException,
            TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException {
        if (tarefa == null) return false;
        return atualizarTarefa(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getPrioridade(), tarefa.getPrazo(), tarefa.getCategoria());
    }

    // CONTROLE DE STATUS DE TAREFAS

    public boolean concluirTarefa(String id) throws ConclusaoInvalidaException, TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException,
            TarefaIDNaoPertenceException, RecorrenteExecucaoException, AtualizarTarefaException {
        negocioTarefa.concluirTarefa(id);
        return true;
    }

    public boolean iniciarTarefa(String id) throws IniciacaoInvalidaException, TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException,
            TarefaIDNaoPertenceException {
        negocioTarefa.iniciarTarefa(id);
        return true;
    }

    public boolean cancelarTarefa(String id) throws CancelamentoInvalidoException, TarefaIDVazioException, SessaoJaInativaException, TarefaIDNaoEncontradaException,
            TarefaIDNaoPertenceException {
        negocioTarefa.cancelarTarefa(id);
        return true;
    }

    public boolean delegarTarefa(String id, Usuario novoResponsavel) throws DelegacaoStatusInvalidoException, DelegacaoMotivoException, DelegacaoRegistroInvalidoException,
            DelegacaoResponsavelInvalidoException, DelegacaoResponsavelVazioException, TarefaIDVazioException,
            SessaoJaInativaException, TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException,
            DelegacaoInvalidaException, CriadorVazioException {
        negocioTarefa.delegarTarefa(id, novoResponsavel);
        return true;
    }

    public boolean registrarTrabalho(String id, Duration duracao) throws TemporizadaNaoEException, TemporizadaDuracaoException, TemporizadaTempoException,
            TemporizadaEstimativaException, AtualizarTarefaException, TarefaIDVazioException, SessaoJaInativaException,
            TarefaIDNaoEncontradaException, TarefaIDNaoPertenceException {
        negocioTarefa.registrarTrabalho(id, duracao);
        return true;
    }

    // GERENCIAMENTO DE USUÁRIOS
    
    public List<Usuario> listarUsuarios() {
        return negocioUsuario.listarTodos();
    }
    
    public Usuario buscarUsuarioPorId(String id) throws IDUsuarioVazio {
        return negocioUsuario.buscarUsuarioPorId(id);
    }
    
    public Usuario buscarUsuarioPorEmail(String email) throws EmailVazioException {
        return negocioUsuario.buscarUsuarioPorEmail(email);
    }

    // GERENCIAMENTO DE CATEGORIAS
  
    public List<Categoria> listarCategorias() throws SessaoJaInativaException {
        return negocioCategoria.listarCategoriasDoUsuario();
    }
    
    public Categoria criarCategoria(String nome) throws CriadorVazioException, CategoriaVaziaException, SessaoJaInativaException {
        return negocioCategoria.criarCategoria(nome);
    }

    public boolean removerCategoria(String nomeCategoria) throws CategoriaNaoPertenceException, CategoriaNaoEncontrada, CategoriaVaziaException, SessaoJaInativaException, CategoriaAtivaRemocaoException, RepositorioCategoriaRemocaoException {
        return negocioCategoria.removerCategoria(nomeCategoria);
    }

    public boolean isCategoriaPadrao(String nomeCategoria) throws CategoriaVaziaException {
        return negocioCategoria.isCategoriaPadrao(nomeCategoria);
    }
    
    // BLOCO 8: RELATÓRIOS E ESTATÍSTICAS
    
    public DadosEstatisticos obterEstatisticasProdutividade(LocalDateTime dataInicio, LocalDateTime dataFim) throws SessaoJaInativaException, UsuarioVazioException {
        return calculadoraEstatisticas.calcularEstatisticas(negocioSessao.getUsuarioLogado(), dataInicio, dataFim);
    }
    
    public DadosEstatisticos.TarefasAtencao obterTarefasQueNecessitamAtencao() throws SessaoJaInativaException, UsuarioVazioException {
        DadosEstatisticos dados = calculadoraEstatisticas.calcularEstatisticas(negocioSessao.getUsuarioLogado(), null, null);
        return dados.getTarefasAtencao();
    }
    
    public Map<LocalDateTime, Long> obterProdutividadeTemporal(LocalDateTime dataInicio, LocalDateTime dataFim) throws SessaoJaInativaException {
        return calculadoraEstatisticas.contarTarefasConcluidasPorDia(negocioSessao.getUsuarioLogado(), dataInicio, dataFim);
    }
    
    public long contarTarefasConcluidasPorCategoria(Categoria categoria) throws SessaoJaInativaException {
        return calculadoraEstatisticas.contarTarefasConcluidasPorCategoria(negocioSessao.getUsuarioLogado(), categoria);
    }
    
    public long contarTarefasConcluidasPorPrioridade(Prioridade prioridade) throws SessaoJaInativaException {
        return calculadoraEstatisticas.contarTarefasConcluidasPorPrioridade(negocioSessao.getUsuarioLogado(), prioridade);
    }
}
