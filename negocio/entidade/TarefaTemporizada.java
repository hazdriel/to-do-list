package negocio.entidade;

import negocio.excecao.tarefa.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarefa com sistema Pomodoro integrado.
 * Permite sessões de trabalho cronometradas com pausas configuráveis.
 * Implementa Temporizada para compatibilidade e ControleSessao para funcionalidades Pomodoro.
 */
public class TarefaTemporizada extends TarefaAbstrata implements Temporizada, ControleSessao {

    private static final long serialVersionUID = 1L;

    // Configurações do Pomodoro
    private Duration duracaoSessao;        // Ex: 25 minutos
    private Duration duracaoPausa;         // Ex: 5 minutos
    private int totalSessoes;              // Ex: 4 sessões
    private int sessoesCompletadas;        // Contador atual
    
    private LocalDateTime inicioSessao;
    private boolean sessaoAtiva;
    private boolean emPausa;
    private LocalDateTime inicioPausa;
    
    private List<SessaoPomodoro> historicoSessoes;
    
    private static final Duration MAX_DURACAO_SESSAO = Duration.ofHours(2);
    private static final Duration MIN_DURACAO_SESSAO = Duration.ofMinutes(5);

    public TarefaTemporizada(String titulo, String descricao, LocalDateTime prazo, 
                            Prioridade prioridade, Categoria categoria, Usuario criador,
                            Duration duracaoSessao, Duration duracaoPausa, int totalSessoes) 
            throws TituloVazioException, CriadorVazioException {
        super(titulo, descricao, prazo, prioridade, categoria, criador);
        
        if (duracaoSessao == null || duracaoSessao.isNegative() || 
            duracaoSessao.compareTo(MAX_DURACAO_SESSAO) > 0 ||
            duracaoSessao.compareTo(MIN_DURACAO_SESSAO) < 0) {
            throw new IllegalArgumentException("Duração da sessão deve ser entre 5min e 2h");
        }
        
        if (duracaoPausa == null || duracaoPausa.isNegative()) {
            throw new IllegalArgumentException("Duração da pausa deve ser positiva");
        }
        
        if (totalSessoes <= 0) {
            throw new IllegalArgumentException("Total de sessões deve ser positivo");
        }
        
        this.duracaoSessao = duracaoSessao;
        this.duracaoPausa = duracaoPausa;
        this.totalSessoes = totalSessoes;
        this.sessoesCompletadas = 0;
        this.historicoSessoes = new ArrayList<>();
        this.sessaoAtiva = false;
        this.emPausa = false;
    }

    @Override
    public String getTipo() {
        return "Temporizada";
    }

    @Override
    public boolean podeSerDelegada() {
        return false; 
    }

    @Override
    public void iniciarSessao() {
        if (sessaoAtiva) {
            throw new IllegalStateException("Sessão já está ativa");
        }
        
        if (sessoesCompletadas >= totalSessoes) {
            throw new IllegalStateException("Todas as sessões foram completadas");
        }
        
        sessaoAtiva = true;
        emPausa = false;
        inicioSessao = LocalDateTime.now();
    }

    @Override
    public void pausarSessao() {
        if (!sessaoAtiva) {
            throw new IllegalStateException("Nenhuma sessão ativa para pausar");
        }
        
        emPausa = true;
        inicioPausa = LocalDateTime.now();
    }

    @Override
    public void retomarSessao() {
        if (!sessaoAtiva || !emPausa) {
            throw new IllegalStateException("Nenhuma sessão pausada para retomar");
        }
        
        emPausa = false;
        Duration tempoPausado = Duration.between(inicioPausa, LocalDateTime.now());
        inicioSessao = inicioSessao.plus(tempoPausado);
    }

    @Override
    public void concluirSessao() {
        if (!sessaoAtiva) {
            throw new IllegalStateException("Nenhuma sessão ativa para concluir");
        }
        
        SessaoPomodoro sessao = new SessaoPomodoro();
        sessao.setInicio(inicioSessao);
        sessao.setFim(LocalDateTime.now());
        sessao.calcularDuracao();
        sessao.setConcluida(true);
        sessao.setInterrompida(emPausa);
        
        historicoSessoes.add(sessao);
        sessoesCompletadas++;
        
        sessaoAtiva = false;
        emPausa = false;
        inicioSessao = null;
        inicioPausa = null;
    }

