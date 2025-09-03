package negocio.excecao.usuario;

public class SenhaVaziaException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public SenhaVaziaException() {
        super("A senha não pode ser vazia. Por favor, preencha-a.");
    }

}
