package negocio.excecao.usuario;

public class UsuarioExistenteException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public UsuarioExistenteException(String email) {
        super("o e-mail "+ email +" já está vinculado a outra conta. Por favor, utilize outro e-mail ou recupere o acesso à conta existente.");
    }

}