    @Override
    public boolean isSessaoAtiva() {
        return sessaoAtiva;
    }

    @Override
    public boolean isSessaoPausada() {
        return sessaoAtiva && emPausa;
    }

    @Override
    public Duration getTempoRestante() {
        if (!sessaoAtiva) {
            return Duration.ZERO;
        }
        
        LocalDateTime agora = LocalDateTime.now();
        
        if (emPausa) {
            LocalDateTime fimSessao = inicioSessao.plus(duracaoSessao);
            return Duration.between(inicioPausa, fimSessao);
        }
        
        LocalDateTime fimSessao = inicioSessao.plus(duracaoSessao);
        
        if (agora.isAfter(fimSessao)) {
            return Duration.ZERO;
        }
        
        return Duration.between(agora, fimSessao);
    }

    @Override
    public double getProgressoSessao() {
        if (!sessaoAtiva) {
            return 0.0;
        }
        
        LocalDateTime referencia = emPausa ? inicioPausa : LocalDateTime.now();
        LocalDateTime fimSessao = inicioSessao.plus(duracaoSessao);
        
        if (referencia.isAfter(fimSessao)) {
            return 100.0;
        }
        
        Duration tempoDecorrido = Duration.between(inicioSessao, referencia);
        double progresso = (double) tempoDecorrido.toSeconds() / duracaoSessao.toSeconds() * 100;
        return Math.max(0.0, Math.min(100.0, progresso));
    }

    @Override
    public int getSessoesCompletadas() {
        return sessoesCompletadas;
    }

    @Override
    public int getTotalSessoes() {
        return totalSessoes;
    }

    @Override
    public Duration getDuracaoSessao() {
        return duracaoSessao;
    }

    @Override
    public Duration getDuracaoPausa() {
        return duracaoPausa;
    }

    @Override
    public LocalDateTime getPrazoFinal() {
        return getPrazo(); 
    }

    @Override
    public void setPrazoFinal(LocalDateTime prazoFinal) throws AtualizarTarefaException {
        try {
            setPrazo(prazoFinal);
        } catch (Exception e) {
            throw new AtualizarTarefaException(getTitulo());
        }
    }

    @Override
    public Duration getEstimativa() {
        return duracaoSessao.multipliedBy(totalSessoes);
    }

    @Override
    public void setEstimativa(Duration estimativa) {
        if (estimativa != null && !estimativa.isNegative() && totalSessoes > 0) {
            this.duracaoSessao = estimativa.dividedBy(totalSessoes);
        }
    }

    @Override
    public Duration getTempoGasto() {
        return getTempoTotalFocado();
    }

    @Override
    public void setTempoGasto(Duration tempoGasto) {
        throw new UnsupportedOperationException("Tempo gasto é calculado automaticamente");
    }

    public Duration getTempoTotalFocado() {
        return historicoSessoes.stream()
            .filter(s -> s.isConcluida() && !s.isInterrompida())
            .map(SessaoPomodoro::getDuracao)
            .reduce(Duration.ZERO, Duration::plus);
    }

    public int getSessoesInterrompidas() {
        return (int) historicoSessoes.stream()
            .filter(SessaoPomodoro::isInterrompida)
            .count();
    }

    public double getTaxaConclusao() {
        if (historicoSessoes.isEmpty()) return 0.0;
        long concluidas = historicoSessoes.stream().filter(SessaoPomodoro::isConcluida).count();
        return (double) concluidas / historicoSessoes.size() * 100;
    }

    public List<SessaoPomodoro> getHistoricoSessoes() {
        return new ArrayList<>(historicoSessoes);
    }

    public boolean isCompleta() {
        return sessoesCompletadas >= totalSessoes;
    }

    public int getProximaSessao() {
        return sessoesCompletadas + 1;
    }

    @Override
    public String toString() {
        return String.format("%s: %s (Sessões: %d/%d, Tempo: %s)", 
            getTipo(), getTitulo(), sessoesCompletadas, totalSessoes, 
            formatarDuracao(getDuracaoSessao()));
    }

    private String formatarDuracao(Duration duracao) {
        long minutos = duracao.toMinutes();
        return String.format("%dmin", minutos);
    }

}
