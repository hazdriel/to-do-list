package negocio.entidade;

import negocio.excecao.tarefa.*;

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
            throws DelegacaoListaVaziaException, DelegacaoResponsavelVazioException {
        if (responsaveis == null) {
            throw new DelegacaoListaVaziaException();
        }
        
        for (Usuario usuario : responsaveis) {
            if (usuario == null) {
                throw new DelegacaoResponsavelVazioException();
            }
        }
        
        List<Usuario> responsaveisAtuais = getResponsaveis();
        responsaveisAtuais.clear();
        responsaveisAtuais.addAll(responsaveis);
    }
    
    default void setHistoricoDelegacao(List<RegistroDelegacao> historicoDelegacoes) 
            throws DelegacaoHistoricoVazioException {
        if (historicoDelegacoes == null) {
            throw new DelegacaoHistoricoVazioException();
        }
        
        for (RegistroDelegacao registro : historicoDelegacoes) {
            if (registro == null) {
                throw new DelegacaoHistoricoVazioException();
            }
        }
        
        List<RegistroDelegacao> historicoAtual = getHistoricoDelegacoes();
        historicoAtual.clear();
        historicoAtual.addAll(historicoDelegacoes);
    }
    
    default void adicionarResponsavel(Usuario usuario)
            throws DelegacaoResponsavelVazioException, DelegacaoResponsavelInvalidoException {
        if (usuario == null) {
            throw new DelegacaoResponsavelVazioException();
        }
        
        List<Usuario> responsaveis = getResponsaveis();
        if (responsaveis.contains(usuario)) {
            throw new DelegacaoResponsavelInvalidoException(usuario);
        }
        
        responsaveis.add(usuario);
    }
    
    default void removerResponsavel(Usuario usuario)
            throws DelegacaoRemoverResponsavelException, DelegacaoResponsavelInvalidoException, DelegacaoResponsavelVazioException {
        if (usuario == null) {
            throw new DelegacaoResponsavelVazioException();
        }
        
        if (usuario.equals(getResponsavelOriginal())) {
            throw new DelegacaoRemoverResponsavelException(usuario);
        }
        
        List<Usuario> responsaveis = getResponsaveis();
        if (!responsaveis.contains(usuario)) {
            throw new DelegacaoResponsavelInvalidoException(usuario);
        }
        
        responsaveis.remove(usuario);
    }
    
    default void registrarDelegacao(Usuario responsavel, String motivo)
            throws DelegacaoResponsavelVazioException, DelegacaoMotivoException, DelegacaoStatusInvalidoException, DelegacaoRegistroInvalidoException, CriadorVazioException {
        if (responsavel == null) {
            throw new DelegacaoResponsavelVazioException();
        }
        
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new DelegacaoMotivoException();
        }
        
        if (!podeSerDelegada()) {
            throw new DelegacaoStatusInvalidoException();
        }
        
        if (!ehResponsavel(responsavel)) {
            throw new DelegacaoRegistroInvalidoException();
        }
        
        RegistroDelegacao registro = new RegistroDelegacao(getCriador(), responsavel, motivo);
        getHistoricoDelegacoes().add(registro);
    }
    
    default boolean podeSerDelegada() {
        return getStatus() == Status.PENDENTE || getStatus() == Status.EM_PROGRESSO;
    }
    
    default void delegarPara(Usuario novoResponsavel)
            throws DelegacaoStatusInvalidoException, DelegacaoResponsavelInvalidoException, DelegacaoMotivoException,
            DelegacaoRegistroInvalidoException, DelegacaoResponsavelVazioException, CriadorVazioException {
        if (novoResponsavel == null) {
            throw new DelegacaoResponsavelVazioException();
        }
        
        if (!podeSerDelegada()) {
            throw new DelegacaoStatusInvalidoException();
        }
        
        adicionarResponsavel(novoResponsavel);
        registrarDelegacao(novoResponsavel, "Delegação direta");
    }
    
    default void delegarParaEquipe(List<Usuario> equipe, String motivo)
            throws DelegacaoListaVaziaException, DelegacaoMotivoException, DelegacaoStatusInvalidoException, DelegacaoResponsavelVazioException,
            DelegacaoRegistroInvalidoException, DelegacaoResponsavelInvalidoException, CriadorVazioException {
        if (equipe == null || equipe.isEmpty()) {
            throw new DelegacaoListaVaziaException();
        }
        
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new DelegacaoMotivoException();
        }
        
        if (!podeSerDelegada()) {
            throw new DelegacaoStatusInvalidoException();
        }
        
        for (Usuario membro : equipe) {
            if (membro == null) {
                throw new DelegacaoResponsavelVazioException();
            }
            adicionarResponsavel(membro);
            registrarDelegacao(membro, "Delegação para equipe: " + motivo);
        }
    }
    
    default void removerDelegacao(Usuario responsavel)
            throws DelegacaoResponsavelVazioException, DelegacaoRemoverResponsavelException, DelegacaoResponsavelInvalidoException {
        if (responsavel == null) {
            throw new DelegacaoResponsavelVazioException();
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
