package negocio.excecao.usuario;

public class UsuarioException extends Exception {

    private static final long serialVersionUID = 1L;
    private String msg;

    public UsuarioException(String msg) {
        super(msg);
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
