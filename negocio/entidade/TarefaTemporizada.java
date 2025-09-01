package negocio.entidade;

import negocio.excecao.tarefa.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarefa com controle de tempo, prazo final e estimativa.
 * Implementa Temporizada para funcionalidades de tempo e Delegavel para delegação.
 */
public class TarefaTemporizada extends TarefaAbstrata implements Temporizada, Delegavel {

    private LocalDateTime prazoFinal;
    private Duration estimativa; 
    private Duration tempoGasto;

    public TarefaTemporizada(String titulo, String descricao, LocalDateTime prazo, Prioridade prioridade, Categoria categoria,
        Usuario criador, LocalDateTime prazoFinal, Duration estimativa) throws CriadorVazioException, TituloVazioException, PrazoVazioException, TemporizadaEstimativaException, PrazoInvalidoException {
        super(titulo, descricao, prazo, prioridade, categoria, criador);
        
        if (prazoFinal == null) {
            throw new PrazoVazioException();
        }
        
        if (estimativa == null || estimativa.isNegative()) {
            throw new TemporizadaEstimativaException();
        }
        
        if (prazoFinal.isBefore(prazo)) {
            throw new PrazoInvalidoException(prazo, prazoFinal);
        }
        
        this.prazoFinal = prazoFinal;
        this.estimativa = estimativa;
        this.tempoGasto = Duration.ZERO;
    }

    @Override
    public String getTipo() {
        return "Temporizada";
    }

    @Override
    public boolean podeSerDelegada() {
        return Delegavel.super.podeSerDelegada();
    }

    @Override
    public Usuario getResponsavelOriginal() {
        return getCriador(); // Criador é o responsável original
    }

    @Override
    public List<Usuario> getResponsaveis() {
        List<Usuario> responsaveis = new ArrayList<>();
        responsaveis.add(getCriador());
        return responsaveis;
    }

    @Override
    public List<RegistroDelegacao> getHistoricoDelegacoes() {
        return new ArrayList<>();
    }

    @Override
    public LocalDateTime getPrazoFinal() {
        return prazoFinal;
    }

    @Override
    public Duration getEstimativa() {
        return estimativa;
    }

    @Override
    public Duration getTempoGasto() {
        return tempoGasto;
    }

    @Override
    public void setTempoGasto(Duration tempoGasto)
            throws TemporizadaTempoException, AtualizarTarefaException {
        if (tempoGasto == null || tempoGasto.isNegative()) {
            throw new TemporizadaTempoException();
        }
        if (!podeSerAlterada()) {
            throw new AtualizarTarefaException(getTitulo());
        }
        
        this.tempoGasto = tempoGasto;
    }

    @Override
    public void setEstimativa(Duration estimativa)
            throws TemporizadaEstimativaException, AtualizarTarefaException {
        if (estimativa == null || estimativa.isNegative()) {
            throw new TemporizadaEstimativaException();
        }
        
        if (!podeSerAlterada()) {
            throw new AtualizarTarefaException(getTitulo());
        }
        
        this.estimativa = estimativa;
    }

    @Override
    public void setPrazoFinal(LocalDateTime prazoFinal)
            throws PrazoVazioException, AtualizarTarefaException, PrazoInvalidoException {
        if (prazoFinal == null) {
            throw new PrazoVazioException();
        }
        
        if (!podeSerAlterada()) {
            throw new AtualizarTarefaException(getTitulo());
        }
        
        if (prazoFinal.isBefore(getPrazo())) {
            throw new PrazoInvalidoException(getPrazo(), prazoFinal);
        }
        
        this.prazoFinal = prazoFinal;
    }

    public void registrarTrabalho(Duration duracao)
            throws TemporizadaDuracaoException, TemporizadaEstimativaException, TemporizadaTempoException, AtualizarTarefaException {
        if (duracao == null || duracao.isNegative()) {
            throw new TemporizadaDuracaoException();
        }
        
        if (!podeSerAlterada()) {
            throw new AtualizarTarefaException(getTitulo());
        }
        
        Temporizada.super.registrarTrabalho(duracao);
    }

    public boolean estourouPrazo() {
        return Temporizada.super.estourouPrazo();
    }

    public boolean ultrapassouEstimativa() {
        return Temporizada.super.ultrapassouEstimativa();
    }

}
