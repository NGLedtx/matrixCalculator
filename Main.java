import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String ARQUIVO_PADRAO = "matrizes.txt";

    // Lista das matrizes cadastradas
    private static ArrayList<Matriz> matrizes = new ArrayList<>();
    private static Scanner leitor = new Scanner(System.in);

    public static void main(String[] args) {
        carregarArquivo(ARQUIVO_PADRAO);

        String opcao = "";
        while (!opcao.equals("0")) {
            System.out.print(
                "\n---------- MENU PRINCIPAL ----------"
                    + "\n[1] - Adicionar Matriz"
                    + "\n[2] - Visualizar Matrizes"
                    + "\n[3] - Operações com Matrizes"
                    + "\n[0] - Sair"
                    + "\nDigite a opção desejada: "
            );
            opcao = leitor.nextLine().trim();

            switch (opcao) {
                case "1": adicionarMatriz(); break;
                case "2": listarMatrizes(); break;
                case "3": menuOperacoes(); break;
                case "0": System.out.println("Saindo... Adeus :("); break;
                default: System.out.println("\nValor inválido! Tente novamente");
            }
        }
        leitor.close();
    }

    // Menu de operações
    private static void menuOperacoes() {
        if (!temMatrizes()) return;

        String opcao = "";
        while (!opcao.equals("0")) {
            System.out.print(
                "\n---------- MENU OPERAÇÕES ----------"
                    + "\n[1] - Soma de Matrizes"
                    + "\n[2] - Subtração de Matrizes"
                    + "\n[3] - Multiplicação de Matrizes"
                    + "\n[4] - Operações mais complexas"
                    + "\n[0] - Voltar"
                    + "\nDigite a opção que deseja: "
            );
            opcao = leitor.nextLine().trim();

            switch (opcao) {
                case "1": somar(); break;
                case "2": subtrair(); break;
                case "3": multiplicar(); break;
                case "4": menuComplexas(); break;
                case "0": System.out.println("\nVoltando ao menu principal..."); break;
                default: System.out.println("\nValor inválido! Tente novamente");
            }
        }
    }

    // Menu de operações complexas
    private static void menuComplexas() {
        String opcao = "";
        while (!opcao.equals("0")) {
            System.out.print(
                "\n---------- MENU OPERAÇÕES COMPLEXAS ----------"
                    + "\n[1] - Determinante"
                    + "\n[2] - Transposta"
                    + "\n[3] - Inversa"
                    + "\n[4] - Escalonamento"
                    + "\n[0] - Voltar ao menu de Operações"
                    + "\nDigite a opção que deseja: "
            );
            opcao = leitor.nextLine().trim();

            switch (opcao) {
                case "1": determinante(); break;
                case "2": transpor(); break;
                case "3": inversa(); break;
                case "4": escalonar(); break;
                case "0": break;
                default: System.out.println("\nValor inválido! Tente novamente");
            }
        }
    }

    // Carrega as matrizes do arquivo na lista
    private static void carregarArquivo(String caminho) {
        try {
            ArrayList<Matriz> lidas = lerArquivo(caminho);
            int adicionadas = 0;
            for (Matriz m : lidas) {
                if (buscarMatriz(m.getNome()) != null) {
                    System.out.println("Matriz " + m.getNome() + " já existe, foi ignorada.");
                } else {
                    matrizes.add(m);
                    adicionadas++;
                }
            }
            System.out.println(adicionadas + " matriz(es) carregada(s) de '" + caminho + "'.");
        } catch (IOException e) {
            System.out.println("Não foi possível ler '" + caminho + "': " + e.getMessage());
            System.out.println("Você ainda pode cadastrar matrizes pela opção [1] do menu principal.");
        }
    }

    // Lê todas as matrizes do arquivo
    private static ArrayList<Matriz> lerArquivo(String caminho) throws IOException {
        ArrayList<Matriz> lidas = new ArrayList<>();
        ArrayList<double[]> linhas = new ArrayList<>();
        char nome = 0;
        int numeroLinha = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                numeroLinha++;

                int comentario = linha.indexOf('#');
                if (comentario >= 0) {
                    linha = linha.substring(0, comentario);
                }
                linha = linha.trim();

                if (linha.isEmpty()) {
                    nome = fecharMatriz(lidas, linhas, nome);
                    continue;
                }

                // Nome da matriz
                if (Character.isLetter(linha.charAt(0))) {
                    fecharMatriz(lidas, linhas, nome);
                    nome = Character.toUpperCase(linha.charAt(0));
                    continue;
                }

                // Matriz sem nome recebe uma letra livre
                if (nome == 0) {
                    nome = proximaLetra(lidas);
                }

                String[] partes = linha.replace('|', ' ').replace(';', ' ').split("\\s+");
                double[] valores = new double[partes.length];
                try {
                    for (int i = 0; i < partes.length; i++) {
                        valores[i] = Double.parseDouble(partes[i].replace(',', '.'));
                    }
                } catch (NumberFormatException e) {
                    throw new IOException("valor inválido na linha " + numeroLinha + " -> \"" + linha + "\"");
                }

                if (!linhas.isEmpty() && valores.length != linhas.get(0).length) {
                    throw new IOException("a matriz " + nome + " tem linhas de tamanhos diferentes (linha " + numeroLinha + ")");
                }
                linhas.add(valores);
            }
        }
        fecharMatriz(lidas, linhas, nome);

        if (lidas.isEmpty()) {
            throw new IOException("nenhuma matriz encontrada no arquivo");
        }
        return lidas;
    }

    // Cria a matriz com as linhas lidas
    private static char fecharMatriz(ArrayList<Matriz> lidas, ArrayList<double[]> linhas, char nome) {
        if (linhas.isEmpty()) {
            return nome;
        }
        Matriz m = new Matriz(nome, linhas.size(), linhas.get(0).length);
        for (int i = 0; i < linhas.size(); i++) {
            for (int j = 0; j < linhas.get(i).length; j++) {
                m.definirValor(i, j, linhas.get(i)[j]);
            }
        }
        lidas.add(m);
        linhas.clear();
        return 0;
    }

    // Retorna a próxima letra disponível
    private static char proximaLetra(ArrayList<Matriz> outras) {
        char letra = 'A';
        while (buscarMatriz(letra) != null || contemNome(outras, letra)) {
            letra++;
        }
        return letra;
    }

    private static boolean contemNome(ArrayList<Matriz> lista, char nome) {
        for (Matriz m : lista) {
            if (m.getNome() == nome) {
                return true;
            }
        }
        return false;
    }

    // Busca a matriz pelo nome
    private static Matriz buscarMatriz(char nome) {
        for (Matriz m : matrizes) {
            if (m.getNome() == nome) {
                return m;
            }
        }
        return null;
    }

    // Exibe todas as matrizes
    private static void listarMatrizes() {
        if (matrizes.isEmpty()) {
            System.out.println("\nNenhuma matriz carregada.");
            return;
        }
        for (Matriz m : matrizes) {
            System.out.printf("%nMatriz %c (%dx%d):%n", m.getNome(), m.getLinhas(), m.getColunas());
            m.imprimir();
        }
    }

    // Mostra o nome e a ordem das matrizes
    private static void mostrarDisponiveis() {
        StringBuilder sb = new StringBuilder("\nMatrizes disponíveis: ");
        for (Matriz m : matrizes) {
            sb.append(String.format("%c(%dx%d)  ", m.getNome(), m.getLinhas(), m.getColunas()));
        }
        System.out.println(sb.toString().trim());
    }

    // Pede ao usuário a letra da matriz
    private static Matriz escolherMatriz(String texto) {
        while (true) {
            System.out.print(texto);
            String entrada = leitor.nextLine().trim().toUpperCase();
            if (entrada.length() != 1) {
                System.out.println("Digite só a letra da matriz (ex: A).");
                continue;
            }
            Matriz m = buscarMatriz(entrada.charAt(0));
            if (m == null) {
                System.out.println("Não existe matriz " + entrada + ".");
                continue;
            }
            return m;
        }
    }

    // Verifica se existe alguma matriz cadastrada
    private static boolean temMatrizes() {
        if (matrizes.isEmpty()) {
            System.out.println("\nNenhuma matriz carregada! Cadastre uma na opção [1] do menu principal.");
            return false;
        }
        return true;
    }

    private static void somar() {
        if (!temMatrizes()) return;
        mostrarDisponiveis();
        Matriz a = escolherMatriz("Primeira matriz: ");
        Matriz b = escolherMatriz("Segunda matriz: ");

        Matriz r = a.somar(b);
        if (r == null) {
            System.out.println("\nNão dá pra somar: as matrizes precisam ter a mesma ordem.");
            return;
        }
        System.out.printf("%n%c + %c =%n", a.getNome(), b.getNome());
        r.imprimir();
    }

    private static void subtrair() {
        if (!temMatrizes()) return;
        mostrarDisponiveis();
        Matriz a = escolherMatriz("Primeira matriz: ");
        Matriz b = escolherMatriz("Segunda matriz: ");

        Matriz r = a.subtrair(b);
        if (r == null) {
            System.out.println("\nNão dá pra subtrair: as matrizes precisam ter a mesma ordem.");
            return;
        }
        System.out.printf("%n%c - %c =%n", a.getNome(), b.getNome());
        r.imprimir();
    }

    private static void multiplicar() {
        if (!temMatrizes()) return;
        mostrarDisponiveis();
        Matriz a = escolherMatriz("Primeira matriz: ");
        Matriz b = escolherMatriz("Segunda matriz: ");

        Matriz r = a.multiplicar(b);
        if (r == null) {
            System.out.printf("%nNão dá pra multiplicar: %c tem %d colunas e %c tem %d linhas (precisam ser iguais).%n",
                    a.getNome(), a.getColunas(), b.getNome(), b.getLinhas());
            return;
        }
        System.out.printf("%n%c . %c =%n", a.getNome(), b.getNome());
        r.imprimir();
    }

    private static void transpor() {
        if (!temMatrizes()) return;
        mostrarDisponiveis();
        Matriz a = escolherMatriz("Matriz: ");

        System.out.printf("%nTransposta de %c:%n", a.getNome());
        a.transposta().imprimir();
    }

    private static void determinante() {
        if (!temMatrizes()) return;
        mostrarDisponiveis();
        Matriz a = escolherMatriz("Matriz: ");

        if (!a.isQuadrada()) {
            System.out.println("\nSó existe determinante de matriz quadrada.");
            return;
        }
        double det = a.determinante();
        if (Math.abs(det) < 1e-10) det = 0;

        if (det == Math.rint(det)) {
            System.out.printf("%ndet(%c) = %.0f%n", a.getNome(), det);
        } else {
            System.out.printf("%ndet(%c) = %.3f%n", a.getNome(), det);
        }
    }

    private static void escalonar() {
        if (!temMatrizes()) return;
        mostrarDisponiveis();
        Matriz a = escolherMatriz("Matriz: ");

        System.out.printf("%nMatriz %c original:%n", a.getNome());
        a.imprimir();
        System.out.printf("%nForma escalonada (Gauss):%n");
        a.escalonar().imprimir();
        System.out.printf("%nForma escalonada reduzida (Gauss-Jordan):%n");
        a.escalonarReduzida().imprimir();
    }

    private static void inversa() {
        if (!temMatrizes()) return;
        mostrarDisponiveis();
        Matriz a = escolherMatriz("Matriz: ");

        if (!a.isQuadrada()) {
            System.out.println("\nSó matriz quadrada pode ter inversa.");
            return;
        }
        Matriz inv = a.inversa();
        if (inv == null) {
            System.out.printf("%n%c não tem inversa (determinante igual a zero).%n", a.getNome());
            return;
        }
        System.out.printf("%nInversa de %c:%n", a.getNome());
        inv.imprimir();
    }

    // Menu de cadastro de matrizes
    private static void adicionarMatriz() {
        String opcao = "";
        while (!opcao.equals("0")) {
            System.out.print(
                "\n---------- SELECIONE O MÉTODO DE CADASTRO ----------"
                    + "\n[1] - Importar matrizes"
                    + "\n[2] - Cadastrar manualmente"
                    + "\n[0] - Voltar"
                    + "\nDigite a opção: "
            );
            opcao = leitor.nextLine().trim();

            switch (opcao) {
                case "1":
                    System.out.print("\nInforme o caminho do arquivo: ");
                    carregarArquivo(leitor.nextLine().trim());
                    break;
                case "2":
                    cadastrarManual();
                    break;
                case "0":
                    System.out.println("\nVoltando ao menu principal!");
                    break;
                default:
                    System.out.println("\nDigite um número válido");
            }
        }
    }

    // Cadastra uma matriz digitada pelo usuário
    private static void cadastrarManual() {
        char nome = proximaLetra(new ArrayList<>());
        System.out.printf("%n---------- CADASTRO DA MATRIZ [%c] ----------%n", nome);
        int linhas = lerInteiroPositivo("Digite quantas linhas terá a matriz: ");
        int colunas = lerInteiroPositivo("Digite quantas colunas terá a matriz: ");

        Matriz nova = new Matriz(nome, linhas, colunas);
        nova.preencher(leitor);
        matrizes.add(nova);
        System.out.printf("%nMatriz %c cadastrada:%n", nome);
        nova.imprimir();
    }

    // Lê um número inteiro maior que zero
    private static int lerInteiroPositivo(String texto) {
        while (true) {
            System.out.print(texto);
            try {
                int valor = Integer.parseInt(leitor.nextLine().trim());
                if (valor > 0) {
                    return valor;
                }
                System.out.println("Precisa ser maior que zero.");
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro.");
            }
        }
    }
}
