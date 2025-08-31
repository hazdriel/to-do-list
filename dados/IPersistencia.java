package dados;

import java.util.List;

// Interface que define o contrato para operações de persistência
public interface IPersistencia<T> {
    
    // Salva uma lista de entidades
    void salvar(List<T> entidades) throws PersistenciaException;
    
    // Carrega uma lista de entidades
    List<T> carregar() throws PersistenciaException;
    
    // Verifica se o arquivo de persistência existe
    boolean arquivoExiste();
    
    // Limpa todos os dados persistidos
    void limparDados() throws PersistenciaException;
}
