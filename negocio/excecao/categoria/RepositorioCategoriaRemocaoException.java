package negocio.excecao.categoria;

public class RepositorioCategoriaRemocaoException extends CategoriaException  {
    private static final long serialVersionUID = 1L;

    public RepositorioCategoriaRemocaoException() {
        super("Error ao remover do repositório. Tente novamente.");
    }

}
