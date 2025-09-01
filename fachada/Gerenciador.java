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

    public Gerenciador() {
        this.repositorioTarefas = new RepositorioTarefas();
        this.repositorioUsuarios = new RepositorioUsuarios();
        this.repositorioCategorias = new RepositorioCategorias();
        this.negocioUsuario = new NegocioUsuario(repositorioUsuarios);
        this.negocioSessao = new NegocioSessao(negocioUsuario);
        this.negocioTarefa = new NegocioTarefa(repositorioTarefas, negocioSessao);
        this.calculadoraEstatisticas = new CalculadoraEstatisticas(repositorioTarefas);
        this.negocioCategoria = new NegocioCategoria(repositorioCategorias, negocioSessao, negocioTarefa);
    }

    // GERENCIAMENTO DE SESSÃO E AUTENTICAÇÃO
    
    public boolean fazerLogin(String email, String senha) {
        return negocioSessao.autenticar(email, senha);
    }

    public void cadastrarUsuario(String nome, String email, String senha) {
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

    public void alterarSenha(String senhaAtual, String novaSenha) {
        negocioSessao.alterarSenhaUsuarioLogado(senhaAtual, novaSenha);
    }

    public void excluirConta(String senhaConfirmacao) {
        negocioSessao.excluirContaUsuarioLogado(senhaConfirmacao);
    }

    // CRIAÇÃO DE TAREFAS
    
    public void criarTarefaSimples(String titulo, String descricao, Prioridade prioridade, 
                                   LocalDateTime prazo, Categoria categoria) {
        negocioTarefa.criarTarefaSimples(titulo, descricao, prioridade, prazo, categoria);
    }

    public void criarTarefaDelegavel(String titulo, String descricao, Prioridade prioridade, 
                                     LocalDateTime prazo, Categoria categoria, Usuario responsavel) {
        negocioTarefa.criarTarefaDelegavel(titulo, descricao, prioridade, prazo, categoria, responsavel);
    }

    public void criarTarefaRecorrente(String titulo, String descricao, Prioridade prioridade, 
                                      LocalDateTime prazo, Categoria categoria, Period periodicidade) {
        negocioTarefa.criarTarefaRecorrente(titulo, descricao, prioridade, prazo, categoria, periodicidade);
    }

    public void criarTarefaTemporizada(String titulo, String descricao, Prioridade prioridade, 
                                       LocalDateTime prazo, Categoria categoria, LocalDateTime prazoFinal, 
                                       Duration estimativa) {
        negocioTarefa.criarTarefaTemporizada(titulo, descricao, prioridade, prazo, categoria, prazoFinal, estimativa);
    }

    // CONSULTA E LISTAGEM DE TAREFAS
    
    public List<TarefaAbstrata> listarTarefas() {
        return negocioTarefa.listarTarefas();
    }

    public TarefaAbstrata buscarTarefa(String id) {
        return negocioTarefa.buscarTarefaPorId(id);
    }

    // Listagens por critério
    public List<TarefaAbstrata> listarPorPrioridade(Prioridade prioridade) {
        return negocioTarefa.listarPorPrioridade(prioridade);
    }

    public List<TarefaAbstrata> listarPorCategoria(Categoria categoria) {
        return negocioTarefa.listarTarefasPorCategoria(categoria);
    }

    public List<TarefaAbstrata> listarPorTipo(String tipo) {
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

    // MODIFICAÇÃO DE TAREFAS
    
    public boolean atualizarTarefa(String id, String novoTitulo, String novaDescricao, 
                                   Prioridade novaPrioridade, LocalDateTime novoPrazo, Categoria novaCategoria) {
        try {
            negocioTarefa.atualizarTarefa(id, novoTitulo, novaDescricao, novaPrioridade, novoPrazo, novaCategoria);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

    public boolean removerTarefa(String id) {
        try {
            negocioTarefa.removerTarefa(id);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

    public boolean salvarTarefa(TarefaAbstrata tarefa) {
        if (tarefa == null) return false;
        return atualizarTarefa(tarefa.getId(), tarefa.getTitulo(), 
                              tarefa.getDescricao(), tarefa.getPrioridade(), 
                              tarefa.getPrazo(), tarefa.getCategoria());
    }

    // CONTROLE DE STATUS DE TAREFAS
    
    public boolean concluirTarefa(String id) {
        try {
            negocioTarefa.concluirTarefa(id);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

    public boolean iniciarTarefa(String id) {
        try {
            negocioTarefa.iniciarTarefa(id);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

    public boolean cancelarTarefa(String id) {
        try {
            negocioTarefa.cancelarTarefa(id);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

    public boolean delegarTarefa(String id, Usuario novoResponsavel) {
        try {
            negocioTarefa.delegarTarefa(id, novoResponsavel);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

    public boolean registrarTrabalho(String id, Duration duracao) {
        try {
            negocioTarefa.registrarTrabalho(id, duracao);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

    // GERENCIAMENTO DE USUÁRIOS
    
    public List<Usuario> listarUsuarios() {
        return negocioUsuario.listarTodos();
    }
    
    public Usuario buscarUsuarioPorId(String id) {
        return negocioUsuario.buscarUsuarioPorId(id);
    }
    
    public Usuario buscarUsuarioPorEmail(String email) {
        return negocioUsuario.buscarUsuarioPorEmail(email);
    }

    // GERENCIAMENTO DE CATEGORIAS
  
    public List<Categoria> listarCategorias() {
        return negocioCategoria.listarCategoriasDoUsuario();
    }
    
    public Categoria criarCategoria(String nome) {
        return negocioCategoria.criarCategoria(nome);
    }

    public boolean removerCategoria(String nomeCategoria) {
        return negocioCategoria.removerCategoria(nomeCategoria);
    }

    public boolean isCategoriaPadrao(String nomeCategoria) {
        return negocioCategoria.isCategoriaPadrao(nomeCategoria);
    }
    
    // BLOCO 8: RELATÓRIOS E ESTATÍSTICAS
    
    public DadosEstatisticos obterEstatisticasProdutividade(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return calculadoraEstatisticas.calcularEstatisticas(negocioSessao.getUsuarioLogado(), dataInicio, dataFim);
    }
    
    public DadosEstatisticos.TarefasAtencao obterTarefasQueNecessitamAtencao() {
        DadosEstatisticos dados = calculadoraEstatisticas.calcularEstatisticas(negocioSessao.getUsuarioLogado(), null, null);
        return dados.getTarefasAtencao();
    }
    
    public Map<LocalDateTime, Long> obterProdutividadeTemporal(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return calculadoraEstatisticas.contarTarefasConcluidasPorDia(negocioSessao.getUsuarioLogado(), dataInicio, dataFim);
    }
    
    public long contarTarefasConcluidasPorCategoria(Categoria categoria) {
        return calculadoraEstatisticas.contarTarefasConcluidasPorCategoria(negocioSessao.getUsuarioLogado(), categoria);
    }
    
    public long contarTarefasConcluidasPorPrioridade(Prioridade prioridade) {
        return calculadoraEstatisticas.contarTarefasConcluidasPorPrioridade(negocioSessao.getUsuarioLogado(), prioridade);
    }
}
