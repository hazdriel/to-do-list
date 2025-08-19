package main;
import dados.RepositorioTarefas;
import dados.RepositorioUsuarios;
import iu.TelaMenu;
import negocio.NegocioSessao;
import negocio.NegocioTarefa;
import negocio.NegocioUsuario;
import negocio.excecao.tarefa.*;
import negocio.excecao.usuario.*;

public class Main {
  public static void main(String[] args) throws EmailVazioException, SenhaTamanhoInvalidoException, UsuarioExistenteException, NomeApenasLetrasException, NomeTamanhoInvalidoException, NomeVazioException, EmailFormatoInvalidoException, UsuarioNaoEncontradoException, SenhaIncorretaException, CategoriaVaziaException, PrazoInvalidoException, TarefaIDNaoEncontradaException, TarefaIDNaoPertece, TituloVazioException {
    
    RepositorioTarefas repositorioTarefas = new RepositorioTarefas();
    RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios();

    NegocioSessao sessao = new NegocioSessao();
    NegocioTarefa tarefaService = new NegocioTarefa(repositorioTarefas, repositorioUsuarios, sessao);
    NegocioUsuario usuarioService = new NegocioUsuario(repositorioUsuarios, sessao);

    usuarioService.cadastrarUsuario("Teste", "teste@teste.com", "teste1234");

    TelaMenu menu = new TelaMenu(tarefaService, usuarioService, sessao);

    menu.mostrarMenu();

  }
}
