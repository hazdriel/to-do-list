package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class NomeVazioException extends UsuarioException {

    public NomeVazioException() {
        super("O nome não pode ser vazio. Por favor, preencha-o.");
    }

}
