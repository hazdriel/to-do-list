package negocio.excecao.categoria;

public class CategoriaVaziaException extends CategoriaException  {
    private static final long serialVersionUID = 1L;

    public CategoriaVaziaException(){
        super("A categoria não pode ser vazia. Por favor, preencha-a.");
    }

}
