package negocio.excecao.categoria;


public class CategoriaAtivaRemocaoException extends CategoriaException  {

    private static final long serialVersionUID = 1L;

    public CategoriaAtivaRemocaoException(String categoria) {
        super("a "+categoria+" não pode ser removida. Somente categorias inativas ou não padrões podem ser removidas. " +
                "Por favor, verifique se há tarefas ativas e seu tipo.");
    }

}
