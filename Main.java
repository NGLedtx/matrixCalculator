import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    //#region -- Imprimir Lista de Matrizes Cadastradas
    public static void imprimirMatrizes(ArrayList<Matriz> matrizes) {
        // Exibe todas as matrizes cadastradas usando letras como nomes.
        System.out.println("---------- MATRIZES CADASTRADAS ----------");
        for (int i = 0; i < matrizes.size(); i++) {
            System.out.printf("Matriz " + (char) ('A' + i) + ": \n");
            matrizes.get(i).imprimir();
            System.out.println();
        }
        System.out.println("----------------------------------------");
    }
    //#endregion

    public static Matriz selecionarMatriz(ArrayList<Matriz> matrizes, Scanner leitor) {
        // Solicita uma matriz até o usuário informar uma letra válida.
        while (true) {
            System.out.printf("Digite a matriz desejada: ");

            String entrada = leitor.nextLine().trim().toUpperCase();

            // Exige que a entrada seja exatamente uma letra.
            if (entrada.length() != 1) {
                System.out.println("Digite apenas uma letra!");
                continue;
            }

            char escolha = entrada.charAt(0);
            int indice = escolha - 'A';

            // Verifica se a letra corresponde a uma matriz cadastrada.
            if (indice < 0 || indice >= matrizes.size()) {
                System.out.println("Essa matriz não está cadastrada!");
                continue;
            }
            return matrizes.get(indice);
        }
    }

   
    private static Matriz carregarMatriz(String caminhoArquivo) throws IOException { 
        // Armazena temporariamente todas as linhas lidas do arquivo.
        ArrayList<int[]> linhasMatriz = new ArrayList<>();
        try (BufferedReader leitorArquivo = new BufferedReader(new FileReader(caminhoArquivo))) { 
            String linhaArquivo; 
            while ((linhaArquivo = leitorArquivo.readLine()) != null) { 
                // Remove separadores comuns e espaços extras da linha.
                String linhaLimpa = linhaArquivo.replace('|', ' ').replace(',', ' ').replace(';', ' ').trim(); 
                if (linhaLimpa.isEmpty()) { 
                    continue; 
                } 
                String[] valoresTexto = linhaLimpa.split("\\s+"); 
                int[] valoresLinha = new int[valoresTexto.length];
                // Converte cada valor textual em inteiro.
                for (int coluna = 0; coluna < valoresTexto.length; coluna++) {
                    valoresLinha[coluna] = Integer.parseInt(valoresTexto[coluna]); 
                } 
                // Todas as linhas devem possuir o mesmo tamanho.
                if (!linhasMatriz.isEmpty() && valoresLinha.length != linhasMatriz.get(0).length) { 
                    throw new IOException("Todas as linhas do arquivo precisam ter a mesma quantidade de valores."); 
                } 
                linhasMatriz.add(valoresLinha);
            } 
        }
        if (linhasMatriz.isEmpty()) { 
            throw new IOException("O arquivo não contém valores para formar uma matriz."); 
        } 
        // Cria o objeto Matriz com as dimensões encontradas.
        Matriz novaMatriz = new Matriz(linhasMatriz.size(), linhasMatriz.get(0).length); 
        for (int linha = 0; linha < linhasMatriz.size(); linha++) { 
            for (int coluna = 0; coluna < linhasMatriz.get(linha).length; coluna++) { 
                novaMatriz.definirValor(linha, coluna, linhasMatriz.get(linha)[coluna]);
            } 
        } 
        return novaMatriz;
    }

    public static void main(String[] args) {
        // Gerencia a entrada do usuário e a lista de matrizes cadastradas.
        Scanner leitor = new Scanner(System.in);
        ArrayList<Matriz> matrizes = new ArrayList<>();
        char nomeMatriz = 'A';

        String menu1 = "";
        // Repete o menu principal até o usuário escolher sair.
        while (!menu1.equals("0")) {
            //#region -- Menu Principal
            System.out.printf(
                ("\n---------- MENU PRINCIPAL ----------")
                    + "\n[1] - Adicionar Matriz"
                    + ("\n[2] - Visualizar Matrizes")
                    + ("\n[3] - Operações com Matrizes")
                    + ("\n[0] - Sair")
                    + ("\nDigite a opção desejada: ")
            );
            //#endregion

            menu1 = leitor.nextLine();

            int linhas = 0;
            int colunas = 0;

            switch (menu1) {

                case "1":
                    // Submenu responsável pelo cadastro ou importação.
                    String caseCadastro;
                    do {
                        System.out.printf(
                            "\n---------- SELECIONE O MÉTODO DE CADASTRO ----------"
                                + "\n[1] - Importar matrizes"
                                + "\n[2] - Cadastrar manualmente"
                                + "\n[0] - Voltar"
                                + "\nDigite a opção: "
                        );
                        caseCadastro = leitor.nextLine();

                        switch (caseCadastro) {
                            case "1":
                                // Solicita o caminho e tenta importar o arquivo.
                                System.out.printf("\nInforme o caminho do arquivo: ");

                                String caminhoArquivo = leitor.nextLine(); 

                                try {
                                    Matriz novaMatriz = carregarMatriz(caminhoArquivo);

                                    matrizes.add(novaMatriz); 
                                    nomeMatriz++; 
                                    System.out.println("\nMatriz importada com sucesso:");
                                    novaMatriz.imprimir(); 
                                } catch (IOException | NumberFormatException e) { 
                                    System.out.println("Não foi possível importar a matriz: " + e.getMessage()); 
                                } 
                                break;
                            case "2": {
                                // Cadastra uma matriz informando dimensões e valores.
                                System.out.printf(
                                    "\n---------- CADASTRO DA MATRIZ [%c] -----------\n",
                                    nomeMatriz
                                );
                                while (true) {
                                    try {
                                        // Lê as dimensões da matriz.
                                        System.out.printf("\nDigite quantas linhas terá a matriz: ");
                                        linhas = Integer.parseInt(leitor.nextLine());

                                        System.out.printf("Digite quantas colunas terá a matriz: ");
                                        colunas = Integer.parseInt(leitor.nextLine());
                                        System.out.println();
                                        if (linhas <= 0 || colunas <= 0) {
                                            System.out.println(
                                                "Linhas e Colunas precisam ser maior do que zero!"
                                            );

                                            continue;
                                        }

                                        break;
                                    } catch (NumberFormatException e) {
                                        System.out.println("Digite um número inteiro!");
                                    }
                                }

                                Matriz novaMatriz = new Matriz(linhas, colunas);
                                novaMatriz.preencher(leitor, nomeMatriz);
                                matrizes.add(novaMatriz);
                                nomeMatriz++;
                                break;
                            }
                            case "0":
                                System.out.println("\nVoltando ao menu principal!");
                                break;
                            default:
                                System.out.println("\nDigite um número válido");
                                break;
                        }
                    } while (!caseCadastro.equals("0"));
                    break;
                case "2":
                    // Verifica se existe pelo menos uma matriz para visualizar.
                    if (matrizes.isEmpty()) {
                        System.out.println(
                            "\nNenhuma matriz cadastrada! Cadastre uma Matriz na opção [1] do menu principal!"
                        );
                        break;
                    }

                    imprimirMatrizes(matrizes);
                    break;
                case "3":
                    // Verifica se existe uma matriz antes de acessar as operações.
                    if (matrizes.size() >= 1) {
                        String menu2 = "";

                        // Mantém o usuário no menu de operações até escolher voltar.
                        while (!menu2.equals("0")) {
                            //#region -- Menu Operações
                            System.out.println("\n---------- MENU OPERAÇÕES ----------");
                            System.out.printf(
                                "\n[1] - Soma de Matrizes"
                                    + "\n[2] - Subtração de Matrizes"
                                    + "\n[3] - Multiplicação de Matrizes"
                                    + "\n[4] - Operações mais complexas"
                                    + "\n[0] - Voltar"
                                    + "\nDigite a opção que deseja: "
                            );
                            //#endregion

                            menu2 = leitor.nextLine();

                            switch (menu2) {
                                case "1":
                                    // Seleciona duas matrizes e calcula a soma.
                                    imprimirMatrizes(matrizes);
                                    Matriz matriz1 = selecionarMatriz(matrizes, leitor);
                                    Matriz matriz2 = selecionarMatriz(matrizes, leitor);

                                    Matriz resultado = matriz1.somar(matriz2);
                                    if (resultado == null) {
                                        System.out.println(
                                            "\nERRO: A soma de matrizes requer que elas tenham o mesmo número de linhas e colunas."
                                        );
                                        break;
                                    } else {
                                        System.out.println("\nResultado da soma: ");
                                        resultado.imprimir();
                                    }
                                    break;

                                case "2":
                                    // Seleciona duas matrizes e calcula a subtração.
                                    imprimirMatrizes(matrizes);
                                    Matriz matriz3 = selecionarMatriz(matrizes, leitor);
                                    Matriz matriz4 = selecionarMatriz(matrizes, leitor);

                                    Matriz resultado2 = matriz3.subtrair(matriz4);
                                    if (resultado2 == null) {
                                        System.out.println(
                                            "\nERRO: A soma de matrizes requer que elas tenham o mesmo número de linhas e colunas."
                                        );
                                        break;
                                    } else {
                                        System.out.println("\nResultado da subtração: ");
                                        resultado2.imprimir();
                                    }
                                    break;

                                case "3":
                                    // Seleciona duas matrizes e calcula o produto.
                                    imprimirMatrizes(matrizes);
                                    Matriz matriz5 = selecionarMatriz(matrizes, leitor);
                                    Matriz matriz6 = selecionarMatriz(matrizes, leitor);

                                    Matriz resultado3 = matriz5.multiplicar(matriz6);

                                    if (resultado3 == null) {
                                        System.out.println(
                                            "ERRO: O número de colunas da primeira matriz deve ser igual ao número de linhas da segunda."
                                        );
                                        break;
                                    } else {
                                        System.out.println("\nResultado da multiplicação: ");
                                        resultado3.imprimir();
                                    }
                                    break;

                                case "4":
                                    // Abre o submenu com operações que exigem cálculos avançados.
                                    String menu3 = "";

                                    while (!menu3.equals("0")) {
                                        //#region Menu Operações Complexas
                                        System.out.printf(
                                            "\n---------- MENU OPERAÇÕES COMPLEXAS ----------"
                                                + "\n[1] - Determinante"
                                                + "\n[2] - Transposta"
                                                + "\n[3] - Inversa"
                                                + "\n[4] - Escalonamento"
                                                + "\n[0] - Voltar ao menu de Operações"
                                                + "\nDigite a opção que deseja: "
                                        );
                                        menu3 = leitor.nextLine();
                                        switch (menu3) {
                                            case "1":
                                                // Calcula o determinante pelo método adequado.
                                                imprimirMatrizes(matrizes);
                                                Matriz matrizDeterminante = selecionarMatriz(matrizes, leitor);
                                                Matriz resultadoDeterminante;
                                                if (matrizDeterminante.getLinhas() <= 3
                                                        && matrizDeterminante.getColunas() <= 3) {
                                                    resultadoDeterminante = matrizDeterminante.determinanteSarrus();
                                                } else {
                                                    resultadoDeterminante = matrizDeterminante.determinanteLaplace();
                                                }
                                                if (resultadoDeterminante == null) {
                                                    System.out.println(
                                                        "\nERRO: O determinante só pode ser calculado em matrizes quadradas."
                                                    );
                                                    break;
                                                }
                                                System.out.println("\nDeterminante da matriz selecionada:");
                                                resultadoDeterminante.imprimir();
                                                break;
                                            case "2":
                                                // Calcula e exibe a matriz transposta.
                                                imprimirMatrizes(matrizes);
                                                Matriz matriz7 = selecionarMatriz(matrizes, leitor);
                                                Matriz transposta = matriz7.transposta();
                                                System.out.println("\nTransposta da matriz selecionada:\n");
                                                transposta.imprimir();
                                                break;
                                            case "3":
                                                // Tenta calcular a matriz inversa.
                                                imprimirMatrizes(matrizes);
                                                Matriz matrizInversa = selecionarMatriz(matrizes, leitor);
                                                Matriz resultadoInversa = matrizInversa.inversa();
                                                if (resultadoInversa == null) {
                                                    System.out.println(
                                                        "\nERRO: A inversa só existe para matrizes quadradas não singulares."
                                                    );
                                                    break;
                                                }
                                                System.out.println("\nInversa da matriz selecionada:");
                                                resultadoInversa.imprimir();
                                                break;
                                            case "4":
                                                // Calcula a forma escalonada reduzida por linhas.
                                                imprimirMatrizes(matrizes);
                                                Matriz matrizEscalonamento = selecionarMatriz(matrizes, leitor);
                                                Matriz resultadoEscalonamento = matrizEscalonamento.escalonar();
                                                System.out.println("\nMatriz escalonada (forma escalonada reduzida por linhas):");
                                                resultadoEscalonamento.imprimir();
                                                break;
                                            default:
                                                break;
                                        }
                                        //#endregion
                                    }

                                    break;

                                case "0":
                                    System.out.println("\n Voltando ao menu principal...");
                                    break;

                                default:
                                    System.out.println("\nValor inválido! Tente novamente");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("\nVocê precisa ter pelo menos uma matriz cadastrada!");
                        break;
                    }

                    break;

                case "0":
                    System.out.println("Saindo... Adeus :( ");
                    break;

                default:
                    System.out.println("\nValor inválido! Tente novamente\n");
                    break;
            }

        }
        leitor.close();
    }
}
