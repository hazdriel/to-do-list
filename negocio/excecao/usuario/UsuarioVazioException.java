package negocio.excecao.usuario;

public class UsuarioVazioException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public UsuarioVazioException() {
        super("O usuário não pode ser vazio. Por favor, preencha-o.");
    }
}
