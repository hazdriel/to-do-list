package negocio.relatorio;

import negocio.DadosEstatisticos;
import negocio.entidade.TarefaAbstrata;
import negocio.entidade.Usuario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.File;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.io.font.constants.StandardFonts;

public class ExportadorPDF {
    
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMATO_ARQUIVO = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    public String exportarRelatorioProdutividade(DadosEstatisticos dados) throws Exception {
        String nomeArquivo = gerarNomeArquivo("produtividade", dados.getUsuario());
        String caminhoCompleto = "relatorios/" + nomeArquivo;
        
        criarDiretorioSeNaoExistir();
        
        PdfWriter writer = new PdfWriter(caminhoCompleto);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        try {
            PdfFont fonteTitulo = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fonteNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            
            adicionarCabecalho(document, "RELATÓRIO DE PRODUTIVIDADE", fonteTitulo);
            adicionarInformacoesPeriodo(document, dados, fonteNormal);
            adicionarDadosProdutividade(document, dados, fonteNormal, fonteTitulo);
            adicionarRodape(document, fonteNormal);
            
        } finally {
            document.close();
        }
        
        return caminhoCompleto;
    }
    
    public String exportarRelatorioTarefas(List<TarefaAbstrata> tarefas, Usuario usuario, String titulo) throws Exception {
        String nomeArquivo = gerarNomeArquivo("tarefas", usuario);
        String caminhoCompleto = "relatorios/" + nomeArquivo;
        
        criarDiretorioSeNaoExistir();
        
        PdfWriter writer = new PdfWriter(caminhoCompleto);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        try {
            PdfFont fonteTitulo = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fonteNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            
            adicionarCabecalho(document, titulo.toUpperCase(), fonteTitulo);
            adicionarInformacoesUsuario(document, usuario, fonteNormal);
            adicionarListaTarefas(document, tarefas, fonteNormal, fonteTitulo);
            adicionarRodape(document, fonteNormal);
            
        } finally {
            document.close();
        }
        
        return caminhoCompleto;
    }
    
    public String exportarRelatorioCompleto(DadosEstatisticos dados, List<TarefaAbstrata> todasTarefas) throws Exception {
        String nomeArquivo = gerarNomeArquivo("completo", dados.getUsuario());
        String caminhoCompleto = "relatorios/" + nomeArquivo;
        
        criarDiretorioSeNaoExistir();
        
        PdfWriter writer = new PdfWriter(caminhoCompleto);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        try {
            PdfFont fonteTitulo = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fonteNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            
            adicionarCabecalho(document, "RELATÓRIO COMPLETO", fonteTitulo);
            adicionarInformacoesPeriodo(document, dados, fonteNormal);
            
            document.add(new Paragraph("\nESTATÍSTICAS DE PRODUTIVIDADE").setFont(fonteTitulo).setFontSize(14));
            adicionarDadosProdutividade(document, dados, fonteNormal, fonteTitulo);
            
            if (dados.getTarefasAtencao().temTarefasAtencao()) {
                document.add(new Paragraph("\nTAREFAS QUE NECESSITAM ATENÇÃO").setFont(fonteTitulo).setFontSize(14));
                adicionarTarefasAtencao(document, dados.getTarefasAtencao(), fonteNormal, fonteTitulo);
            }
            
            document.add(new Paragraph("\nLISTA COMPLETA DE TAREFAS").setFont(fonteTitulo).setFontSize(14));
            adicionarListaTarefas(document, todasTarefas, fonteNormal, fonteTitulo);
            
            adicionarRodape(document, fonteNormal);
            
        } finally {
            document.close();
        }
        
        return caminhoCompleto;
    }
    
    private void criarDiretorioSeNaoExistir() {
        File diretorio = new File("relatorios");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }
    
    private String gerarNomeArquivo(String tipo, Usuario usuario) {
        String timestamp = LocalDateTime.now().format(FORMATO_ARQUIVO);
        String nomeUsuario = usuario.getNome().replaceAll("[^a-zA-Z0-9]", "_");
        return String.format("relatorio_%s_%s_%s.pdf", tipo, nomeUsuario, timestamp);
    }
    
    private void adicionarCabecalho(Document document, String titulo, PdfFont fonteTitulo) {
        Paragraph sistema = new Paragraph("SISTEMA TO-DO LIST")
            .setFont(fonteTitulo)
            .setFontSize(20)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(5);
        
        Paragraph tituloRelatorio = new Paragraph(titulo)
            .setFont(fonteTitulo)
            .setFontSize(16)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20);
        
