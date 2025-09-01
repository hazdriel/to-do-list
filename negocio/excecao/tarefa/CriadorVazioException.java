package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class CriadorVazioException extends TarefaException {

    public CriadorVazioException() {
        super("O criador não pode ser vazio. Por favor, preencha-o.");
    }

}
