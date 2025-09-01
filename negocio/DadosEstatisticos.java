package negocio;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Map;

import negocio.entidade.Categoria;
import negocio.entidade.Prioridade;
import negocio.entidade.Status;
import negocio.entidade.Usuario;
import negocio.entidade.TarefaAbstrata;

import java.util.HashMap;
import java.util.List;

/**
 * Classe que encapsula dados estatísticos calculados para relatórios.
 */
public class DadosEstatisticos {
    
    private Usuario usuario;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    
    private int totalTarefas;
    private int tarefasConcluidas;
    private int tarefasPendentes;
    private int tarefasEmProgresso;
    private int tarefasCanceladas;
    private double taxaConclusao;
    
    private Duration tempoTotalGasto;
    private Duration tempoMedioTarefa;
    
   
    private Map<Status, Integer> distribuicaoPorStatus;
    private Map<Categoria, Integer> distribuicaoPorCategoria;
    private Map<Prioridade, Integer> distribuicaoPorPrioridade;
    
    private int tarefasDelegadas;
    private int tarefasRecebidas;
    private double mediaResponsaveisPorTarefa;
    
    private TarefasAtencao tarefasAtencao;
    
    public DadosEstatisticos() {
        this.distribuicaoPorStatus = new HashMap<>();
        this.distribuicaoPorCategoria = new HashMap<>();
        this.distribuicaoPorPrioridade = new HashMap<>();
        this.tempoTotalGasto = Duration.ZERO;
        this.tempoMedioTarefa = Duration.ZERO;
        this.tarefasAtencao = new TarefasAtencao();
    }
    
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    
    public int getTotalTarefas() { return totalTarefas; }
    public void setTotalTarefas(int totalTarefas) { 
        this.totalTarefas = Math.max(0, totalTarefas); 
    }
    
    public int getTarefasConcluidas() { return tarefasConcluidas; }
    public void setTarefasConcluidas(int tarefasConcluidas) { 
        this.tarefasConcluidas = Math.max(0, tarefasConcluidas); 
    }
    
    public int getTarefasPendentes() { return tarefasPendentes; }
    public void setTarefasPendentes(int tarefasPendentes) { 
        this.tarefasPendentes = Math.max(0, tarefasPendentes); 
    }
    
    public int getTarefasEmProgresso() { return tarefasEmProgresso; }
    public void setTarefasEmProgresso(int tarefasEmProgresso) { 
        this.tarefasEmProgresso = Math.max(0, tarefasEmProgresso); 
    }
    
    public int getTarefasCanceladas() { return tarefasCanceladas; }
    public void setTarefasCanceladas(int tarefasCanceladas) { 
        this.tarefasCanceladas = Math.max(0, tarefasCanceladas); 
    }
    
    public double getTaxaConclusao() { return taxaConclusao; }
    public void setTaxaConclusao(double taxaConclusao) { 
        this.taxaConclusao = Math.max(0.0, Math.min(100.0, taxaConclusao)); 
    }
    
    public Duration getTempoTotalGasto() { return tempoTotalGasto; }
    public void setTempoTotalGasto(Duration tempoTotalGasto) { 
        this.tempoTotalGasto = tempoTotalGasto != null ? tempoTotalGasto : Duration.ZERO; 
    }
    
    public Duration getTempoMedioTarefa() { return tempoMedioTarefa; }
    public void setTempoMedioTarefa(Duration tempoMedioTarefa) { 
        this.tempoMedioTarefa = tempoMedioTarefa != null ? tempoMedioTarefa : Duration.ZERO; 
    }
    
    public Map<Status, Integer> getDistribuicaoPorStatus() { return distribuicaoPorStatus; }
    public void setDistribuicaoPorStatus(Map<Status, Integer> distribuicaoPorStatus) { 
        this.distribuicaoPorStatus = distribuicaoPorStatus != null ? distribuicaoPorStatus : new HashMap<>(); 
    }
    
