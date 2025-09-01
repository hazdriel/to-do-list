package negocio.excecao.categoria;

@SuppressWarnings("serial")
public class CategoriaNaoEncontrada extends CategoriaException {

    public CategoriaNaoEncontrada(String categoria) {
        super("A categoria "+categoria+" não foi encontrada. Por favor, verifique o nome.");
    }

}
