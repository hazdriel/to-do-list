package negocio.excecao.sessao;

@SuppressWarnings("serial")
public class SessaoException extends Exception {

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
