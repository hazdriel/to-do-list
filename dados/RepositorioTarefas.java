package dados;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import negocio.entidade.*;
import negocio.excecao.tarefa.TarefaVaziaException;

// Repositório para gerenciar persistência de tarefas

public class RepositorioTarefas {
    
    private final Map<String, TarefaAbstrata> tarefas = new HashMap<>();
    private final PersistenciaArquivo<TarefaAbstrata> persistencia;
    
    public RepositorioTarefas() {
        this.persistencia = new PersistenciaArquivo<>("tarefas");
        carregarDados();
    }
    
    public RepositorioTarefas(PersistenciaArquivo<TarefaAbstrata> persistencia) {
        this.persistencia = persistencia;
        carregarDados();
    }

    // MÉTODOS BÁSICOS DE CRUD
    
    public void inserirTarefa(TarefaAbstrata tarefa) throws TarefaVaziaException {
        if (tarefa == null) {
            throw new TarefaVaziaException();
        }
        tarefas.put(tarefa.getId(), tarefa);
        salvarDados();
    }

    public TarefaAbstrata buscarTarefaPorID(String id) {
        return tarefas.get(id);
    }

    public boolean atualizarTarefa(TarefaAbstrata tarefaAtualizada) throws TarefaVaziaException {
        if (tarefaAtualizada == null) {
            throw new TarefaVaziaException();
        }
        
        if (tarefas.containsKey(tarefaAtualizada.getId())) {
            tarefas.put(tarefaAtualizada.getId(), tarefaAtualizada);
            salvarDados();
            return true;
        }
        return false;
    }

    public boolean removerTarefa(String id) {
        boolean removido = tarefas.remove(id) != null;
        if (removido) {
            salvarDados();
        }
        return removido;
    }
    
    public List<TarefaAbstrata> listarTodas() {
        return new ArrayList<>(tarefas.values());
    }
    
    public int getTotalTarefas() {
        return tarefas.size();
    }
    
    // MÉTODOS PARA FILTRAGEM POR USUÁRIO
    
    public List<TarefaAbstrata> listarTarefasPorUsuario(Usuario usuario) {
        List<TarefaAbstrata> resultado = new ArrayList<>();
        if (usuario != null) {
            for (TarefaAbstrata tarefa : tarefas.values()) {
                if (tarefa.getCriador().equals(usuario)) {
                    resultado.add(tarefa);
                }
            }
        }
        return resultado;
    }

    public List<TarefaAbstrata> listarPorPrioridade(Prioridade prioridade, Usuario usuario) {
        List<TarefaAbstrata> resultado = new ArrayList<>();
        if (prioridade != null && usuario != null) {
            for (TarefaAbstrata tarefa : tarefas.values()) {
                if (tarefa.getPrioridade() == prioridade && tarefa.getCriador().equals(usuario)) {
                    resultado.add(tarefa);
                }
            }
        }
        return resultado;
    }

    public List<TarefaAbstrata> listarPorStatus(Status status, Usuario usuario) {
        List<TarefaAbstrata> resultado = new ArrayList<>();
        if (status != null && usuario != null) {
            for (TarefaAbstrata tarefa : tarefas.values()) {
                if (tarefa.getStatus() == status && tarefa.getCriador().equals(usuario)) {
                    resultado.add(tarefa);
                }
            }
        }
        return resultado;
    }

    public List<TarefaAbstrata> listarPorTipo(String tipo, Usuario usuario) {
        List<TarefaAbstrata> resultado = new ArrayList<>();
        if (tipo != null && usuario != null) {
            for (TarefaAbstrata tarefa : tarefas.values()) {
                if (tarefa.getTipo().equals(tipo) && tarefa.getCriador().equals(usuario)) {
                    resultado.add(tarefa);
                }
            }
        }
        return resultado;
    }

    public List<TarefaAbstrata> listarPorCategoria(Categoria categoria, Usuario usuario) {
        List<TarefaAbstrata> resultado = new ArrayList<>();
        if (categoria != null && usuario != null) {
            for (TarefaAbstrata tarefa : tarefas.values()) {
                if (categoria.equals(tarefa.getCategoria()) && tarefa.getCriador().equals(usuario)) {
                    resultado.add(tarefa);
                }
            }
        }
        return resultado;
    }
    
    // MÉTODOS PARA FILTRAGEM POR STATUS ESPECÍFICOS
    
    public List<TarefaAbstrata> buscarConcluidas(Usuario usuario) {
        return listarPorStatus(Status.CONCLUIDA, usuario);
    }
    
    public List<TarefaAbstrata> buscarPendentes(Usuario usuario) {
        return listarPorStatus(Status.PENDENTE, usuario);
    }
    

    
    // MÉTODOS DE PERSISTÊNCIA
    
    private void carregarDados() {
        try {
            List<TarefaAbstrata> tarefasCarregadas = persistencia.carregar();
            tarefas.clear();
            
            int maxIdTarefa = 0;
            for (TarefaAbstrata tarefa : tarefasCarregadas) {
                tarefas.put(tarefa.getId(), tarefa);
                
                int numeroId = GeradorId.extrairNumeroId(tarefa.getId());
                maxIdTarefa = Math.max(maxIdTarefa, numeroId);
            }
            
            if (maxIdTarefa > 0) {
                GeradorId.sincronizarContadorTarefas(maxIdTarefa);
            }
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao carregar tarefas: " + e.getMessage());
        }
    }
    
    private void salvarDados() {
        try {
            List<TarefaAbstrata> listaTarefas = new ArrayList<>(tarefas.values());
            persistencia.salvar(listaTarefas);
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }
    
    public void limparTodos() {
        try {
            tarefas.clear();
            persistencia.limparDados();
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao limpar dados de tarefas: " + e.getMessage());
        }
    }

}
