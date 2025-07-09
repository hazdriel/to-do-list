package service;

import model.Tarefa;
import model.Usuario;
import repository.*;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TarefaService {
  private Scanner sc;
  private TarefaRepository repositorioTarefas;
  private UsuarioRespository repositorioUsuarios;
  private Usuario usuarioLogado;

  public TarefaService(Scanner sc, TarefaRepository repositorioTarefas, UsuarioRespository repositorioUsuarios) {
    this.sc = sc;
    this.repositorioUsuarios = repositorioUsuarios;
    this.repositorioTarefas = repositorioTarefas;
  }

  public void criarTarefa() {
    
    if (usuarioLogado == null) {
      System.out.println("Nenhum usuário logado. Faça login antes de criar uma tarefa.");
      return;
    }

    System.out.println("Insira o título da tarefa: ");
    String titulo = sc.nextLine();
    if (titulo.trim().isEmpty()) {
      System.out.println("O título não pode ser vazio.");
      return;
    }

    System.out.println("Agora descreva sua tarefa (max 200 caracteres): ");
    String descricao = sc.nextLine();
    if (descricao.trim().isEmpty()) {
      System.out.println("A descrição não pode ser vazia.");
      return;
    }

    Tarefa novaTarefa = new Tarefa(titulo, descricao, usuarioLogado);

    novaTarefa.setPrazo(definirPrazo());
    novaTarefa.setPrioridade(definirPrioridade());

    repositorioTarefas.inserirTarefas(novaTarefa);
    System.out.println("Tarefa criada com sucesso!");
  }

  private LocalDate definirPrazo() {
    System.out.println("Deseja informar um prazo para esta tarefa? Digite 1 para sim e 2 para não");
    int opcao = sc.nextInt();
    sc.nextLine();

    if (opcao == 1) {
      System.out.println("Digite o prazo da tarefa (ex: 2025-12-31): ");
      String entrada = sc.nextLine();
      try {
        return LocalDate.parse(entrada);
      } catch (DateTimeParseException e) {
        System.out.println("Formato de data inválido. A tarefa será criada sem prazo.");
      }
    }
    return null;
  }

  private String definirPrioridade() {
    System.out.println("Deseja informar uma prioridade para esta tarefa? Digite 1 para sim e 2 para não (padrão: Baixa)");
    int opcao = sc.nextInt();
    sc.nextLine();

    if (opcao == 1) {
      while (true) {
        System.out.println("Escolha a prioridade:\n1 - Alta\n2 - Média\n3 - Baixa");
        String entrada = sc.nextLine();
        switch (entrada) {
          case "1":
            return "Alta";
          case "2":
            return "Média";
          case "3":
            return "Baixa";
          default:
            System.out.println("Opção inválida. Tente novamente.");
        }
      }
    }
    return "Baixa";
  }

  /* 
  public void solicitarUsuario() {
    System.out.println("===================================");
    System.out.println("   LOGIN DO CLIENTE - To-do-List     ");
    System.out.println("===================================");

    System.out.print("Seja bem-vindo ao To-do-List!");
    System.out.print("Informe seu e-mail (usuário teste: teste@todolist.com): ");
    String email = sc.nextLine();
    Usuario usuario = repositorioUsuarios.buscarPorEmail(email);
    if (usuario == null) {
      System.out.println("Cliente não encontrado. Verifique o CPF e tente novamente.");
    } else {
      usuarioLogado = usuario;
      System.out.println("Login realizado com sucesso. Bem-vindo, " + usuarioLogado.getNome() + "!");
    }
  } */
}
