package service;

import model.Tarefa;
import model.Usuario;
import repository.TarefaRepository;
import repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.Scanner;

public class AdminTarefas {
    private Scanner sc = new Scanner(System.in);

    private TarefaRepository tarefaRep;
    private UsuarioRepository usuarioRep;

    public AdminTarefas(TarefaRepository tarefaRep, UsuarioRepository usuarioRep) {
        this.tarefaRep = tarefaRep;
        this.usuarioRep = usuarioRep;
    }

    public void criarTarefa() {
        System.out.println("--- Criar Usuario ---");
        System.out.print("-> Nome: ");
        String nome = sc.nextLine();
        System.out.print("-> Email: ");
        String email = sc.nextLine();
        System.out.println("---------------------");

        Usuario usuario = new Usuario(nome, email);
        usuarioRep.inserirUsuarios(usuario);

        System.out.println("--- Criar Tarefa ---");
        System.out.print("-> Título: ");
        String titulo = sc.nextLine();
        System.out.print("-> Descrição: ");
        String descricao = sc.nextLine();
        System.out.println("--------------------");

        Tarefa tarefa = new Tarefa(titulo, descricao, usuario);
        tarefaRep.inserirTarefas(tarefa);

        System.out.println("Tarefa criada com sucesso!");
        System.out.println("--------------------");
        tarefa.exibirTarefa();
    }

    public void editarTarefa(String id) {
        Tarefa tarefa = tarefaRep.buscarTarefaPorID(id);
        if (tarefa != null) {
            System.out.println("--- Atualizar Tarefa ---");
            System.out.println("1-> Título");
            System.out.println("2-> Descrição");
            System.out.println("3-> Prioridade");
            System.out.println("4-> Situação");
            System.out.println("5-> Prazo");
            System.out.println("0-> Voltar");
            System.out.println("----------------------");
            System.out.print("-> Opção: ");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> {
                    System.out.print("-> Novo Título: ");
                    String titulo = sc.nextLine();
                    tarefa.setTitulo(titulo);
                }
                case 2 -> {
                    System.out.print("-> Nova Descrição: ");
                    String descricao = sc.nextLine();
                    tarefa.setDescricao(descricao);
                }
                case 3 -> {
                    System.out.print("-> Nova Prioridade: ");
                    String prioridade = sc.nextLine();
                    tarefa.setPrioridade(prioridade);
                }
                case 4 -> {
                    System.out.print("-> Marcar como concluída? (s/n): ");
                    String resp = sc.nextLine();
                    if (resp.equalsIgnoreCase("s")) {
                        tarefa.isConcluida();
                    }
                }
                case 5 -> {
                    System.out.print("-> Novo Prazo (AAAA-MM-DD): ");
                    String prazoStr = sc.nextLine();
                    if(prazoStr.matches("\\d{4}-\\d{2}-\\d{2}")){
                        LocalDate prazo = LocalDate.parse(prazoStr);
                        tarefa.setPrazo(prazo);
                    } else {
                        System.out.println("Formato inválido. Use o padrão AAAA-MM-DD.");
                    }
                }
                case 0 -> System.out.println("Ação encerrada.");
                default -> System.out.println("Opção inválida.");
            }
            tarefaRep.atualizarTarefa(tarefa);
            System.out.println("Tarefa atualizada com sucesso.");
        } else {
            System.out.println("Tarefa não encontrada.");
        }
    }

    public void editarUsuario(String email) {
        Usuario usuario = usuarioRep.buscarUsuarios(email);
        if (usuario != null) {
            System.out.println("--- Atualizar Usuário ---");
            System.out.println("1-> Nome");
            System.out.println("2-> Email");
            System.out.println("3-> Remover");
            System.out.println("0-> Voltar");
            System.out.println("----------------------");
            System.out.print("-> Opção: ");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> {
                    System.out.print("-> Novo Nome: ");
                    String nome = sc.nextLine();
                    usuario.setNome(nome);
                }
                case 2 -> {
                    System.out.print("-> Novo Email: ");
                    String novoEmail = sc.nextLine();
                    usuario.setEmail(novoEmail);
                }
                case 3 -> {
                    System.out.print("-> Email a remover: ");
                    String emailRemocao = sc.nextLine();
                    usuarioRep.removerUsuarios(emailRemocao);
                }
                case 0 -> System.out.println("Ação encerrada.");
                default -> System.out.println("Opção inválida.");
            }
            usuarioRep.atualizarUsuarios(usuario);
            System.out.println("Usuário atualizado com sucesso.");
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

}