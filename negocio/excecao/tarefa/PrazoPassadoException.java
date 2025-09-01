package negocio.excecao.tarefa;

import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class PrazoPassadoException extends TarefaException {

    public PrazoPassadoException(LocalDateTime dataInicio) {
        super("O prazo "+ dataInicio +" é anterior a data atual. Por favor, insira um prazo válido.");
    }

}
