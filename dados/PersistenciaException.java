package dados;
import java.io.IOException;

public class PersistenciaException extends IOException {
    
    public PersistenciaException(String message, Throwable causa) {
        super(message, causa);
    }
    
    public PersistenciaException(String message) {
        super(message);
    }
}
