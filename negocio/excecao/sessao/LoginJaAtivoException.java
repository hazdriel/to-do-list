package negocio.excecao.sessao;

public class LoginJaAtivoException extends SessaoException  {
    private static final long serialVersionUID = 1L;

    public LoginJaAtivoException() {
        super("Há um usuário logado. Por favor, faça logout e depois tente novamente.");
    }

}
