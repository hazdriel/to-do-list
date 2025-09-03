package negocio.excecao.categoria;

public class CategoriaException extends Exception {

    private static final long serialVersionUID = 1L;
    private String msg;

    public CategoriaException(String msg) {
        super(msg);
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
