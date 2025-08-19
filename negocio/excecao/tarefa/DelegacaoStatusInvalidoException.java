package negocio.excecao.tarefa;

import negocio.entidade.Status;

@SuppressWarnings("serial")
public class DelegacaoStatusInvalidoException extends TarefaException {

    public DelegacaoStatusInvalidoException(String titulo, Status status){
        super("A tarefa "+ titulo +" não pode ser delegada, devido seu status "+ status+". Somente tarefas pendentes podem ser delegadas. Por favor, altere status ou insira uma tarefa válida e tente novamente.");
    }
}
