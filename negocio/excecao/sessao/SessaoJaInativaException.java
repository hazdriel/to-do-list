package negocio.excecao.sessao;

public class SessaoJaInativaException extends SessaoException  {
    private static final long serialVersionUID = 1L;

    public SessaoJaInativaException() {
        super("Nenhum usuário está logado. Por favor, tente novamente.");
    }

}
