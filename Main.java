import repository.TarefaRepository;
import repository.UsuarioRepository;
import service.SessaoService;
import service.TarefaService;
import service.UsuarioService;
import view.Menu;

public class Main {
  public static void main(String[] args) {
    
    TarefaRepository repositorioTarefas = new TarefaRepository();
    UsuarioRepository repositorioUsuarios = new UsuarioRepository();

    SessaoService sessao = new SessaoService();
    TarefaService tarefaService = new TarefaService(repositorioTarefas, repositorioUsuarios, sessao);
    UsuarioService usuarioService = new UsuarioService(repositorioUsuarios, sessao);

    usuarioService.cadastrarUsuario("Teste", "teste@teste.com", "teste");

    Menu menu = new Menu(tarefaService, usuarioService, sessao);

    menu.mostrarMenu();

  }
}
