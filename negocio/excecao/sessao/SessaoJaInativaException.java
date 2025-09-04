package negocio.excecao.sessao;

public class SessaoJaInativaException extends SessaoException  {
    private static final long serialVersionUID = 1L;

    public SessaoJaInativaException() {
        super("não usuário algum logado. Por favor, tente novamente.");
    }

}
