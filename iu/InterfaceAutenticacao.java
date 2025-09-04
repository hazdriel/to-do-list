package iu;

import fachada.Gerenciador;
import negocio.entidade.Usuario;
import negocio.excecao.sessao.*;
import negocio.excecao.usuario.*;
import java.util.Scanner;

// Interface para autenticação de usuários (login, cadastro, logout)
public final class InterfaceAutenticacao {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    
    public InterfaceAutenticacao(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
    }
    
    public boolean exibirTelaLogin() {
        while (true) { 
            System.out.println("===================================");
            System.out.println("  BEM-VINDO AO LÚMINA - TO-DO-LIST ");
            System.out.println("===================================");
            System.out.println("1 -> Fazer Login");
            System.out.println("2 -> Cadastrar novo usuário");
            System.out.println("3 -> Esqueci minha senha");
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
                case 3:
                    recuperarSenha();
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
        
        System.out.println("--- LOGIN DE ACESSO AO SISTEMA ---");
        System.out.print("📧 Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("🔒 Senha: ");
        String senha = scanner.nextLine();
        
        try {
            if (gerenciador.fazerLogin(email, senha)) {
                System.out.println("\n✅ Login realizado com sucesso! Bem-vindo(a).");
            } else {
                System.out.println("\n❌ Falha no login. Email ou senha incorretos.");
            }
        } catch (LoginJaAtivoException e) {
            System.out.println("\n❌ Erro inesperado no login: " + e.getMessage());
        } catch (EmailVazioException e) {
            System.out.println("\n❌ Erro inesperado no login: " + e.getMessage());
        } catch (SenhaVaziaException e) {
            System.out.println("\n❌ Erro inesperado no login: " + e.getMessage());
        } catch (UsuarioVazioException e) {
            System.out.println("\n❌ Erro inesperado no login: " + e.getMessage());
        }
    }
    

    private void realizarCadastro() {
        UtilitariosInterface.limparTela();
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
        } catch (NomeVazioException | EmailVazioException | SenhaVaziaException | UsuarioExistenteException | SenhaTamanhoInvalidoException |
                 NomeApenasLetrasException | NomeTamanhoInvalidoException | EmailFormatoInvalidoException | UsuarioVazioException e) {
            System.out.println("❌ Erro ao realizar cadastro: " + e.getMessage());
        }
    }

    private void recuperarSenha() {
        System.out.println("--- RECUPERAÇÃO DE SENHA ---");
        
        System.out.print("📧 Digite seu email: ");
        String email = scanner.nextLine().trim();
        
        try {
            gerenciador.solicitarRecuperacaoSenha(email);
            
            System.out.print("🔑 Digite o código de verificação: ");
            String codigoDigitado = scanner.nextLine().trim();
            
            System.out.print("🔒 Digite sua nova senha: ");
            String novaSenha = scanner.nextLine();
            
            System.out.print("🔒 Confirme sua nova senha: ");
            String confirmacaoSenha = scanner.nextLine();
            
            if (!novaSenha.equals(confirmacaoSenha)) {
                System.out.println("❌ As senhas não coincidem. Tente novamente.");
                return;
            }
            
            gerenciador.recuperarSenha(email, codigoDigitado, novaSenha);
            System.out.println("✅ Senha alterada com sucesso!");
            System.out.println(" Agora você pode fazer login com sua nova senha");
            
        } catch (EmailVazioException e) {
            System.out.println("❌ Erro ao recuperar senha: " + e.getMessage());
        } catch (UsuarioNaoEncontradoException e) {
            System.out.println("❌ Erro ao recuperar senha: " + e.getMessage());
        } catch (CodigoInvalidoException e) {
            System.out.println("❌ Erro ao recuperar senha: " + e.getMessage());
        } catch (SenhaVaziaException e) {
            System.out.println("❌ Erro ao recuperar senha: " + e.getMessage());
        } catch (SenhaTamanhoInvalidoException e) {
            System.out.println("❌ Erro ao recuperar senha: " + e.getMessage());
        } catch (UsuarioVazioException e) {
            System.out.println("❌ Erro ao recuperar senha: " + e.getMessage());
        }
    }
    

    public void realizarLogout() {
        System.out.println("--- LOGOUT ---");
        System.out.println("🚪 A sua sessão está sendo encerrada...");
        
        try {
            gerenciador.fazerLogout();
            System.out.println("\n✅ Sessão encerrada com sucesso!");
        } catch (SessaoJaInativaException e) {
            System.out.println("❌ Erro ao realizar logout: " + e.getMessage());
        }
        UtilitariosInterface.pressioneEnterParaContinuar(scanner);
    }
    
    public boolean estaLogado() {
        return gerenciador.estaLogado();
    }
    
    public Usuario getUsuarioLogado() {
        try {
            return gerenciador.getUsuarioLogado();
        } catch (SessaoJaInativaException e) {
            return null;
        }
    }
}