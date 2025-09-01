package negocio;

import negocio.entidade.*;
import dados.RepositorioTarefas;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;

public class CalculadoraEstatisticas {
    
    private final RepositorioTarefas repositorioTarefas;
    
    public CalculadoraEstatisticas(RepositorioTarefas repositorioTarefas) {
        if (repositorioTarefas == null) {
            throw new IllegalArgumentException("Repositório de tarefas não pode ser nulo");
        }
        this.repositorioTarefas = repositorioTarefas;
    }
    
    public DadosEstatisticos calcularEstatisticas(Usuario usuario, LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        
        DadosEstatisticos dados = new DadosEstatisticos();
        dados.setUsuario(usuario);
        dados.setDataInicio(dataInicio);
        dados.setDataFim(dataFim);
        
        List<TarefaAbstrata> todasTarefas = repositorioTarefas.listarTarefasPorUsuario(usuario);
        
        List<TarefaAbstrata> tarefasNoPeriodo = filtrarPorPeriodo(todasTarefas, dataInicio, dataFim);
        
        calcularDadosBasicos(dados, tarefasNoPeriodo);
        
        calcularDadosTemporais(dados, tarefasNoPeriodo);
        
        calcularDistribuicoes(dados, tarefasNoPeriodo);
        
        calcularDadosDelegacao(dados, tarefasNoPeriodo);
        
        calcularTarefasAtencao(dados, todasTarefas);
        
        return dados;
    }
    
    public DadosEstatisticos calcularEstatisticas(Usuario usuario) {
        return calcularEstatisticas(usuario, null, null);
    }
    
    private List<TarefaAbstrata> filtrarPorPeriodo(List<TarefaAbstrata> tarefas, 
                                                   LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null && fim == null) {
            return tarefas;
        }
        
