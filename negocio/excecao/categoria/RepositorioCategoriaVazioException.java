package negocio.excecao.categoria;

@SuppressWarnings("serial")
public class RepositorioCategoriaVazioException extends CategoriaException {

    public RepositorioCategoriaVazioException() {
        super("O repositório de categorias não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
