package dados;
import java.io.IOException;

// Exceção personalizada para erros de persistência de dados
public class PersistenciaException extends IOException {
    
    public PersistenciaException(String message, Throwable causa) {
        super(message, causa);
    }
    
    public PersistenciaException(String message) {
        super(message);
    }
}
