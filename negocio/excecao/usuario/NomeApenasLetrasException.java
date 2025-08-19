package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class NomeApenasLetrasException extends UsuarioException {

    public NomeApenasLetrasException(String nome) {
        super("O nome "+ nome +" é inválido. Por favor, utilize apenas letras, sem números ou símbolos.");
    }

}
