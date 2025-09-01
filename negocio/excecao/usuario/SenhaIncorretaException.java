package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class SenhaIncorretaException extends UsuarioException {

    public SenhaIncorretaException(String email) {
        super("A senha fornecida não corresponde à conta "+ email +". Por favor, verifique se a senha está correta e tente novamente.");
    }
}
