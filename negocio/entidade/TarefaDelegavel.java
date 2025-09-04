package negocio.entidade;

import negocio.excecao.tarefa.CriadorVazioException;
import negocio.excecao.tarefa.DelegacaoResponsavelVazioException;
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

    public TarefaDelegavel(String titulo, String descricao, LocalDateTime prazo, 
                           Prioridade prioridade, Categoria categoria, 
                           Usuario criador, Usuario responsavel) throws CriadorVazioException, TituloVazioException, DelegacaoResponsavelVazioException {
        super(titulo, descricao, prazo, prioridade, categoria, criador);
        
        if (responsavel == null) {
            throw new DelegacaoResponsavelVazioException();
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
