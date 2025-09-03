package negocio.excecao.tarefa;


public class TarefaException extends Exception  {

    private static final long serialVersionUID = 1L;
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
