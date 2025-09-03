package negocio.excecao.sessao;

public class SessaoException extends Exception {

    private static final long serialVersionUID = 1L;
    private String msg;

    public SessaoException(String msg) {
        super(msg);
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
