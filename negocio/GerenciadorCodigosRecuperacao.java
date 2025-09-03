package negocio;

import negocio.entidade.CodigoRecuperacao;
import java.util.HashMap;
import java.util.Map;

// Gerenciador de códigos de verificação para recuperação de senha
public class GerenciadorCodigosRecuperacao {
    private Map<String, CodigoRecuperacao> codigos = new HashMap<>();
    
    public String gerarCodigoParaEmail(String email) {
        codigos.remove(email);
        
        CodigoRecuperacao codigo = new CodigoRecuperacao(email);
        codigos.put(email, codigo);
        
        simularEnvioEmail(email, codigo.getCodigo());
        
        return codigo.getCodigo();
    }
    
    public boolean verificarCodigo(String email, String codigoDigitado) {
        CodigoRecuperacao codigo = codigos.get(email);
        if (codigo == null) return false;
        
        return codigo.verificarCodigo(codigoDigitado);
    }
    
    private void simularEnvioEmail(String email, String codigo) {
        System.out.println("📧 Email enviado para: " + email);
        System.out.println("🔑 Código de verificação: " + codigo);
        System.out.println("⏰ Este código expira em 5 minutos");
        System.out.println("ℹ️  Nota: Em um sistema real, este código seria enviado por email via API. Esta é apenas uma simulação para fins da disciplina.");
    }
}
