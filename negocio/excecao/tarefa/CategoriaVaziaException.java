package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class CategoriaVaziaException extends TarefaException {

    public CategoriaVaziaException(){
        super("A categoria não pode ser vazia. Por favor, preencha-a.");
    }

}