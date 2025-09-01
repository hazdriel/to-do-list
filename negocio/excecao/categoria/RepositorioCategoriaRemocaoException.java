package negocio.excecao.categoria;

@SuppressWarnings("serial")
public class RepositorioCategoriaRemocaoException extends CategoriaException {

    public RepositorioCategoriaRemocaoException() {
        super("Error ao remover do repositório. Tente novamente.");
    }

}
