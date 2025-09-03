package negocio.excecao.tarefa;

import java.time.LocalDateTime;

public class PrazoPassadoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public PrazoPassadoException(LocalDateTime dataInicio) {
        super("O prazo "+ dataInicio +" é anterior a data atual. Por favor, insira um prazo válido.");
    }

}
