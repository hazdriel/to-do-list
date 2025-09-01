package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class RepositorioUsuariosVazioException extends UsuarioException {

    public RepositorioUsuariosVazioException() {
        super("O repositótio de usuários não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
