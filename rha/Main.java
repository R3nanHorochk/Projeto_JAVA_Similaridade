package rha;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.FileFilter;

public class Main {
    private static int func_hash(int chave) {
        double A = 0.6180339887;
        return (int)(20 * (chave * A % 1));
    }

    private static int func_hash2(String a, int stringSize, int tableSize) {
    int hashVal = (int) a.charAt(0);

    for (int j = 1; j < stringSize; j++) {
        hashVal += (int) a.charAt(j);
    }

    return hashVal % tableSize;
    }
    
    public static int charactere(String palavra) {
        int indice = 0;
        int valor = 0;
        for(int i = 0 ; i < palavra.length() ; i++) {
            valor = func_hash(palavra.charAt(i));
            indice += valor;
        }
        return indice;
    }
    
    public static void main(String[] args) {
        // 1. Validação básica de argumentos
        if (args.length < 3) {
            System.out.println("Uso: java Main <diretorio> <limiar> <modo> [args_extras...]");
            System.out.println("Modos disponíveis:");
            System.out.println("  lista  -> Exibe todos os pares acima do limiar.");
            System.out.println("  topK   -> Exibe os K pares mais semelhantes (ex: topK 5).");
            System.out.println("  busca  -> Busca a similaridade entre dois arquivos específicos.");
            return;
        }

        String diretorioDocumentos = args[0];
        double limiar = Double.parseDouble(args[1]);
        String modo = args[2];
        
        // --- 2. CONFIGURAÇÃO E LEITURA DE ARGUMENTOS EXTRAS ---
        int k = 0;
        String arquivoBusca1 = "";
        String arquivoBusca2 = "";

        if (modo.equals("topK")) {
            if (args.length > 3) {
                k = Integer.parseInt(args[3]);
            } else {
                System.out.println("Erro: Informe o valor de K para o modo topK.");
                return;
            }
        } else if (modo.equals("busca")) {
            if (args.length > 4) {
                arquivoBusca1 = args[3];
                arquivoBusca2 = args[4];
            } else {
                System.out.println("Erro: O modo busca requer dois nomes de arquivos.");
                return;
            }
        } else if (!modo.equals("lista")) {
            System.out.println("Erro: Modo '" + modo + "' desconhecido. Use: lista, topK ou busca.");
            return;
        }
        
        File dir = new File(diretorioDocumentos);
        FileFilter filtro = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith("txt");
            }
        };
        File[] arquivos = dir.listFiles(filtro);

        if (arquivos == null || arquivos.length == 0) {
            System.out.println("Nenhum arquivo .txt encontrado no diretório " + diretorioDocumentos);
            return;
        }
        
        // Processa cada documento
        List<Documento> documentos = new ArrayList<>();
        List<Hash> hashes = new ArrayList<>();
        List<Hash> hashes2 = new ArrayList<>();
        List<String> nomesArquivos = new ArrayList<>();
        int[] comparaDiv = new int[arquivos.length];
        int[] comparaMult = new int[arquivos.length];
        String[] compArq = new String[arquivos.length];
        int icount = 0;
        int colisaoMult = 0;
        int colisaoDiv = 0;
        for (File arquivo : arquivos) {
            String caminho = arquivo.getAbsolutePath();
            Documento doc = new Documento(caminho,true);
            Documento doc2 = new Documento(caminho,false);
            Hash hash = doc.wordFrequency(caminho,true);
             
            Hash hash2 = doc2.wordFrequency(caminho,false);
            comparaMult[icount] = hash.getTotalColisoes();
            comparaDiv[icount] = hash2.getTotalColisoes();
            compArq[icount] = caminho;
            int teste = doc.Col(caminho,true);
            int teste2 = doc.Col(caminho,false);
            colisaoMult = colisaoMult + hash.getTotalColisoes() ; 
            colisaoDiv = colisaoDiv + hash2.getTotalColisoes() ;
            documentos.add(doc);
            hashes.add(hash);
            hash.setMult(false);
            hashes2.add(hash2);
            nomesArquivos.add(arquivo.getName());
            icount++;
        }
        
        // Calcula similaridade entre todos os pares
        List<Resultado> resultados = new ArrayList<>();
        int totalPares = 0;
        
        for (int i = 0; i < hashes.size(); i++) {
            for (int j = i + 1; j < hashes.size(); j++) {
                double similaridade = CompareTo.cosineSimilarity(
                    hashes.get(i).getAllWordData(), 
                    hashes.get(j).getAllWordData()
                );
                Resultado resultado = new Resultado(
                    nomesArquivos.get(i),
                    nomesArquivos.get(j),
                    similaridade
                );
                resultados.add(resultado);
                totalPares++;
            }
        }
        
        // Ordena por similaridade (maior para menor)
        Collections.sort(resultados, new Comparator<Resultado>() {
            @Override
            public int compare(Resultado r1, Resultado r2) {
                return Double.compare(r2.getSimilaridade(), r1.getSimilaridade());
            }
        });
        
        // --- 3. GERAÇÃO DA SAÍDA (CONSOLE E TXT) ---
        
        StringBuilder saida = new StringBuilder();
        saida.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===\n");
        saida.append("\n");
        saida.append("Total de documentos processados: ").append(documentos.size()).append("\n");
        saida.append("\n");
        saida.append("Total de pares comparados: ").append(totalPares).append("\n");
        saida.append("\n");
        saida.append("Funcao hash utilizada: hashMultiplicativo\n");
        saida.append("\n");
        saida.append("Métrica de similaridade: Cosseno\n");
        saida.append("\n");

        // Lista auxiliar para guardar o que será exportado no CSV
        List<Resultado> resultadosFiltrados = new ArrayList<>();
        AVLR alvRESULT = new AVLR();
        if (modo.equals("topK")) {
            saida.append("Modo: Top ").append(k).append(" pares mais semelhantes:\n");
            saida.append("\n");
            saida.append("---------------------------------\n");
            
            int limite = Math.min(k, resultados.size());
            if (limite == 0) saida.append("Nenhum par processado.\n");

            for (int i = 0; i < limite; i++) {
                Resultado r = resultados.get(i);
                resultadosFiltrados.add(r); // Adiciona para o CSV
                alvRESULT.insertAVL(r);
                saida.append(r.getNomeFile1()).append(" <-> ").append(r.getNomeFile2())
                    .append(" = ").append(String.format("%.2f", r.getSimilaridade())).append("\n");
                saida.append("\n");
            }
            System.out.println("\n=== PRINT AVL ===\n");
            alvRESULT.printTop(limite);
            alvRESULT.printTreeR();
            System.out.println("\nTotal de colisões no metodo da Multiplicação: " + colisaoMult + "\n");
            for(int j = 0; j < arquivos.length;j++){
                System.out.println("\n Colisoes no arquivo:" + compArq[j]);
                System.out.println(" Colisoes :" + comparaMult[j] + " \n ");
            }
            System.out.println("Total de colisões no metodo da Divisão: " + colisaoDiv + "\n");
            for(int j = 0; j < arquivos.length;j++){
                System.out.println("\n Colisoes no arquivo:" + compArq[j]);
                System.out.println(" Colisoes :" + comparaDiv[j] + " \n ");
            }
        } else if (modo.equals("busca")) {
            saida.append("Modo: Busca Específica (").append(arquivoBusca1)
                 .append(" e ").append(arquivoBusca2).append(")\n");
            saida.append("\n");
            saida.append("---------------------------------\n");
            
            boolean encontrado = false;
            for (Resultado r : resultados) {
                boolean matchDireto = r.getNomeFile1().equals(arquivoBusca1) && r.getNomeFile2().equals(arquivoBusca2);
                boolean matchInverso = r.getNomeFile1().equals(arquivoBusca2) && r.getNomeFile2().equals(arquivoBusca1);
                
                if (matchDireto || matchInverso) {
                    resultadosFiltrados.add(r); // Adiciona para o CSV
                    alvRESULT.insertAVL(r);
                    saida.append(r.getNomeFile1()).append(" <-> ").append(r.getNomeFile2())
                        .append(" = ").append(String.format("%.2f", r.getSimilaridade())).append("\n");
                    saida.append("\n");
                    encontrado = true;
                    break;
                }
            }
            System.out.println("\n=== PRINT AVL ===\n");
            alvRESULT.printTreeR();
            System.out.println("\nTotal de colisões no metodo da Multiplicação: " + colisaoMult + "\n");
            for(int j = 0; j < arquivos.length;j++){
                System.out.println("\n Colisoes no arquivo:" + compArq[j]);
                System.out.println("Colisoes :" + comparaMult[j] + " \n ");
            }
            System.out.println("Total de colisões no metodo da Divisão: " + colisaoDiv + "\n");
            for(int j = 0; j < arquivos.length;j++){
                System.out.println("\n Colisoes no arquivo:" + compArq[j]);
                System.out.println("Colisoes :" + comparaDiv[j] + " \n " );
            }
            if (!encontrado) {
                saida.append("Par não encontrado.\n\n");
            }

        } else {
            // --- MODO LISTA (Padrão) ---
            saida.append("Modo: Lista (Similaridade >= ").append(String.format("%.2f", limiar)).append("):\n");
            saida.append("\n");
            saida.append("---------------------------------\n");
            
            boolean encontrouParesAcimaLimiar = false;
            for (Resultado r : resultados) {
                if (r.getSimilaridade() >= limiar) {
                    resultadosFiltrados.add(r); // Adiciona para o CSV
                    alvRESULT.insertAVL(r);
                    saida.append(r.getNomeFile1()).append(" <-> ").append(r.getNomeFile2())
                        .append(" = ").append(String.format("%.2f", r.getSimilaridade())).append("\n");
                    saida.append("\n");
                    encontrouParesAcimaLimiar = true;
                }
            }
            System.out.println("\n=== PRINT AVL ===\n");
             alvRESULT.printTreeR();
             System.out.println("\nTotal de colisões no metodo da Multiplicação: " + colisaoMult + "\n");
            for(int j = 0; j < arquivos.length;j++){
                System.out.println("\n Colisoes no arquivo:" + compArq[j]);
                System.out.println(" Colisoes :" + comparaMult[j] + " \n ");
            }
            System.out.println("Total de colisões no metodo da Divisão: " + colisaoDiv + "\n");
            for(int j = 0; j < arquivos.length;j++){
                System.out.println("\n Colisoes no arquivo:" + compArq[j]);
                System.out.println(" Colisoes :" + comparaDiv[j] + " \n ");
            }
            if (!encontrouParesAcimaLimiar) {
                saida.append("Nenhum par acima do limiar.\n\n");
            }
            
        }
        
        saida.append("Pares com menor similaridade:\n");
        saida.append("\n");
        saida.append("---------------------------------\n");
        
        if (!resultados.isEmpty()) {
            Resultado menor = resultados.get(resultados.size() - 1);
            saida.append(menor.getNomeFile1()).append(" <-> ").append(menor.getNomeFile2())
                .append(" = ").append(String.format("%.2f", menor.getSimilaridade())).append("\n");
        }
        
        // Exibe no console
        System.out.print(saida.toString());
        
        // Salva arquivo texto (Relatório)
        try (PrintWriter writer = new PrintWriter(new FileWriter("rha/resultado.txt"))) {
            writer.print(saida.toString());
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo resultado.txt: " + e.getMessage());
        }

        // --- 4. GERAÇÃO DO CSV (Planilha) ---
        StringBuilder csv = new StringBuilder();
        // Cabeçalho do CSV
        csv.append("Arquivo 1,Arquivo 2,Similaridade\n");
        
        for (Resultado r : resultadosFiltrados) {
            // Formata o número trocando vírgula por ponto para não quebrar colunas no CSV padrão
            String valorFormatado = String.format("%.4f", r.getSimilaridade()).replace(',', '.');
            
            csv.append(r.getNomeFile1()).append(",")
               .append(r.getNomeFile2()).append(",")
               .append(valorFormatado)
               .append("\n");
        }

        // Salva arquivo CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter("rha/resultados.csv"))) {
            writer.print(csv.toString());
            System.out.println("\nArquivo resultados.csv gerado com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo resultados.csv: " + e.getMessage());
        }
    }
}
