package negocio.excecao.usuario;

public class NomeVazioException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public NomeVazioException() {
        super("o nome não pode ser vazio. Por favor, preencha-o.");
    }

}