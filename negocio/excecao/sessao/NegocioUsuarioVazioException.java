package negocio.excecao.sessao;

public class NegocioUsuarioVazioException extends SessaoException  {
    private static final long serialVersionUID = 1L;

    public NegocioUsuarioVazioException() {
        super("NegocioUsuario não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
