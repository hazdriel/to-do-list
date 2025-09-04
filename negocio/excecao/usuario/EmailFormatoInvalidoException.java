package negocio.excecao.usuario;

public class EmailFormatoInvalidoException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public EmailFormatoInvalidoException(String email) {
        super("o e-mail "+ email +" é inválido. Por favor, utilize a formatação padrão de e-mail.");
    }

}
