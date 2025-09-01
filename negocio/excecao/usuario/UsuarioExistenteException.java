package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class UsuarioExistenteException extends UsuarioException {

    public UsuarioExistenteException(String email) {
        super("O e-mail "+ email +" já está vinculado a outra conta. Por favor, utilize outro e-mail ou recupere o acesso à conta existente.");
    }
}
