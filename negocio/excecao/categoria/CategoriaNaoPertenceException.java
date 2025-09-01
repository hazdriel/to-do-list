package negocio.excecao.categoria;

@SuppressWarnings("serial")
public class CategoriaNaoPertenceException extends CategoriaException {

    public CategoriaNaoPertenceException(String categoria) {
        super("A "+categoria+" não pode ser removida. Somente categorias criadas por você podem ser removidas. " +
                "Por favor, verifique seu criador.");
    }

}
