package scripts;

import fachada.Gerenciador;
import negocio.entidade.*;
import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Script V2 para popular o sistema com tarefas da família Silva.
 * Inclui tarefas delegadas e melhor organização.
 */
public class PopularSistemaV2 {
    
    private static final Gerenciador gerenciador = new Gerenciador();
    
    public static void main(String[] args) {
        System.out.println("🏠 POPULANDO SISTEMA V2 - FAMÍLIA SILVA 🏠");
        System.out.println("=".repeat(50));
        
        try {
            // Criar categorias primeiro
            criarCategorias();
            
            // Popular tarefas para cada membro da família
            popularTarefasJoao();
            popularTarefasMaria();
            popularTarefasRosa();
            popularTarefasAna();
            popularTarefasPedro();
            
            // Criar tarefas delegadas
            criarTarefasDelegadas();
            
            System.out.println("\n✅ Sistema populado com sucesso!");
            System.out.println("🎉 Família Silva está pronta para usar o To-Do List!");
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao popular sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void criarCategorias() {
        System.out.println("\n📂 Criando categorias...");
        
        try {
            // Login como João para criar categorias
            gerenciador.fazerLogin("joao.silva@email.com", "joao123");
            
            gerenciador.criarCategoria("Casa");
            gerenciador.criarCategoria("Trabalho");
            gerenciador.criarCategoria("Estudo");
            gerenciador.criarCategoria("Saúde");
            gerenciador.criarCategoria("Lazer");
            gerenciador.criarCategoria("Família");
            gerenciador.criarCategoria("Finanças");
            
            System.out.println("✅ Categorias criadas com sucesso!");
        } catch (Exception e) {
            System.out.println("⚠️ Categorias já existem ou erro: " + e.getMessage());
        }
    }
    
    private static void popularTarefasJoao() {
        System.out.println("\n👨 Criando tarefas para João Silva...");
        
        try {
            // Fazer logout se houver usuário logado
            if (gerenciador.estaLogado()) {
                gerenciador.fazerLogout();
            }
            
            // Login como João
            gerenciador.fazerLogin("joao.silva@email.com", "joao123");
            
            // Buscar categorias
            Categoria casa = buscarCategoria("Casa");
            Categoria financas = buscarCategoria("Finanças");
            Categoria trabalho = buscarCategoria("Trabalho");
            Categoria saude = buscarCategoria("Saúde");
            
            // Tarefas de João
            gerenciador.criarTarefaSimples(
                "Pagar contas mensais", 
                "Pagar luz, água, internet e telefone", 
                Prioridade.ALTA, 
                LocalDateTime.now().plusDays(5), 
                financas
            );
            
            gerenciador.criarTarefaSimples(
                "Manutenção do carro", 
                "Levar carro para revisão e troca de óleo", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(10), 
                casa
            );
            
            gerenciador.criarTarefaSimples(
                "Cortar a grama", 
                "Cortar grama do jardim e podar plantas", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(3), 
                casa
            );
            
            gerenciador.criarTarefaSimples(
                "Cozinhar jantar de domingo", 
                "Preparar feijoada para a família", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(7), 
                casa
            );
            
            gerenciador.criarTarefaSimples(
                "Fazer backup do computador", 
                "Backup de documentos importantes", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(14), 
                trabalho
            );
            
            gerenciador.criarTarefaSimples(
                "Consulta médica", 
                "Check-up anual no cardiologista", 
                Prioridade.ALTA, 
                LocalDateTime.now().plusDays(20), 
                saude
            );
            
            System.out.println("✅ Tarefas de João criadas!");
            
        } catch (Exception e) {
            System.out.println("❌ Erro nas tarefas de João: " + e.getMessage());
        }
    }
    
    private static void popularTarefasMaria() {
        System.out.println("\n👩 Criando tarefas para Maria Silva...");
        
        try {
            // Fazer logout se houver usuário logado
            if (gerenciador.estaLogado()) {
                gerenciador.fazerLogout();
            }
            
            // Login como Maria
            gerenciador.fazerLogin("maria.silva@email.com", "maria123");
            
            // Buscar categorias
            Categoria trabalho = buscarCategoria("Trabalho");
            Categoria estudo = buscarCategoria("Estudo");
            Categoria saude = buscarCategoria("Saúde");
            Categoria lazer = buscarCategoria("Lazer");
            
            // Tarefas de Maria
            gerenciador.criarTarefaSimples(
                "Finalizar projeto profissional", 
                "Concluir relatório trimestral da empresa", 
                Prioridade.ALTA, 
                LocalDateTime.now().plusDays(3), 
                trabalho
            );
            
            gerenciador.criarTarefaSimples(
                "Estudar para certificação", 
                "Preparar para exame de certificação em gestão", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(15), 
                estudo
            );
            
            gerenciador.criarTarefaSimples(
                "Fazer exercícios físicos", 
                "Academia e caminhada no parque", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(1), 
                saude
            );
            
            gerenciador.criarTarefaSimples(
                "Organizar evento familiar", 
                "Planejar aniversário da Ana", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(25), 
                trabalho
            );
            
            gerenciador.criarTarefaSimples(
                "Cuidar das plantas", 
                "Regar e adubar plantas da casa", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(2), 
                trabalho
            );
            
            gerenciador.criarTarefaSimples(
                "Ler livro novo", 
                "Terminar livro de desenvolvimento pessoal", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(30), 
                lazer
            );
            
            System.out.println("✅ Tarefas de Maria criadas!");
            
        } catch (Exception e) {
            System.out.println("❌ Erro nas tarefas de Maria: " + e.getMessage());
        }
    }
    
    private static void popularTarefasRosa() {
        System.out.println("\n🧓 Criando tarefas para Dona Rosa...");
        
        try {
            // Fazer logout se houver usuário logado
            if (gerenciador.estaLogado()) {
                gerenciador.fazerLogout();
            }
            
            // Login como Rosa
            gerenciador.fazerLogin("rosa.silva@email.com", "rosa123");
            
            // Buscar categorias
            Categoria familia = buscarCategoria("Família");
            Categoria casa = buscarCategoria("Casa");
            Categoria saude = buscarCategoria("Saúde");
            Categoria lazer = buscarCategoria("Lazer");
            
            // Tarefas da Dona Rosa
            gerenciador.criarTarefaSimples(
                "Cuidar dos netos", 
                "Ficar com Ana e Pedro enquanto pais trabalham", 
                Prioridade.ALTA, 
                LocalDateTime.now().plusDays(1), 
                familia
            );
            
            gerenciador.criarTarefaSimples(
                "Fazer bolo de chocolate", 
                "Receita tradicional da família", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(2), 
                casa
            );
            
            gerenciador.criarTarefaSimples(
                "Cuidar do jardim", 
                "Regar flores e podar roseiras", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(1), 
                casa
            );
            
            gerenciador.criarTarefaSimples(
                "Organizar fotos antigas", 
                "Digitalizar fotos da família", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(10), 
                familia
            );
            
            gerenciador.criarTarefaSimples(
                "Contar histórias para netos", 
                "Histórias da infância para Ana e Pedro", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(3), 
                familia
            );
            
            gerenciador.criarTarefaSimples(
                "Consulta médica", 
                "Check-up mensal no geriatra", 
                Prioridade.ALTA, 
                LocalDateTime.now().plusDays(7), 
                saude
            );
            
            gerenciador.criarTarefaSimples(
                "Fazer tricô", 
                "Terminar cachecol para o inverno", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(20), 
                lazer
            );
            
            System.out.println("✅ Tarefas da Dona Rosa criadas!");
            
        } catch (Exception e) {
            System.out.println("❌ Erro nas tarefas da Rosa: " + e.getMessage());
        }
    }
    
    private static void popularTarefasAna() {
        System.out.println("\n👧 Criando tarefas para Ana Silva...");
        
        try {
            // Fazer logout se houver usuário logado
            if (gerenciador.estaLogado()) {
                gerenciador.fazerLogout();
            }
            
            // Login como Ana
            gerenciador.fazerLogin("ana.silva@email.com", "ana123");
            
            // Buscar categorias
            Categoria estudo = buscarCategoria("Estudo");
            Categoria casa = buscarCategoria("Casa");
            Categoria lazer = buscarCategoria("Lazer");
            Categoria familia = buscarCategoria("Família");
            
            // Tarefas da Ana
            gerenciador.criarTarefaSimples(
                "Estudar para prova de matemática", 
                "Revisar álgebra e geometria", 
                Prioridade.ALTA, 
                LocalDateTime.now().plusDays(2), 
                estudo
            );
            
            gerenciador.criarTarefaSimples(
                "Fazer trabalho de história", 
                "Pesquisa sobre Revolução Industrial", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(5), 
                estudo
            );
            
            gerenciador.criarTarefaSimples(
                "Organizar quarto", 
                "Limpar e organizar gavetas e armário", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(1), 
                casa
            );
            
            gerenciador.criarTarefaSimples(
                "Ajudar na cozinha", 
                "Ajudar mãe a preparar jantar", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(1), 
                casa
            );
            
            gerenciador.criarTarefaSimples(
                "Praticar violão", 
                "Estudar nova música para apresentação", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(3), 
                lazer
            );
            
            gerenciador.criarTarefaSimples(
                "Fazer voluntariado", 
                "Ajudar na biblioteca da escola", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(7), 
                familia
            );
            
            // Tarefa temporizada (Pomodoro) para estudos
            gerenciador.criarTarefaTemporizada(
                "Sessão de estudo intensivo", 
                "Estudar física com técnica Pomodoro", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(1), 
                estudo,
                Duration.ofMinutes(25), // 25 min sessão
                Duration.ofMinutes(5),  // 5 min pausa
                4 // 4 sessões
            );
            
            System.out.println("✅ Tarefas da Ana criadas!");
            
        } catch (Exception e) {
            System.out.println("❌ Erro nas tarefas da Ana: " + e.getMessage());
        }
    }
    
    private static void popularTarefasPedro() {
        System.out.println("\n Pedro Silva...");
        
        try {
            // Fazer logout se houver usuário logado
            if (gerenciador.estaLogado()) {
                gerenciador.fazerLogout();
            }
            
            // Login como Pedro
            gerenciador.fazerLogin("pedro.silva@email.com", "pedro123");
            
            // Buscar categorias
            Categoria estudo = buscarCategoria("Estudo");
            Categoria casa = buscarCategoria("Casa");
            Categoria lazer = buscarCategoria("Lazer");
            Categoria familia = buscarCategoria("Família");
            
            // Tarefas do Pedro
            gerenciador.criarTarefaSimples(
                "Fazer lição de casa", 
                "Matemática e português", 
                Prioridade.ALTA, 
                LocalDateTime.now().plusDays(1), 
                estudo
            );
            
            gerenciador.criarTarefaSimples(
                "Organizar brinquedos", 
                "Guardar brinquedos no baú", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(1), 
                casa
            );
            
            gerenciador.criarTarefaSimples(
                "Estudar para teste de ciências", 
                "Revisar sobre animais e plantas", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(3), 
                estudo
            );
            
            gerenciador.criarTarefaSimples(
                "Ajudar a arrumar a mesa", 
                "Colocar pratos e talheres na mesa", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(1), 
                casa
            );
            
            gerenciador.criarTarefaSimples(
                "Praticar futebol", 
                "Treino no clube esportivo", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(2), 
                lazer
            );
            
            gerenciador.criarTarefaSimples(
                "Ler livro infantil", 
                "Terminar livro de aventuras", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(5), 
                lazer
            );
            
            gerenciador.criarTarefaSimples(
                "Ajudar a cuidar do pet", 
                "Alimentar e brincar com o cachorro", 
                Prioridade.MEDIA, 
                LocalDateTime.now().plusDays(1), 
                familia
            );
            
            // Tarefa temporizada (Pomodoro) para estudos
            gerenciador.criarTarefaTemporizada(
                "Sessão de estudo divertida", 
                "Estudar matemática com jogos", 
                Prioridade.BAIXA, 
                LocalDateTime.now().plusDays(2), 
                estudo,
                Duration.ofMinutes(20), // 20 min sessão (mais curta para criança)
                Duration.ofMinutes(10), // 10 min pausa
                3 // 3 sessões
            );
            
            System.out.println("✅ Tarefas do Pedro criadas!");
            
        } catch (Exception e) {
            System.out.println("❌ Erro nas tarefas do Pedro: " + e.getMessage());
        }
    }
    
    private static void criarTarefasDelegadas() {
        System.out.println("\n🤝 Criando tarefas delegadas...");
        
        try {
            // João delega tarefas para Maria
            if (gerenciador.estaLogado()) {
                gerenciador.fazerLogout();
            }
            gerenciador.fazerLogin("joao.silva@email.com", "joao123");
            
            // Buscar tarefas de João para delegar
            var tarefasJoao = gerenciador.listarTarefas();
            if (!tarefasJoao.isEmpty()) {
                // Delegar "Cortar a grama" para Maria
                var tarefaCortarGrama = tarefasJoao.stream()
                    .filter(t -> t.getTitulo().contains("Cortar a grama"))
                    .findFirst()
                    .orElse(null);
                
                if (tarefaCortarGrama != null) {
                    var maria = buscarUsuario("maria.silva@email.com");
                    if (maria != null) {
                        gerenciador.delegarTarefa(tarefaCortarGrama.getId(), maria);
                        System.out.println("✅ João delegou 'Cortar a grama' para Maria");
                    }
                }
            }
            
            // Maria delega tarefas para Ana
            gerenciador.fazerLogout();
            gerenciador.fazerLogin("maria.silva@email.com", "maria123");
            
            var tarefasMaria = gerenciador.listarTarefas();
            if (!tarefasMaria.isEmpty()) {
                // Delegar "Cuidar das plantas" para Ana
                var tarefaPlantas = tarefasMaria.stream()
                    .filter(t -> t.getTitulo().contains("Cuidar das plantas"))
                    .findFirst()
                    .orElse(null);
                
                if (tarefaPlantas != null) {
                    var ana = buscarUsuario("ana.silva@email.com");
                    if (ana != null) {
                        gerenciador.delegarTarefa(tarefaPlantas.getId(), ana);
                        System.out.println("✅ Maria delegou 'Cuidar das plantas' para Ana");
                    }
                }
            }
            
            // Dona Rosa delega tarefas para Pedro
            gerenciador.fazerLogout();
            gerenciador.fazerLogin("rosa.silva@email.com", "rosa123");
            
            var tarefasRosa = gerenciador.listarTarefas();
            if (!tarefasRosa.isEmpty()) {
                // Delegar "Cuidar do jardim" para Pedro
                var tarefaJardim = tarefasRosa.stream()
                    .filter(t -> t.getTitulo().contains("Cuidar do jardim"))
                    .findFirst()
                    .orElse(null);
                
                if (tarefaJardim != null) {
                    var pedro = buscarUsuario("pedro.silva@email.com");
                    if (pedro != null) {
                        gerenciador.delegarTarefa(tarefaJardim.getId(), pedro);
                        System.out.println("✅ Dona Rosa delegou 'Cuidar do jardim' para Pedro");
                    }
                }
            }
            
            System.out.println("✅ Tarefas delegadas criadas!");
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao criar tarefas delegadas: " + e.getMessage());
        }
    }
    
    private static Categoria buscarCategoria(String nome) {
        try {
            var categorias = gerenciador.listarCategorias();
            return categorias.stream()
                .filter(c -> c.getNome().equals(nome))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao buscar categoria " + nome + ": " + e.getMessage());
            return null;
        }
    }
    
    private static Usuario buscarUsuario(String email) {
        try {
            // Como não há método direto para buscar usuário por email,
            // vou tentar fazer login temporário para obter o usuário
            var usuarioAtual = gerenciador.getUsuarioLogado();
            if (usuarioAtual != null && usuarioAtual.getEmail().equals(email)) {
                return usuarioAtual;
            }
            
            // Se não for o usuário atual, vou tentar fazer login
            gerenciador.fazerLogin(email, "senha123"); // Assumindo senha padrão
            var usuario = gerenciador.getUsuarioLogado();
            gerenciador.fazerLogout(); // Fazer logout para não afetar o fluxo
            
            return usuario;
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao buscar usuário " + email + ": " + e.getMessage());
            return null;
        }
    }
}
