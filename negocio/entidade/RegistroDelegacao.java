package negocio.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;

// Representa um registro de delegação de tarefa

public class RegistroDelegacao implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Usuario delegador;
    private final Usuario delegado;
    private final LocalDateTime dataDelegacao;
    private final String motivo;
    
    public RegistroDelegacao(Usuario delegador, Usuario delegado, String motivo) {
        if (delegador == null) {
            throw new IllegalArgumentException("Delegador não pode ser nulo");
        }
        if (delegado == null) {
            throw new IllegalArgumentException("Delegado não pode ser nulo");
        }
        
        this.delegador = delegador;
        this.delegado = delegado;
        this.dataDelegacao = LocalDateTime.now();
        this.motivo = motivo;
    }
    
    public Usuario getDelegador() {
        return delegador;
    }
    
    public Usuario getDelegado() {
        return delegado;
    }
    
    public LocalDateTime getDataDelegacao() {
        return dataDelegacao;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public boolean temMotivo() {
        return !motivo.isEmpty();
    }
    
    @Override
    public String toString() {
        String motivoStr = temMotivo() ? " (Motivo: " + motivo + ")" : "";
        return String.format("%s delegou para %s em %s%s", 
                           delegador.getNome(), 
                           delegado.getNome(), 
                           dataDelegacao.toString(),
                           motivoStr);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RegistroDelegacao that = (RegistroDelegacao) obj;
        return delegador.equals(that.delegador) && 
               delegado.equals(that.delegado) && 
               dataDelegacao.equals(that.dataDelegacao);
    }
    
    @Override
    public int hashCode() {
        return delegador.hashCode() + delegado.hashCode() + dataDelegacao.hashCode();
    }
}
