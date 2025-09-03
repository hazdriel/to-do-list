package negocio.entidade;

import java.time.LocalDateTime;

// Classe para gerenciar códigos de verificação de recuperação de senha
public class CodigoRecuperacao {
    private String email;
    private String codigo;
    private LocalDateTime dataExpiracao;
    private boolean usado;
    
    public CodigoRecuperacao(String email) {
        this.email = email;
        this.codigo = gerarCodigo();
        this.dataExpiracao = LocalDateTime.now().plusMinutes(5);
        this.usado = false;
    }
    
    private String gerarCodigo() {
        return String.valueOf((int)(Math.random() * 9000) + 1000);
    }
    
    public boolean isValido() {
        return !usado && LocalDateTime.now().isBefore(dataExpiracao);
    }
    
    public boolean verificarCodigo(String codigoDigitado) {
        if (isValido() && this.codigo.equals(codigoDigitado)) {
            this.usado = true;
            return true;
        }
        return false;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getEmail() {
        return email;
    }
}
