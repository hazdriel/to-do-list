package negocio.excecao.usuario;

public class SenhaTamanhoInvalidoException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public SenhaTamanhoInvalidoException() {
        super("o tamanho da senha é inválido. Por favor, utilize um tamanho entre 6 e 50 caracteres.");
    }

}
