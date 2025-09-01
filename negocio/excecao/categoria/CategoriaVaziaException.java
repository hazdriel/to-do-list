package negocio.excecao.categoria;

@SuppressWarnings("serial")
public class CategoriaVaziaException extends CategoriaException {

    public CategoriaVaziaException(){
        super("A categoria não pode ser vazia. Por favor, preencha-a.");
    }

}
