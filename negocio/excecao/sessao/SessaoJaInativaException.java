package negocio.excecao.sessao;

@SuppressWarnings("serial")
public class SessaoJaInativaException extends SessaoException {

    public SessaoJaInativaException() {
        super("Nenhum usuário está logado. Por favor, tente novamente.");
    }

}
