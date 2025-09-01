package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class SenhaTamanhoInvalidoException extends UsuarioException {

    public SenhaTamanhoInvalidoException() {
        super("O tamanho da senha é inválido. Por favor, utilize um tamanho entre 6 e 50 caracteres.");
    }

}
