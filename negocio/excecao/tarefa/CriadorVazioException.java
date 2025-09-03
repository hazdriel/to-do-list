package negocio.excecao.tarefa;

public class CriadorVazioException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public CriadorVazioException() {
        super("O criador não pode ser vazio. Por favor, preencha-o.");
    }

}
