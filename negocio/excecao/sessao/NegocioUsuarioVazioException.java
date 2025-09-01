package negocio.excecao.sessao;

@SuppressWarnings("serial")
public class NegocioUsuarioVazioException extends SessaoException {

    public NegocioUsuarioVazioException() {
        super("NegocioUsuario não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
