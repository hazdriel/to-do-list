package view;

import model.Tarefa;
import repository.TarefaRepository;
import repository.UsuarioRepository;
import service.AdminTarefas;

import java.util.Scanner;

public class Menu {
    Scanner sc = new Scanner(System.in);

    private TarefaRepository tarefaRep;
    private UsuarioRepository usuarioRep;
    private AdminTarefas admin;

    public Menu() {
        this.tarefaRep = new TarefaRepository();
        this.usuarioRep = new UsuarioRepository();
        this.admin = new AdminTarefas(tarefaRep, usuarioRep);
    }

    public void menu(){
        int opcao;
        do{
            System.out.println("------- Menu -------");
            System.out.println("1-> Criar tarefa");
            System.out.println("2-> Listar tarefa(s)");
            System.out.println("3-> Buscar tarefa");
            System.out.println("4-> Atualizar tarefa");
            System.out.println("5-> Atualizar usuário");
            System.out.println("0-> Sair");
            System.out.println("--------------------");
            System.out.print("Opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao){
                case 1 -> admin.criarTarefa();
                case 2 -> tarefaRep.listarTarefas();
                case 3 -> {
                    System.out.print("-> ID da tarefa: ");
                    String id = sc.nextLine();
                    Tarefa t = tarefaRep.buscarTarefaPorID(id);
                    if (t != null) {
                        t.exibirTarefa();
                    } else {
                        System.out.println("Tarefa não encontrada.");
                    }
                }
                case 4 -> {
                    System.out.print("-> ID da tarefa: ");
                    String id = sc.nextLine();
                    Tarefa t = tarefaRep.buscarTarefaPorID(id);
                    if (t != null) {
                        admin.editarTarefa(id);
                    } else System.out.println("Tarefa não encontrada.");
                }
                case 5 -> {
                    System.out.print("-> Email do usuário: ");
                    String email = sc.nextLine();
                    if (usuarioRep.buscarUsuarios(email) != null) {
                        admin.editarUsuario(email);
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                }
                case 0 -> System.out.println("Ação encerrada.");
                default -> System.out.println("Opção inválida.");
            }

        }while(opcao != 0);
    }

}