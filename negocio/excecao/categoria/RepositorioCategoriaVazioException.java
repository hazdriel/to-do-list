package negocio.excecao.categoria;

public class RepositorioCategoriaVazioException extends CategoriaException  {
    private static final long serialVersionUID = 1L;

    public RepositorioCategoriaVazioException() {
        super("O repositório de categorias não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
