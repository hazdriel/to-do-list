package negocio.excecao.usuario;

public class EmailVazioException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public EmailVazioException() {
        super("o e-mail não pode ser vazio. Por favor, preencha-o.");
    }

}