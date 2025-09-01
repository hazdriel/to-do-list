package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TarefaIDNaoPertece extends TarefaException {

    public TarefaIDNaoPertece(String id){
        super("A tarefa "+ id +" não foi criada por você e não tem permissão para acescá-la. Por favor, verifique se o ID da sua tarefa está correto e tente novamente.");
    }

}
