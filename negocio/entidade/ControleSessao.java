package negocio.entidade;

import java.time.Duration;

/**
 * Interface que define capacidades de controle de sessões Pomodoro.
 * Permite iniciar, pausar, retomar e controlar sessões de trabalho.
 */
public interface ControleSessao {

    void iniciarSessao();
    
    void pausarSessao();
    
    void retomarSessao();
    
    void concluirSessao();
    
    boolean isSessaoAtiva();
    
    boolean isSessaoPausada();

    Duration getTempoRestante();

    double getProgressoSessao();

    int getSessoesCompletadas();
    
    int getTotalSessoes();
    
    Duration getDuracaoSessao();
    
    Duration getDuracaoPausa();
}
