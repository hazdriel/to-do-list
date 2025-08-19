package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class SenhaVaziaException extends UsuarioException {

    public SenhaVaziaException() {
        super("A senha não pode ser vazia. Por favor, preencha-a.");
    }

}