        return tarefas.stream()
            .filter(tarefa -> {
                LocalDateTime dataCriacao = tarefa.getDataCriacao();
                boolean dentroInicio = inicio == null || !dataCriacao.isBefore(inicio);
                boolean dentroFim = fim == null || !dataCriacao.isAfter(fim);
                return dentroInicio && dentroFim;
            })
            .toList();
    }
    
    private void calcularDadosBasicos(DadosEstatisticos dados, List<TarefaAbstrata> tarefas) {
        dados.setTotalTarefas(tarefas.size());
        
        int concluidas = 0, canceladas = 0, pendentes = 0, emProgresso = 0;
        
        for (TarefaAbstrata tarefa : tarefas) {
            switch (tarefa.getStatus()) {
                case CONCLUIDA:
                    concluidas++;
                    break;
                case CANCELADA:
                    canceladas++;
                    break;
                case PENDENTE:
                    pendentes++;
                    break;
                case EM_PROGRESSO:
                    emProgresso++;
                    break;
            }
        }
        
        dados.setTarefasConcluidas(concluidas);
        dados.setTarefasCanceladas(canceladas);
        dados.setTarefasPendentes(pendentes);
        dados.setTarefasEmProgresso(emProgresso);
        
        if (tarefas.size() > 0) {
            double taxa = (double) concluidas / tarefas.size() * 100.0;
            dados.setTaxaConclusao(taxa);
        }
    }
    
    private void calcularDadosTemporais(DadosEstatisticos dados, List<TarefaAbstrata> tarefas) {
        List<TarefaAbstrata> tarefasConcluidas = tarefas.stream()
            .filter(t -> t.getStatus() == Status.CONCLUIDA)
            .toList();
            
        if (tarefasConcluidas.isEmpty()) {
            return;
        }
        
        long totalDias = 0;
        
        for (TarefaAbstrata tarefa : tarefasConcluidas) {
            LocalDateTime inicio = tarefa.getDataCriacao();
            LocalDateTime fim = tarefa.getDataFim();
            
            if (fim != null) {
                long diasTrabalhados = ChronoUnit.DAYS.between(inicio, fim);
                totalDias += diasTrabalhados;
            }
        }
        
        if (!tarefasConcluidas.isEmpty()) {
            double tempoMedio = (double) totalDias / tarefasConcluidas.size();
            Duration tempoMedioTarefa = Duration.ofMinutes((long) tempoMedio);
            dados.setTempoMedioTarefa(tempoMedioTarefa);
        }
        
        dados.setTempoTotalGasto(Duration.ofDays(totalDias));
    }
    
    private void calcularDistribuicoes(DadosEstatisticos dados, List<TarefaAbstrata> tarefas) {
        Map<Categoria, Integer> porCategoria = new HashMap<>();
        for (TarefaAbstrata tarefa : tarefas) {
            Categoria categoria = tarefa.getCategoria();
            if (categoria != null) {
                porCategoria.put(categoria, porCategoria.getOrDefault(categoria, 0) + 1);
            }
        }
        dados.setDistribuicaoPorCategoria(porCategoria);
        
        Map<Prioridade, Integer> porPrioridade = new HashMap<>();
        for (TarefaAbstrata tarefa : tarefas) {
            Prioridade prioridade = tarefa.getPrioridade();
            porPrioridade.put(prioridade, porPrioridade.getOrDefault(prioridade, 0) + 1);
        }
        dados.setDistribuicaoPorPrioridade(porPrioridade);
        
        Map<Status, Integer> porStatus = new HashMap<>();
        for (TarefaAbstrata tarefa : tarefas) {
            Status status = tarefa.getStatus();
            porStatus.put(status, porStatus.getOrDefault(status, 0) + 1);
        }
        dados.setDistribuicaoPorStatus(porStatus);
    }
    
    private void calcularDadosDelegacao(DadosEstatisticos dados, List<TarefaAbstrata> tarefas) {
        int tarefasDelegadas = 0;
        int totalResponsaveis = 0;
        
        for (TarefaAbstrata tarefa : tarefas) {
            if (tarefa instanceof Delegavel) {
                Delegavel delegavel = (Delegavel) tarefa;
                
                if (!delegavel.getHistoricoDelegacoes().isEmpty()) {
                    tarefasDelegadas++;
                }
                
                totalResponsaveis += delegavel.getNumeroResponsaveis();
            } else {
                totalResponsaveis += 1;
            }
        }
        
        dados.setTarefasDelegadas(tarefasDelegadas);
        
        if (!tarefas.isEmpty()) {
            double media = (double) totalResponsaveis / tarefas.size();
            dados.setMediaResponsaveisPorTarefa(media);
        }
    }
    
    public double calcularTaxaConclusao(Usuario usuario) {
        List<TarefaAbstrata> tarefas = repositorioTarefas.listarTarefasPorUsuario(usuario);
        if (tarefas.isEmpty()) return 0.0;
        
        long concluidas = tarefas.stream()
            .filter(t -> t.getStatus() == Status.CONCLUIDA)
            .count();
            
        return (double) concluidas / tarefas.size() * 100.0;
    }
    
    public long contarTarefasPorStatus(Usuario usuario, Status status) {
        List<TarefaAbstrata> tarefas = repositorioTarefas.listarTarefasPorUsuario(usuario);
        return tarefas.stream()
            .filter(t -> t.getStatus() == status)
            .count();
    }
    
    public List<TarefaAbstrata> buscarTarefasAtrasadas(Usuario usuario) {
        List<TarefaAbstrata> tarefas = repositorioTarefas.listarTarefasPorUsuario(usuario);
        return tarefas.stream()
            .filter(TarefaAbstrata::estaAtrasada)
            .toList();
    }
    
    public List<TarefaAbstrata> buscarTarefasVencemHoje(Usuario usuario) {
        List<TarefaAbstrata> tarefas = repositorioTarefas.listarTarefasPorUsuario(usuario);
        LocalDateTime hoje = LocalDateTime.now();
        
        return tarefas.stream()
            .filter(t -> t.getPrazo() != null)
            .filter(t -> t.getPrazo().toLocalDate().equals(hoje.toLocalDate()))
            .filter(t -> t.getStatus() != Status.CONCLUIDA && t.getStatus() != Status.CANCELADA)
            .toList();
    }
    
    public List<TarefaAbstrata> buscarTarefasVencemAmanha(Usuario usuario) {
        List<TarefaAbstrata> tarefas = repositorioTarefas.listarTarefasPorUsuario(usuario);
        LocalDateTime amanha = LocalDateTime.now().plusDays(1);
        
        return tarefas.stream()
            .filter(t -> t.getPrazo() != null)
            .filter(t -> t.getPrazo().toLocalDate().equals(amanha.toLocalDate()))
            .filter(t -> t.getStatus() != Status.CONCLUIDA && t.getStatus() != Status.CANCELADA)
            .toList();
    }
    
    public List<TarefaAbstrata> buscarTarefasEmProgresso(Usuario usuario) {
        List<TarefaAbstrata> tarefas = repositorioTarefas.listarTarefasPorUsuario(usuario);
        return tarefas.stream()
            .filter(t -> t.getStatus() == Status.EM_PROGRESSO)
            .toList();
    }
    
    
