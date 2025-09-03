package dados;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Classe de persistência usando arquivos .dat
public class PersistenciaArquivo<T> {
    
    private final String nomeArquivo;
    private final String caminhoCompleto;
    
    public PersistenciaArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        this.caminhoCompleto = "dados_sistema/" + nomeArquivo + ".dat";
        
        File diretorio = new File("dados_sistema");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }
    
    public void salvar(List<T> entidades) throws PersistenciaException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminhoCompleto))) {
            oos.writeObject(entidades);
            
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao salvar dados em " + caminhoCompleto, e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<T> carregar() throws PersistenciaException {
        if (!arquivoExiste()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(caminhoCompleto))) {
            List<T> entidades = (List<T>) ois.readObject();
            return entidades;
            
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao carregar dados de " + caminhoCompleto, e);
        }
    }
    
    public boolean arquivoExiste() {
        return new File(caminhoCompleto).exists();
    }
    
    public void limparDados() throws PersistenciaException {
        try {
            File arquivo = new File(caminhoCompleto);
            if (arquivo.exists()) {
                if (!arquivo.delete()) {
                    throw new PersistenciaException("Não foi possível remover o arquivo " + caminhoCompleto);
                }
            }
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao limpar dados de " + caminhoCompleto, e);
        }
    }
}

