package iu;

import fachada.Gerenciador;
import negocio.entidade.Usuario;
import java.util.Scanner;

/**
 * Módulo da interface de usuário responsável por todas as operações de autenticação,
 * como login, cadastro e logout.
 */
public final class InterfaceAutenticacao {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    
    public InterfaceAutenticacao(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
    }
    
    /**
     * Exibe o menu de login e cadastro. Utiliza um loop 'while' para robustez,
     * evitando recursão desnecessária.
     *
     * @return {@code true} se a aplicação deve continuar, {@code false} se o usuário escolheu sair.
     */
    public boolean exibirTelaLogin() {
        while (true) { // Usar um loop é mais seguro que recursão para menus.
            
            System.out.println("===================================");
            System.out.println("  BEM-VINDO AO LÚMINA - TO-DO-LIST ");
            System.out.println("===================================");
            System.out.println("1 -> Fazer Login");
            System.out.println("2 -> Cadastrar novo usuário");
            System.out.println("0 -> Sair do Sistema");
            System.out.print("Escolha uma opção: ");
            
            int opcao = UtilitariosInterface.lerInteiro(scanner);
            scanner.nextLine(); // Consumir a quebra de linha restante
            
            switch (opcao) {
                case 1:
                    realizarLogin();
                    return true; // Retorna para o loop principal, que irá detetar o login.
                case 2:
                    realizarCadastro();
                    return true; // Retorna para o loop principal, mostrando a tela de login novamente.
                case 0:
                    return false; // Sinaliza para o loop principal que o sistema deve encerrar.
                default:
                    System.out.println("\n❌ Opção inválida. Por favor, tente novamente.");
                    UtilitariosInterface.pressioneEnterParaContinuar(scanner);
                    // O loop continuará, exibindo o menu novamente.
            }
        }
    }
    
    /**
     * Conduz o processo de login do utilizador.
     */
    private void realizarLogin() {
        
        System.out.println("--- LOGIN DE ACESSO AO SISTEMA ---");
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
    
    /**
     * Conduz o processo de cadastro de um novo utilizador.
     */
    private void realizarCadastro() {
        
        System.out.println("--- CADASTRO DE NOVO USUÁRIO ---");
        System.out.print("👤 Nome Completo: ");
        String nome = scanner.nextLine().trim();
        System.out.print("📧 Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("🔒 Senha: ");
        String senha = scanner.nextLine();
        
        try {
            gerenciador.cadastrarUsuario(nome, email, senha);
            System.out.println("\n✅ Usuário cadastrado com sucesso! Agora, por favor, faça o login.");
        } catch (Exception e) {
            // Exibe uma mensagem mais amigável para o utilizador.
            System.out.println("\n❌ Erro ao cadastrar usuário: " + e.getMessage());
        }
        UtilitariosInterface.pressioneEnterParaContinuar(scanner);
    }
    
    /**
     * Realiza o processo de logout do utilizador atual.
     */
    public void realizarLogout() {
        
        System.out.println("--- LOGOUT ---");
        System.out.println("🚪 A sua sessão está sendo encerrada...");
        
        gerenciador.fazerLogout();
        
        System.out.println("\n✅ Sessão encerrada com sucesso!");
        UtilitariosInterface.pressioneEnterParaContinuar(scanner);
    }
    
    // Os métodos abaixo são "convenience methods", que apenas delegam a chamada
    // para a fachada. Podem ser úteis para simplificar o acesso em outras partes da UI.

    /**
     * Verifica se há um utilizador autenticado no sistema.
     * @return {@code true} se um utilizador estiver logado.
     */
    public boolean estaLogado() {
        return gerenciador.estaLogado();
    }
    
    /**
     * Obtém a instância do utilizador atualmente autenticado.
     * @return O objeto {@code Usuario} logado, ou {@code null} se ninguém estiver logado.
     */
    public Usuario getUsuarioLogado() {
        return gerenciador.getUsuarioLogado();
    }
}