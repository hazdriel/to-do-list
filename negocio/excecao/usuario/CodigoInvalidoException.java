package negocio.excecao.usuario;

public class CodigoInvalidoException extends UsuarioException {
    private static final long serialVersionUID = 1L;

    public CodigoInvalidoException() {
        super("Código de verificação inválido ou expirado. Tente novamente.");
    }
}
