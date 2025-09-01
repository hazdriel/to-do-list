package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class EmailVazioException extends UsuarioException {

    public EmailVazioException() {
        super("O e-mail não pode ser vazio. Por favor, preencha-o.");
    }

}