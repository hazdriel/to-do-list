package negocio.excecao.sessao;

@SuppressWarnings("serial")
public class LoginJaAtivoException extends SessaoException {

    public LoginJaAtivoException() {
        super("Há um usuário logado. Por favor, faça logout e depois tente novamente.");
    }

}
