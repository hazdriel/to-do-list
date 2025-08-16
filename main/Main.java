package main;
import dados.RepositorioTarefas;
import dados.RepositorioUsuarios;
import iu.TelaMenu;
import negocio.NegocioSessao;
import negocio.NegocioTarefa;
import negocio.NegocioUsuario;

public class Main {
  public static void main(String[] args) {
    
    RepositorioTarefas repositorioTarefas = new RepositorioTarefas();
    RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios();

    NegocioSessao sessao = new NegocioSessao();
    NegocioTarefa tarefaService = new NegocioTarefa(repositorioTarefas, repositorioUsuarios, sessao);
    NegocioUsuario usuarioService = new NegocioUsuario(repositorioUsuarios, sessao);

    usuarioService.cadastrarUsuario("Teste", "teste@teste.com", "teste");

    TelaMenu menu = new TelaMenu(tarefaService, usuarioService, sessao);

    menu.mostrarMenu();

  }
}