    public Map<Categoria, Integer> getDistribuicaoPorCategoria() { return distribuicaoPorCategoria; }
    public void setDistribuicaoPorCategoria(Map<Categoria, Integer> distribuicaoPorCategoria) { 
        this.distribuicaoPorCategoria = distribuicaoPorCategoria != null ? distribuicaoPorCategoria : new HashMap<>(); 
    }
    
    public Map<Prioridade, Integer> getDistribuicaoPorPrioridade() { return distribuicaoPorPrioridade; }
    public void setDistribuicaoPorPrioridade(Map<Prioridade, Integer> distribuicaoPorPrioridade) { 
        this.distribuicaoPorPrioridade = distribuicaoPorPrioridade != null ? distribuicaoPorPrioridade : new HashMap<>(); 
    }
    
    public int getTarefasDelegadas() { return tarefasDelegadas; }
    public void setTarefasDelegadas(int tarefasDelegadas) { 
        this.tarefasDelegadas = Math.max(0, tarefasDelegadas); 
    }
    
    public int getTarefasRecebidas() { return tarefasRecebidas; }
    public void setTarefasRecebidas(int tarefasRecebidas) { 
        this.tarefasRecebidas = Math.max(0, tarefasRecebidas); 
    }
    
    public double getMediaResponsaveisPorTarefa() { return mediaResponsaveisPorTarefa; }
    public void setMediaResponsaveisPorTarefa(double mediaResponsaveisPorTarefa) { 
        this.mediaResponsaveisPorTarefa = Math.max(0.0, mediaResponsaveisPorTarefa); 
    }
    
    public TarefasAtencao getTarefasAtencao() { return tarefasAtencao; }
    public void setTarefasAtencao(TarefasAtencao tarefasAtencao) { 
        this.tarefasAtencao = tarefasAtencao != null ? tarefasAtencao : new TarefasAtencao(); 
    }
    
    /**
     * Classe interna que representa tarefas que necessitam de atenção do usuário.
     * Agrupa tarefas por diferentes critérios de urgência e prioridade.
     */

    public static class TarefasAtencao {
        private final List<TarefaAbstrata> atrasadas;
        private final List<TarefaAbstrata> vencemHoje;
        private final List<TarefaAbstrata> vencemAmanha;
        private final List<TarefaAbstrata> emProgresso;
        private final List<TarefaAbstrata> pendentes;
        
        public TarefasAtencao() {
            this.atrasadas = new java.util.ArrayList<>();
            this.vencemHoje = new java.util.ArrayList<>();
            this.vencemAmanha = new java.util.ArrayList<>();
            this.emProgresso = new java.util.ArrayList<>();
            this.pendentes = new java.util.ArrayList<>();
        }
        
        public TarefasAtencao(List<TarefaAbstrata> atrasadas,
                             List<TarefaAbstrata> vencemHoje,
                             List<TarefaAbstrata> vencemAmanha,
                             List<TarefaAbstrata> emProgresso,
                             List<TarefaAbstrata> pendentes) {
            this.atrasadas = atrasadas != null ? atrasadas : new java.util.ArrayList<>();
            this.vencemHoje = vencemHoje != null ? vencemHoje : new java.util.ArrayList<>();
            this.vencemAmanha = vencemAmanha != null ? vencemAmanha : new java.util.ArrayList<>();
            this.emProgresso = emProgresso != null ? emProgresso : new java.util.ArrayList<>();
            this.pendentes = pendentes != null ? pendentes : new java.util.ArrayList<>();
        }
        
        public List<TarefaAbstrata> getAtrasadas() { return atrasadas; }
        public List<TarefaAbstrata> getVencemHoje() { return vencemHoje; }
        public List<TarefaAbstrata> getVencemAmanha() { return vencemAmanha; }
        public List<TarefaAbstrata> getEmProgresso() { return emProgresso; }
        public List<TarefaAbstrata> getPendentes() { return pendentes; }
        

        public int getTotalTarefasAtencao() {
            return atrasadas.size() + vencemHoje.size() + vencemAmanha.size() + 
                   emProgresso.size() + pendentes.size();
        }
        

        public boolean temTarefasAtencao() {
            return getTotalTarefasAtencao() > 0;
        }
    }
}
