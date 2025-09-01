package negocio.entidade;

// Classe utilitária para gerar IDs sequenciais únicos.
// Garante que cada ID seja único dentro do sistema.
public class GeradorId {
    private static int contadorTarefas = 0;
    private static int contadorUsuarios = 0;
    
    public static String gerarIdTarefa() {
        return "T" + String.format("%04d", ++contadorTarefas);
    }
    
    public static String gerarIdUsuario() {
        return "U" + String.format("%04d", ++contadorUsuarios);
    }
    
    // Sincroniza contadores com dados existentes
    public static void sincronizarContadorTarefas(int maxId) {
        contadorTarefas = Math.max(contadorTarefas, maxId);
    }
    
    public static void sincronizarContadorUsuarios(int maxId) {
        contadorUsuarios = Math.max(contadorUsuarios, maxId);
    }
    
    public static void resetarContadores() {
        contadorTarefas = 0;
        contadorUsuarios = 0;
    }
    
    // Extrai número do ID (ex: "T0001" -> 1)
    public static int extrairNumeroId(String id) {
        if (id == null || id.length() < 2) return 0;
        try {
            return Integer.parseInt(id.substring(1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
