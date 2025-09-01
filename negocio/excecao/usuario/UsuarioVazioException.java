package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class UsuarioVazioException extends UsuarioException {

    public UsuarioVazioException() {
        super("O usuário não pode ser vazio. Por favor, preencha-o.");
    }
}
