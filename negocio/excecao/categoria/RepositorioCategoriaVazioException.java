package negocio.excecao.categoria;

public class RepositorioCategoriaVazioException extends CategoriaException  {
    private static final long serialVersionUID = 1L;

    public RepositorioCategoriaVazioException() {
        super("o repositório de categorias não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
