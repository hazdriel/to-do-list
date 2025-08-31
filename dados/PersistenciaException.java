package dados;
import java.io.IOException;

// Exceção personalizada para erros de persistência
public class PersistenciaException extends IOException {
    
    // Construtor com mensagem e causa
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Construtor apenas com mensagem
    public PersistenciaException(String message) {
        super(message);
    }
}
