package negocio.excecao.usuario;

public class NomeApenasLetrasException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public NomeApenasLetrasException(String nome) {
        super("O nome "+ nome +" é inválido. Por favor, utilize apenas letras, sem números ou símbolos.");
    }

}
