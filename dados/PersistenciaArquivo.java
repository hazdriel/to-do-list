package dados;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Implementação de persistência usando arquivos .dat
public class PersistenciaArquivo<T> implements IPersistencia<T> {
    
    private final String nomeArquivo;
    private final String caminhoCompleto;
    
    // Construtor que define o nome do arquivo
    public PersistenciaArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        this.caminhoCompleto = "dados_sistema/" + nomeArquivo + ".dat";
        
        // Criar diretório se não existir
        File diretorio = new File("dados_sistema");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }
    
    @Override
    public void salvar(List<T> entidades) throws PersistenciaException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminhoCompleto))) {
            oos.writeObject(entidades);
            System.out.println("Dados salvos com sucesso em: " + caminhoCompleto);
            
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao salvar dados em " + caminhoCompleto, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<T> carregar() throws PersistenciaException {
        if (!arquivoExiste()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(caminhoCompleto))) {
            List<T> entidades = (List<T>) ois.readObject();
            System.out.println("Dados carregados com sucesso de: " + caminhoCompleto);
            return entidades;
            
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao carregar dados de " + caminhoCompleto, e);
        }
    }
    
    @Override
    public boolean arquivoExiste() {
        return new File(caminhoCompleto).exists();
    }
    
    @Override
    public void limparDados() throws PersistenciaException {
        try {
            File arquivo = new File(caminhoCompleto);
            if (arquivo.exists()) {
                if (arquivo.delete()) {
                    System.out.println("Arquivo " + caminhoCompleto + " removido com sucesso");
                } else {
                    throw new PersistenciaException("Não foi possível remover o arquivo " + caminhoCompleto);
                }
            }
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao limpar dados de " + caminhoCompleto, e);
        }
    }
}
