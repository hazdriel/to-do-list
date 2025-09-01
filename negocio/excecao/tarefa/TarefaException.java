package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TarefaException extends Exception {

    private String msg;

    public TarefaException(String msg) {
        super(msg);
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
