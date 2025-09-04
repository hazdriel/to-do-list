package negocio.excecao.categoria;

public class CategoriaNaoPertenceException extends CategoriaException  {
    private static final long serialVersionUID = 1L;

    public CategoriaNaoPertenceException(String categoria) {
        super("a "+categoria+" não pode ser removida. Somente categorias criadas por você podem ser removidas. " +
                "Por favor, verifique seu criador.");
    }

}
