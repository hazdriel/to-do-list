package negocio.excecao.sessao;

@SuppressWarnings("serial")
public class SessaoNulaException extends SessaoException {

    public SessaoNulaException() {
        super("A sessão não pode ser vazia ou nula. Por favor, preencha-a.");
    }

}
