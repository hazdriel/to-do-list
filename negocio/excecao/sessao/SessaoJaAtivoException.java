package negocio.excecao.sessao;

@SuppressWarnings("serial")
public class SessaoJaAtivoException extends SessaoException {

    public SessaoJaAtivoException() {
        super("Há um usuário logado. Por favor, tente novamente.");
    }

}