        document.add(sistema);
        document.add(tituloRelatorio);
    }
    
    private void adicionarInformacoesPeriodo(Document document, DadosEstatisticos dados, PdfFont fonteNormal) {
        document.add(new Paragraph("USUÁRIO: " + dados.getUsuario().getNome()).setFont(fonteNormal));
        document.add(new Paragraph("EMAIL: " + dados.getUsuario().getEmail()).setFont(fonteNormal));
        document.add(new Paragraph("PERÍODO: " + formatarData(dados.getDataInicio()) + " até " + formatarData(dados.getDataFim())).setFont(fonteNormal));
        document.add(new Paragraph("GERADO EM: " + LocalDateTime.now().format(FORMATO_DATA_HORA)).setFont(fonteNormal).setMarginBottom(20));
    }
    
    private void adicionarInformacoesUsuario(Document document, Usuario usuario, PdfFont fonteNormal) {
        document.add(new Paragraph("USUÁRIO: " + usuario.getNome()).setFont(fonteNormal));
        document.add(new Paragraph("EMAIL: " + usuario.getEmail()).setFont(fonteNormal));
        document.add(new Paragraph("GERADO EM: " + LocalDateTime.now().format(FORMATO_DATA_HORA)).setFont(fonteNormal).setMarginBottom(20));
    }
    
    private void adicionarDadosProdutividade(Document document, DadosEstatisticos dados, PdfFont fonteNormal, PdfFont fonteTitulo) {
        document.add(new Paragraph("RESUMO GERAL:").setFont(fonteTitulo).setFontSize(12).setMarginBottom(10));
        
        document.add(new Paragraph("• Total de Tarefas Criadas: " + dados.getTotalTarefas()).setFont(fonteNormal));
        document.add(new Paragraph("• Tarefas Concluídas: " + dados.getTarefasConcluidas()).setFont(fonteNormal));
        document.add(new Paragraph("• Taxa de Conclusão: " + String.format("%.1f%%", dados.getTaxaConclusao())).setFont(fonteNormal));
        document.add(new Paragraph("• Tarefas Canceladas: " + dados.getTarefasCanceladas()).setFont(fonteNormal));
        document.add(new Paragraph("• Tarefas Pendentes: " + dados.getTarefasPendentes()).setFont(fonteNormal));
        document.add(new Paragraph("• Tarefas em Progresso: " + dados.getTarefasEmProgresso()).setFont(fonteNormal).setMarginBottom(15));
        
        document.add(new Paragraph("DISTRIBUIÇÃO POR CATEGORIA:").setFont(fonteTitulo).setFontSize(12).setMarginBottom(10));
        dados.getDistribuicaoPorCategoria().forEach((categoria, quantidade) -> {
            double percentual = (quantidade.doubleValue() / dados.getTotalTarefas()) * 100;
            document.add(new Paragraph("• " + categoria.getNome() + ": " + quantidade + " (" + String.format("%.1f%%", percentual) + ")").setFont(fonteNormal));
        });
        
        document.add(new Paragraph("DISTRIBUIÇÃO POR PRIORIDADE:").setFont(fonteTitulo).setFontSize(12).setMarginTop(15).setMarginBottom(10));
        dados.getDistribuicaoPorPrioridade().forEach((prioridade, quantidade) -> {
            double percentual = (quantidade.doubleValue() / dados.getTotalTarefas()) * 100;
            document.add(new Paragraph("• " + prioridade + ": " + quantidade + " (" + String.format("%.1f%%", percentual) + ")").setFont(fonteNormal));
        });
    }
    
    private void adicionarTarefasAtencao(Document document, DadosEstatisticos.TarefasAtencao tarefasAtencao, PdfFont fonteNormal, PdfFont fonteTitulo) {
        if (!tarefasAtencao.getAtrasadas().isEmpty()) {
            document.add(new Paragraph("TAREFAS ATRASADAS (" + tarefasAtencao.getAtrasadas().size() + "):").setFont(fonteTitulo).setFontSize(11).setMarginTop(10));
            adicionarListaTarefasSimples(document, tarefasAtencao.getAtrasadas(), fonteNormal);
        }
        
        if (!tarefasAtencao.getVencemHoje().isEmpty()) {
            document.add(new Paragraph("VENCEM HOJE (" + tarefasAtencao.getVencemHoje().size() + "):").setFont(fonteTitulo).setFontSize(11).setMarginTop(10));
            adicionarListaTarefasSimples(document, tarefasAtencao.getVencemHoje(), fonteNormal);
        }
        
        if (!tarefasAtencao.getVencemAmanha().isEmpty()) {
            document.add(new Paragraph("VENCEM AMANHÃ (" + tarefasAtencao.getVencemAmanha().size() + "):").setFont(fonteTitulo).setFontSize(11).setMarginTop(10));
            adicionarListaTarefasSimples(document, tarefasAtencao.getVencemAmanha(), fonteNormal);
        }
        
        if (!tarefasAtencao.getEmProgresso().isEmpty()) {
            document.add(new Paragraph("EM PROGRESSO (" + tarefasAtencao.getEmProgresso().size() + "):").setFont(fonteTitulo).setFontSize(11).setMarginTop(10));
            adicionarListaTarefasSimples(document, tarefasAtencao.getEmProgresso(), fonteNormal);
        }
        
        if (!tarefasAtencao.getPendentes().isEmpty()) {
            document.add(new Paragraph("PENDENTES (" + tarefasAtencao.getPendentes().size() + "):").setFont(fonteTitulo).setFontSize(11).setMarginTop(10));
            adicionarListaTarefasSimples(document, tarefasAtencao.getPendentes(), fonteNormal);
        }
    }
    
    private void adicionarListaTarefas(Document document, List<TarefaAbstrata> tarefas, PdfFont fonteNormal, PdfFont fonteTitulo) {
        if (tarefas.isEmpty()) {
            document.add(new Paragraph("Nenhuma tarefa encontrada.").setFont(fonteNormal));
            return;
        }
        
        document.add(new Paragraph("TOTAL: " + tarefas.size() + " tarefa(s)").setFont(fonteTitulo).setFontSize(11).setMarginBottom(15));
        
        for (int i = 0; i < tarefas.size(); i++) {
            TarefaAbstrata tarefa = tarefas.get(i);
            
            Paragraph titulo = new Paragraph((i + 1) + ". (" + tarefa.getId() + ") " + tarefa.getTitulo())
                .setFont(fonteTitulo)
                .setFontSize(11)
                .setMarginBottom(5);
            document.add(titulo);
            
            document.add(new Paragraph("   Descrição: " + tarefa.getDescricao()).setFont(fonteNormal).setFontSize(9));
            document.add(new Paragraph("   Prioridade: " + tarefa.getPrioridade()).setFont(fonteNormal).setFontSize(9));
            document.add(new Paragraph("   Status: " + tarefa.getStatus()).setFont(fonteNormal).setFontSize(9));
            document.add(new Paragraph("   Prazo: " + formatarData(tarefa.getPrazo())).setFont(fonteNormal).setFontSize(9));
            document.add(new Paragraph("   Categoria: " + (tarefa.getCategoria() != null ? tarefa.getCategoria().getNome() : "Sem categoria")).setFont(fonteNormal).setFontSize(9));
            document.add(new Paragraph("   Tipo: " + tarefa.getTipo()).setFont(fonteNormal).setFontSize(9));
            document.add(new Paragraph("   Criador: " + tarefa.getCriador().getNome()).setFont(fonteNormal).setFontSize(9).setMarginBottom(10));
        }
    }
    
    private void adicionarListaTarefasSimples(Document document, List<TarefaAbstrata> tarefas, PdfFont fonteNormal) {
        for (TarefaAbstrata tarefa : tarefas) {
            String linha = "  • (" + tarefa.getId() + ") " + tarefa.getTitulo() + " - " + formatarData(tarefa.getPrazo()) + " [" + tarefa.getPrioridade() + "]";
            document.add(new Paragraph(linha).setFont(fonteNormal).setFontSize(9));
        }
    }
    
    private void adicionarRodape(Document document, PdfFont fonteNormal) {
        document.add(new Paragraph("\nRelatório gerado pelo Sistema To-Do List").setFont(fonteNormal).setFontSize(9).setTextAlignment(TextAlignment.CENTER).setMarginTop(20));
        document.add(new Paragraph(LocalDateTime.now().format(FORMATO_DATA_HORA)).setFont(fonteNormal).setFontSize(9).setTextAlignment(TextAlignment.CENTER));
    }
    
    private String formatarData(LocalDateTime data) {
        return data != null ? data.format(FORMATO_DATA_HORA) : "N/A";
    }
}

