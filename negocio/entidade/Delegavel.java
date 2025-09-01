package negocio.entidade;

import java.util.List;

// Interface que define capacidades de delegação para tarefas
// Permite que tarefas sejam delegadas para outros usuários
public interface Delegavel {
    
    Usuario getResponsavelOriginal();
    List<Usuario> getResponsaveis();
    List<RegistroDelegacao> getHistoricoDelegacoes();
    Status getStatus();
    Usuario getCriador();

    default void setResponsaveis(List<Usuario> responsaveis) 
            throws IllegalArgumentException {
        if (responsaveis == null) {
            throw new IllegalArgumentException("Lista de responsáveis não pode ser nula");
        }
        
        for (Usuario usuario : responsaveis) {
            if (usuario == null) {
                throw new IllegalArgumentException("Lista contém usuário nulo");
            }
        }
        
        List<Usuario> responsaveisAtuais = getResponsaveis();
        responsaveisAtuais.clear();
        responsaveisAtuais.addAll(responsaveis);
    }
    
    default void setHistoricoDelegacao(List<RegistroDelegacao> historicoDelegacoes) 
            throws IllegalArgumentException {
        if (historicoDelegacoes == null) {
            throw new IllegalArgumentException("Histórico de delegações não pode ser nulo");
        }
        
        for (RegistroDelegacao registro : historicoDelegacoes) {
            if (registro == null) {
                throw new IllegalArgumentException("Histórico contém registro nulo");
            }
        }
        
        List<RegistroDelegacao> historicoAtual = getHistoricoDelegacoes();
        historicoAtual.clear();
        historicoAtual.addAll(historicoDelegacoes);
    }
    
    default void adicionarResponsavel(Usuario usuario) 
            throws IllegalArgumentException, IllegalStateException {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        
        List<Usuario> responsaveis = getResponsaveis();
        if (responsaveis.contains(usuario)) {
            throw new IllegalStateException("Usuário já é responsável por esta tarefa");
        }
        
        responsaveis.add(usuario);
    }
    
    default void removerResponsavel(Usuario usuario) 
            throws IllegalArgumentException, IllegalStateException {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        
        if (usuario.equals(getResponsavelOriginal())) {
            throw new IllegalStateException("Não é possível remover o responsável original");
        }
        
        List<Usuario> responsaveis = getResponsaveis();
        if (!responsaveis.contains(usuario)) {
            throw new IllegalStateException("Usuário não é responsável por esta tarefa");
        }
        
        responsaveis.remove(usuario);
    }
    
    default void registrarDelegacao(Usuario responsavel, String motivo) 
            throws IllegalArgumentException, IllegalStateException {
        if (responsavel == null) {
            throw new IllegalArgumentException("Responsável não pode ser nulo");
        }
        
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Motivo da delegação não pode ser vazio");
        }
        
        if (!podeSerDelegada()) {
            throw new IllegalStateException("Tarefa não pode ser delegada no status atual");
        }
        
        if (!ehResponsavel(responsavel)) {
            throw new IllegalStateException("Usuário deve ser responsável pela tarefa para registrar delegação");
        }
        
        RegistroDelegacao registro = new RegistroDelegacao(getCriador(), responsavel, motivo);
        getHistoricoDelegacoes().add(registro);
    }
    
    default boolean podeSerDelegada() {
        return getStatus() == Status.PENDENTE || getStatus() == Status.EM_PROGRESSO;
    }
    
    default void delegarPara(Usuario novoResponsavel) 
            throws IllegalArgumentException, IllegalStateException {
        if (novoResponsavel == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        
        if (!podeSerDelegada()) {
            throw new IllegalStateException("Tarefa não pode ser delegada no status atual");
        }
        
        adicionarResponsavel(novoResponsavel);
        registrarDelegacao(novoResponsavel, "Delegação direta");
    }
    
    default void delegarParaEquipe(List<Usuario> equipe, String motivo) 
            throws IllegalArgumentException, IllegalStateException {
        if (equipe == null || equipe.isEmpty()) {
            throw new IllegalArgumentException("Equipe não pode ser vazia");
        }
        
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Motivo da delegação para equipe não pode ser vazio");
        }
        
        if (!podeSerDelegada()) {
            throw new IllegalStateException("Tarefa não pode ser delegada no status atual");
        }
        
        for (Usuario membro : equipe) {
            if (membro == null) {
                throw new IllegalArgumentException("Equipe contém usuário nulo");
            }
            adicionarResponsavel(membro);
            registrarDelegacao(membro, "Delegação para equipe: " + motivo);
        }
    }
    
    default void removerDelegacao(Usuario responsavel) 
            throws IllegalArgumentException {
        if (responsavel == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        removerResponsavel(responsavel);
    }
    
    default boolean ehResponsavel(Usuario usuario) {
        if (usuario == null) return false;
        return getResponsaveis().stream().anyMatch(resp -> resp.equals(usuario));
    }
    
    default int getNumeroResponsaveis() {
        return getResponsaveis().size();
    }
    
    default boolean temMultiplosResponsaveis() {
        return getNumeroResponsaveis() > 1;
    }
}
