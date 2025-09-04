package negocio.excecao.categoria;


public class CategoriaNaoEncontrada extends CategoriaException {

    public CategoriaNaoEncontrada(String categoria) {
        super("a categoria "+categoria+" não foi encontrada. Por favor, verifique o nome.");
    }

}
