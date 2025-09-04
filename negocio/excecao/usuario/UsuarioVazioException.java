package negocio.excecao.usuario;

public class UsuarioVazioException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public UsuarioVazioException() {
        super("o usuário não pode ser vazio. Por favor, preencha-o.");
    }
}
