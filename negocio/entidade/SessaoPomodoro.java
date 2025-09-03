package negocio.entidade;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Representa uma sessão individual do sistema Pomodoro.
 * Armazena informações sobre início, fim, duração e status da sessão.
 */
public class SessaoPomodoro implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private Duration duracao;
    private boolean concluida;
    private boolean interrompida;
    
    public SessaoPomodoro() {
        this.concluida = false;
        this.interrompida = false;
    }
    
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    
    public LocalDateTime getFim() { return fim; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }
    
    public Duration getDuracao() { return duracao; }
    public void setDuracao(Duration duracao) { this.duracao = duracao; }
    
    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
    
    public boolean isInterrompida() { return interrompida; }
    public void setInterrompida(boolean interrompida) { this.interrompida = interrompida; }
    
    public void calcularDuracao() {
        if (inicio != null && fim != null) {
            this.duracao = Duration.between(inicio, fim);
        }
    }
    
    @Override
    public String toString() {
        return String.format("Sessão: %s - %s (%s)", 
            inicio != null ? inicio.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "N/A",
            fim != null ? fim.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "N/A",
            duracao != null ? formatarDuracao(duracao) : "N/A");
    }
    
    private String formatarDuracao(Duration duracao) {
        long minutos = duracao.toMinutes();
        long segundos = duracao.toSecondsPart();
        return String.format("%02d:%02d", minutos, segundos);
    }
}
