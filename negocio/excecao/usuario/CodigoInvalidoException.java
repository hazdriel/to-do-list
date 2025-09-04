package negocio.excecao.usuario;

public class CodigoInvalidoException extends UsuarioException {
    private static final long serialVersionUID = 1L;

    public CodigoInvalidoException() {
        super("o código de verificação inválido ou expirado. Por favor, tente novamente.");
    }
}
