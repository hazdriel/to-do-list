package negocio.excecao.usuario;

public class NomeTamanhoInvalidoException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public NomeTamanhoInvalidoException(String nome) {
        super("o tamanho do nome "+ nome +" é inválido. Por favor, utilize entre 2 e 100 caracteres.");
    }

}

