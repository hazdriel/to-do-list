package negocio.excecao.categoria;

@SuppressWarnings("serial")
public class CategoriaException extends Exception {

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
