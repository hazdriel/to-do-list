package iu;

import fachada.Gerenciador;
import negocio.entidade.Usuario;
import java.util.Scanner;

public final class InterfaceAutenticacao {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    
    public InterfaceAutenticacao(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
    }
    
    
    public boolean exibirTelaLogin() {
        while (true) { // Usar um loop é mais seguro que recursão para menus.
            UtilitariosInterface.limparTela();
            System.out.println("===================================");
            System.out.println("      BEM-VINDO AO TO-DO LIST      ");
            System.out.println("===================================");
            System.out.println("1 -> Fazer Login");
            System.out.println("2 -> Cadastrar Novo Usuario");
            System.out.println("0 -> Sair do Sistema");
            System.out.print("Escolha uma opção: ");
            
            int opcao = UtilitariosInterface.lerInteiro(scanner);
            
            switch (opcao) {
                case 1:
                    realizarLogin();
                    return true; 
                case 2:
                    realizarCadastro();
                    return true; 
                case 0:
                    return false; 
                default:
                    System.out.println("\n❌ Opção inválida. Por favor, tente novamente.");
                    UtilitariosInterface.pressioneEnterParaContinuar(scanner);

            }
        }
    }
    
    private void realizarLogin() {
        UtilitariosInterface.limparTela();
        System.out.println("--- LOGIN DE ACESSO ---");
        System.out.print("📧 Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("🔒 Senha: ");
        String senha = scanner.nextLine();
        
        if (gerenciador.fazerLogin(email, senha)) {
            System.out.println("\n✅ Login realizado com sucesso! Bem-vindo(a).");
        } else {
            System.out.println("\n❌ Falha no login. Email ou senha incorretos.");
        }
        UtilitariosInterface.pressioneEnterParaContinuar(scanner);
    }
    

    private void realizarCadastro() {
        UtilitariosInterface.limparTela();
        System.out.println("--- CADASTRO DE NOVO USUARIO ---");
        System.out.print("👤 Nome Completo: ");
        String nome = scanner.nextLine().trim();
        System.out.print("📧 Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("🔒 Senha: ");
        String senha = scanner.nextLine();
        
        try {
            gerenciador.cadastrarUsuario(nome, email, senha);
            System.out.println("\n✅ Usuario cadastrado com sucesso! Agora, por favor, faça o login.");
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao cadastrar usuario: " + e.getMessage());
        }
        UtilitariosInterface.pressioneEnterParaContinuar(scanner);
    }
    

    public void realizarLogout() {
        UtilitariosInterface.limparTela();
        System.out.println("--- LOGOUT ---");
        System.out.println("🚪 A sua sessão está sendo encerrada...");
        
        gerenciador.fazerLogout();
        
        System.out.println("\n✅ Sessão encerrada com sucesso!");
        UtilitariosInterface.pressioneEnterParaContinuar(scanner);
    }
    
    public boolean estaLogado() {
        return gerenciador.estaLogado();
    }
    
    public Usuario getUsuarioLogado() {
        return gerenciador.getUsuarioLogado();
    }
}