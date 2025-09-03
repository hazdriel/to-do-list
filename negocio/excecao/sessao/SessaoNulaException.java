package negocio.excecao.sessao;

public class SessaoNulaException extends SessaoException  {
    private static final long serialVersionUID = 1L;

    public SessaoNulaException() {
        super("A sessão não pode ser vazia ou nula. Por favor, preencha-a.");
    }

}
