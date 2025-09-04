package negocio.excecao.usuario;

public class RepositorioUsuariosVazioException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public RepositorioUsuariosVazioException() {
        super("o repositótio de usuários não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