public List<TarefaAbstrata> buscarTarefasPendentes(Usuario usuario) {
    return repositorioTarefas.listarTarefasPorUsuario(usuario).stream()
        .filter(t -> t.getStatus() == Status.PENDENTE)
        .sorted(
            Comparator.comparing(TarefaAbstrata::getPrioridade, Comparator.reverseOrder())
                      .thenComparing(t -> t.getPrazo() == null) // quem tem prazo vem antes
                      .thenComparing(
                          TarefaAbstrata::getPrazo,
                          Comparator.nullsLast(Comparator.naturalOrder())
                      )
        )
        .toList();
}
    
    public long contarTarefasConcluidasPorCategoria(Usuario usuario, Categoria categoria) {
        List<TarefaAbstrata> tarefas = repositorioTarefas.listarTarefasPorUsuario(usuario);
        return tarefas.stream()
            .filter(t -> categoria.equals(t.getCategoria()))
            .filter(t -> t.getStatus() == Status.CONCLUIDA)
            .count();
    }
    
    public long contarTarefasConcluidasPorPrioridade(Usuario usuario, Prioridade prioridade) {
        List<TarefaAbstrata> tarefas = repositorioTarefas.listarTarefasPorUsuario(usuario);
        return tarefas.stream()
            .filter(t -> prioridade.equals(t.getPrioridade()))
            .filter(t -> t.getStatus() == Status.CONCLUIDA)
            .count();
    }
    
    public Map<LocalDateTime, Long> contarTarefasConcluidasPorDia(Usuario usuario, LocalDateTime inicio, LocalDateTime fim) {
        List<TarefaAbstrata> tarefas = repositorioTarefas.listarTarefasPorUsuario(usuario);
        
        return tarefas.stream()
            .filter(t -> t.getStatus() == Status.CONCLUIDA)
            .filter(t -> t.getDataFim() != null)
            .filter(t -> inicio == null || !t.getDataFim().isBefore(inicio))
            .filter(t -> fim == null || !t.getDataFim().isAfter(fim))
            .collect(java.util.stream.Collectors.groupingBy(
                t -> t.getDataFim().toLocalDate().atStartOfDay(),
                java.util.stream.Collectors.counting()
            ));
    }
    
    private void calcularTarefasAtencao(DadosEstatisticos dados, List<TarefaAbstrata> todasTarefas) {
        List<TarefaAbstrata> atrasadas = buscarTarefasAtrasadas(dados.getUsuario());
        List<TarefaAbstrata> vencemHoje = buscarTarefasVencemHoje(dados.getUsuario());
        List<TarefaAbstrata> vencemAmanha = buscarTarefasVencemAmanha(dados.getUsuario());
        List<TarefaAbstrata> emProgresso = buscarTarefasEmProgresso(dados.getUsuario());
        List<TarefaAbstrata> pendentes = buscarTarefasPendentes(dados.getUsuario());
        
        DadosEstatisticos.TarefasAtencao tarefasAtencao = 
            new DadosEstatisticos.TarefasAtencao(atrasadas, vencemHoje, vencemAmanha, emProgresso, pendentes);
        
        dados.setTarefasAtencao(tarefasAtencao);
    }
}
