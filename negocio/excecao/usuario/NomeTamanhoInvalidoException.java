package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class NomeTamanhoInvalidoException extends UsuarioException {

    public NomeTamanhoInvalidoException(String nome) {
        super("O tamanho do nome "+ nome +" é inválido. Por favor, utilize entre 2 e 100 caracteres.");
    }

}
