package negocio.entidade;

import negocio.excecao.tarefa.CategoriaVaziaException;
import negocio.excecao.tarefa.CriadorVazioException;
import negocio.excecao.tarefa.PrazoInvalidoException;
import negocio.excecao.tarefa.TituloVazioException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarefa que pode ser delegada a outros usuários.
 * Implementa diretamente a interface Delegavel com métodos default.
 */
public class TarefaDelegavel extends TarefaAbstrata implements Delegavel {

    private final Usuario responsavelOriginal;
    private final List<Usuario> responsaveis;
    private final List<RegistroDelegacao> historicoDelegacoes;

<<<<<<< HEAD
  public TarefaDelegavel(String titulo, String descricao, LocalDateTime prazo, Prioridade prioridade, Categoria categoria, Usuario criador, Usuario responsavel)
          throws IllegalArgumentException, CriadorVazioException, TituloVazioException, CategoriaVaziaException, PrazoInvalidoException {
    super(titulo, descricao, prazo, prioridade, categoria, criador);
    this.responsavelOriginal = responsavel;
    this.responsavelAtual = responsavel;
    this.jaDelegada = false;
  }

  @Override
  public String getTipo() {
    return "Delegável";
  }

  @Override
  public boolean podeSerDelegada() {
    return !jaDelegada && getStatus() == Status.PENDENTE;
  }

  public Usuario getResponsavelAtual() {
    return responsavelAtual;
  }

  public Usuario getResponsavelOriginal() {
    return responsavelOriginal;
  }

  public void delegarPara(Usuario novoResponsavel) {
    if (!podeSerDelegada()) {
      throw new IllegalStateException("A tarefa não pode ser delegada.");
=======
    public TarefaDelegavel(String titulo, String descricao, LocalDateTime prazo, 
                           Prioridade prioridade, Categoria categoria, 
                           Usuario criador, Usuario responsavel) throws IllegalArgumentException {
        super(titulo, descricao, prazo, prioridade, categoria, criador);
        
        if (responsavel == null) {
            throw new IllegalArgumentException("Responsável não pode ser nulo");
        }
        
        this.responsavelOriginal = responsavel;
        this.responsaveis = new ArrayList<>();
        this.historicoDelegacoes = new ArrayList<>();
        
        // Criador se torna responsável automaticamente (supervisor)
        this.responsaveis.add(criador);
        
        // Responsável selecionado é adicionado (executor)
        if (!responsavel.equals(criador)) {
            this.responsaveis.add(responsavel);
        }
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e
    }

    @Override
    public String getTipo() {
        return "Delegável";
    }

    @Override
    public Usuario getResponsavelOriginal() {
        return responsavelOriginal;
    }

    @Override
    public List<Usuario> getResponsaveis() {
        return new ArrayList<>(responsaveis); 
    }

    @Override
    public List<RegistroDelegacao> getHistoricoDelegacoes() {
        return new ArrayList<>(historicoDelegacoes); 
    }

    @Override
    public boolean podeSerDelegada() {
        return getStatus() == Status.PENDENTE || getStatus() == Status.EM_PROGRESSO;
    }
}
