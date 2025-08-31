package dados;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import negocio.entidade.*;

// Repositório de tarefas que salva em arquivo .dat
public class RepositorioTarefasComPersistencia {
    
    private final Map<String, TarefaAbstrata> tarefas = new HashMap<>();
    private final IPersistencia<TarefaAbstrata> persistencia;
    
    // Construtor que carrega dados existentes
    public RepositorioTarefasComPersistencia() {
        this.persistencia = new PersistenciaArquivo<>("tarefas");
        carregarDados();
    }
    
    // Construtor para testes
    public RepositorioTarefasComPersistencia(IPersistencia<TarefaAbstrata> persistencia) {
        this.persistencia = persistencia;
        carregarDados();
    }

    public void inserirTarefa(TarefaAbstrata tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException("Tarefa não pode ser nula");
        }
        tarefas.put(tarefa.getId(), tarefa);
        salvarDados();
    }

    public TarefaAbstrata buscarTarefaPorID(String id, Usuario usuario) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        if (tarefas.get(id).getCriador().equals(usuario)) {
            return tarefas.get(id);
        } else {
            throw new IllegalArgumentException("Tarefa não vinculada ao usuário");
        }
    }

    public List<TarefaAbstrata> listarTarefasPorUsuario(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getCriador().equals(usuario)) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }

    public List<TarefaAbstrata> listarPorPrioridade(Prioridade prioridade, Usuario usuario) {
        if (prioridade == null || usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getPrioridade() == prioridade && tarefa.getCriador().equals(usuario)) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }
    
    // Método sobrecarregado para compatibilidade
    public List<TarefaAbstrata> listarPorPrioridade(Prioridade prioridade) {
        if (prioridade == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getPrioridade() == prioridade) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }

    public List<TarefaAbstrata> listarPorStatus(Status status, Usuario usuario) {
        if (status == null || usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getStatus() == status && tarefa.getCriador().equals(usuario)) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }

    public List<TarefaAbstrata> listarPorTipo(String tipo, Usuario usuario) {
        if (tipo == null || tipo.trim().isEmpty() || usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getTipo().equals(tipo.trim()) && tarefa.getCriador().equals(usuario)) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }
    
    // Método sobrecarregado para compatibilidade
    public List<TarefaAbstrata> listarPorTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getTipo().equals(tipo.trim())) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }

    public List<TarefaAbstrata> listarPorCategoria(Categoria categoria, Usuario usuario) {
        if (categoria == null || usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getCategoria() != null && 
                tarefa.getCategoria().equals(categoria) && 
                tarefa.getCriador().equals(usuario)) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }
    
    // Método sobrecarregado para compatibilidade
    public List<TarefaAbstrata> listarPorCategoria(Categoria categoria) {
        if (categoria == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getCategoria() != null && tarefa.getCategoria().equals(categoria)) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }

    // Métodos adicionais para compatibilidade com NegocioTarefa
    public List<TarefaAbstrata> buscarConcluidas(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getStatus() == Status.CONCLUIDA && tarefa.getCriador().equals(usuario)) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }
    
    public List<TarefaAbstrata> buscarPendentes(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getStatus() == Status.PENDENTE && tarefa.getCriador().equals(usuario)) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }
    
    public List<TarefaAbstrata> buscarAtrasadas(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            if (tarefa.getStatus() == Status.PENDENTE && 
                tarefa.getPrazo().isBefore(java.time.LocalDateTime.now()) && 
                tarefa.getCriador().equals(usuario)) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }
    
    public List<TarefaAbstrata> buscarDelegadasParaUsuario(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            // Verificar se é uma tarefa delegável (implementa a interface Delegavel)
            if (tarefa instanceof negocio.entidade.Delegavel) {
                negocio.entidade.Delegavel tarefaDelegavel = (negocio.entidade.Delegavel) tarefa;
                
                // Verificar se o usuário é responsável E se não é o criador
                if (tarefaDelegavel.ehResponsavel(usuario) && !tarefa.getCriador().equals(usuario)) {
                    resultado.add(tarefa);
                }
            }
        }
        return resultado;
    }

    public List<TarefaAbstrata> buscarDelegadasPeloUsuario(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            // Verificar se é uma tarefa delegável (implementa a interface Delegavel)
            if (tarefa instanceof negocio.entidade.Delegavel) {
                negocio.entidade.Delegavel tarefaDelegavel = (negocio.entidade.Delegavel) tarefa;
                
                // Verificar se o usuário é o criador E é responsável E há múltiplos responsáveis
                if (tarefa.getCriador().equals(usuario) &&
                    tarefaDelegavel.getResponsaveis().size() > 1) {
                    resultado.add(tarefa);
                }
            }
        }
        return resultado;
    }
    
    // Busca tarefas onde o usuário é responsável (incluindo criadas por ele)
    public List<TarefaAbstrata> buscarTarefasDoUsuario(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        
        List<TarefaAbstrata> resultado = new ArrayList<>();
        for (TarefaAbstrata tarefa : tarefas.values()) {
            // Verificar se é uma tarefa delegável (implementa a interface Delegavel)
            if (tarefa instanceof negocio.entidade.Delegavel) {
                negocio.entidade.Delegavel tarefaDelegavel = (negocio.entidade.Delegavel) tarefa;
                
                // Verificar se o usuário é responsável pela tarefa
                if (tarefaDelegavel.ehResponsavel(usuario)) {
                    resultado.add(tarefa);
                }
            }
        }
        return resultado;
    }
    
    public TarefaAbstrata buscarTarefaPorId(String id, Usuario usuario) {
        return buscarTarefaPorID(id, usuario);
    }

    public boolean atualizarTarefa(TarefaAbstrata tarefaAtualizada) {
        if (tarefaAtualizada == null) {
            throw new IllegalArgumentException("Tarefa não pode ser nula");
        }
        
        if (tarefas.containsKey(tarefaAtualizada.getId())) {
            tarefas.put(tarefaAtualizada.getId(), tarefaAtualizada);
            salvarDados();
            return true;
        }
        return false;
    }

    public boolean removerTarefa(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        boolean removido = tarefas.remove(id.trim()) != null;
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
    
    // Carrega dados do arquivo
    private void carregarDados() {
        try {
            List<TarefaAbstrata> tarefasCarregadas = persistencia.carregar();
            tarefas.clear();
            
            int maxIdTarefa = 0;
            for (TarefaAbstrata tarefa : tarefasCarregadas) {
                tarefas.put(tarefa.getId(), tarefa);
                
                // Encontrar o maior ID para sincronizar o contador
                int numeroId = GeradorId.extrairNumeroId(tarefa.getId());
                maxIdTarefa = Math.max(maxIdTarefa, numeroId);
            }
            
            // Sincronizar o contador para evitar IDs duplicados
            if (maxIdTarefa > 0) {
                GeradorId.sincronizarContadorTarefas(maxIdTarefa);
            }
            
            System.out.println("Carregadas " + tarefas.size() + " tarefas do arquivo");
            System.out.println("Contador de IDs sincronizado: " + maxIdTarefa);
            
        } catch (PersistenciaException e) {
            System.out.println("Erro ao carregar tarefas: " + e.getMessage());
        }
    }
    
    // Salva dados no arquivo
    private void salvarDados() {
        try {
            List<TarefaAbstrata> listaTarefas = new ArrayList<>(tarefas.values());
            persistencia.salvar(listaTarefas);
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }
    
    // Limpa todos os dados
    public void limparTodos() {
        try {
            tarefas.clear();
            persistencia.limparDados();
            System.out.println("Todos os dados de tarefas foram removidos");
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao limpar dados de tarefas: " + e.getMessage());
        }
    }

}
