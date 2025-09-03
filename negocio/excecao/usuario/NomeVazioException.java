package negocio.excecao.usuario;

public class NomeVazioException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public NomeVazioException() {
        super("O nome não pode ser vazio. Por favor, preencha-o.");
    }

}