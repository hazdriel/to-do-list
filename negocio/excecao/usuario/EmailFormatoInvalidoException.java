package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class EmailFormatoInvalidoException extends UsuarioException {

    public EmailFormatoInvalidoException(String email) {
        super("O e-mail "+ email +" é inválido. Por favor, utilize a formatação padrão de e-mail.");
    }
}
