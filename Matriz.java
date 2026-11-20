import java.util.Scanner;

public class Matriz {
    // Dados da matriz armazenados em linhas e colunas.
    private double[][] matriz;
    // Tolerância usada para considerar valores muito próximos de zero.
    private static final double EPSILON = 1e-10;

    public Matriz(int linhas, int colunas){
        // Cria uma matriz vazia preenchida inicialmente com zeros.
        matriz = new double[linhas][colunas];

    }

    public void preencher(Scanner leitor, char nomeMatriz){
        // Percorre todas as posições para solicitar os valores ao usuário.
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("Digite o valor da posição %c%d%d: ",Character.toLowerCase(nomeMatriz), i+1, j+1);
                matriz[i][j] = Integer.parseInt(leitor.nextLine());
            }
        }
    }

    public void definirValor(int linha, int coluna, int valor) { 
        // Define um valor em uma posição específica da matriz.
        matriz[linha][coluna] = valor;
    } 

    public void imprimir(){
        // Percorre as linhas e exibe cada valor entre barras.
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                // Trata números muito próximos de zero como zero.
                if (Math.abs(matriz[i][j]) < EPSILON) {
                    System.out.print("| 0 |");
                } else if (Math.abs(matriz[i][j] - Math.rint(matriz[i][j])) < EPSILON) {
                    // Exibe números inteiros sem casas decimais.
                    System.out.printf("| %.0f |", matriz[i][j]);
                } else {
                    // Exibe números decimais com três casas.
                    System.out.printf("| %.3f |", matriz[i][j]);
                }
            }
            System.out.println();
        }
    }
    public int getLinhas(){
        // Retorna a quantidade de linhas.
        return matriz.length;
    }
    public int getColunas(){
        // Retorna a quantidade de colunas da primeira linha.
        return matriz[0].length;
    }

    public double getValor(int linha, int coluna) {
        // Retorna o valor armazenado em uma posição.
        return matriz[linha][coluna];
    }

    public Matriz somar(Matriz outra){
        // A soma só é possível quando as dimensões são iguais.
        if(getLinhas() != outra.getLinhas() || getColunas() != outra.getColunas()){
            return null;
        }

        Matriz resultado = new Matriz(getLinhas(), getColunas());

        // Soma elemento a elemento.
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                resultado.matriz[i][j] = matriz[i][j] + outra.matriz[i][j];
            }
        }
        return resultado;
    }

    public Matriz subtrair(Matriz outra){
        // A subtração só é possível quando as dimensões são iguais.
        if(getLinhas() != outra.getLinhas() || getColunas() != outra.getColunas()){
            return null;
        }

        Matriz resultado = new Matriz(getLinhas(), getColunas());

        // Subtrai elemento a elemento.
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                resultado.matriz[i][j] = matriz[i][j] - outra.matriz[i][j];
            }
        }
        return resultado;
    }

    public Matriz multiplicar(Matriz outra){
        // A quantidade de colunas da primeira deve ser igual às linhas da segunda.
        if(getColunas() != outra.getLinhas()){
            return null;
        }

        Matriz resultado = new Matriz(getLinhas(), outra.getColunas());

        // Percorre linhas da primeira matriz e colunas da segunda.
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < outra.getColunas(); j++) {
                // Soma o produto dos elementos correspondentes.
                for (int k = 0; k < getColunas(); k++) {
                    resultado.matriz[i][j] += matriz[i][k] * outra.matriz[k][j];
                }
            }
        }
        return resultado;
    }
    public Matriz transposta(){
        // A transposta troca linhas por colunas.
        Matriz resultado = new Matriz(getColunas(), getLinhas());

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                resultado.matriz[j][i] = matriz[i][j];
            }
        }
        return resultado;
    }
    public Matriz determinanteSarrus(){
        // Regra de Sarrus aceita matrizes quadradas de até ordem 3.
        if(getLinhas() != getColunas() || getLinhas() > 3){
            return null;
        }

        Matriz resultado = new Matriz(1, 1);
        if (getLinhas() == 1) {
            // O determinante de uma matriz 1x1 é seu único valor.
            resultado.matriz[0][0] = matriz[0][0];
        } else if (getLinhas() == 2) {
            // Calcula o determinante pela diferença dos produtos diagonais.
            resultado.matriz[0][0] =
                matriz[0][0] * matriz[1][1] - matriz[0][1] * matriz[1][0];
        } else {
            // Soma os produtos das diagonais principais e secundárias.
            resultado.matriz[0][0] =
                matriz[0][0] * matriz[1][1] * matriz[2][2]
                    + matriz[0][1] * matriz[1][2] * matriz[2][0]
                    + matriz[0][2] * matriz[1][0] * matriz[2][1]
                    - matriz[0][2] * matriz[1][1] * matriz[2][0]
                    - matriz[0][0] * matriz[1][2] * matriz[2][1]
                    - matriz[0][1] * matriz[1][0] * matriz[2][2];
        }
        return resultado;
    }

    public Matriz determinanteLaplace(){
        // O determinante só pode ser calculado em matrizes quadradas.
        if(getLinhas() != getColunas()){
            return null;
        }

        Matriz resultado = new Matriz(1, 1);
        resultado.matriz[0][0] = calcularDeterminante(matriz);
        return resultado;
    }

    private double calcularDeterminante(double[][] matrizAtual) {
        int tamanho = matrizAtual.length;

        // Caso base para uma matriz 1x1.
        if (tamanho == 1) {
            return matrizAtual[0][0];
        }

        // Caso base para uma matriz 2x2.
        if (tamanho == 2) {
            return matrizAtual[0][0] * matrizAtual[1][1]
                - matrizAtual[0][1] * matrizAtual[1][0];
        }

        double determinante = 0;

        // Percorre a primeira linha e cria uma matriz menor para cada coluna.
        for (int coluna = 0; coluna < tamanho; coluna++) {
            double[][] menor = new double[tamanho - 1][tamanho - 1];

            // Copia os valores restantes para a matriz menor.
            for (int linhaMenor = 1; linhaMenor < tamanho; linhaMenor++) {
                int indiceMenor = 0;
                for (int colunaMenor = 0; colunaMenor < tamanho; colunaMenor++) {
                    if (colunaMenor == coluna) {
                        continue;
                    }
                    menor[linhaMenor - 1][indiceMenor] = matrizAtual[linhaMenor][colunaMenor];
                    indiceMenor++;
                }
            }

            // Alterna o sinal positivo e negativo de cada termo.
            int sinal = (coluna % 2 == 0) ? 1 : -1;
            determinante += sinal * matrizAtual[0][coluna] * calcularDeterminante(menor);
        }

        return determinante;
    }

    public Matriz escalonar() {
        // Trabalha em uma cópia para não modificar a matriz original.
        double[][] trabalho = copiarDados();
        int linhas = trabalho.length;
        int colunas = trabalho[0].length;
        int linhaPivo = 0;

        // Percorre as colunas procurando pivôs não nulos.
        for (int coluna = 0; coluna < colunas && linhaPivo < linhas; coluna++) {
            int linhaEscolhida = linhaPivo;

            // Escolhe a linha com maior valor absoluto para o pivô.
            for (int linha = linhaPivo + 1; linha < linhas; linha++) {
                if (Math.abs(trabalho[linha][coluna]) > Math.abs(trabalho[linhaEscolhida][coluna])) {
                    linhaEscolhida = linha;
                }
            }

            // Não existe pivô nesta coluna.
            if (Math.abs(trabalho[linhaEscolhida][coluna]) < EPSILON) {
                continue;
            }

            // Troca a linha atual pela linha que contém o melhor pivô.
            double[] temporaria = trabalho[linhaPivo];
            trabalho[linhaPivo] = trabalho[linhaEscolhida];
            trabalho[linhaEscolhida] = temporaria;

            // Divide a linha do pivô para transformar o pivô em 1.
            double pivo = trabalho[linhaPivo][coluna];
            for (int j = 0; j < colunas; j++) {
                trabalho[linhaPivo][j] /= pivo;
            }

            // Zera o elemento desta coluna nas demais linhas.
            for (int linha = 0; linha < linhas; linha++) {
                if (linha == linhaPivo) {
                    continue;
                }
                double fator = trabalho[linha][coluna];
                if (Math.abs(fator) < EPSILON) {
                    continue;
                }
                for (int j = 0; j < colunas; j++) {
                    trabalho[linha][j] -= fator * trabalho[linhaPivo][j];
                }
            }

            linhaPivo++;
        }

        return criarMatriz(trabalho);
    }

    public Matriz escalonamento() {
        // Nome alternativo para chamar o método de escalonamento.
        return escalonar();
    }

    public Matriz inversa() {
        // Apenas matrizes quadradas podem possuir inversa.
        if (getLinhas() != getColunas()) {
            return null;
        }

        int tamanho = getLinhas();
        // Cria uma matriz aumentada: [matriz original | matriz identidade].
        double[][] aumentada = new double[tamanho][tamanho * 2];

        for (int linha = 0; linha < tamanho; linha++) {
            for (int coluna = 0; coluna < tamanho; coluna++) {
                aumentada[linha][coluna] = matriz[linha][coluna];
                // A parte direita recebe 1 na diagonal e 0 nas demais posições.
                aumentada[linha][coluna + tamanho] = (linha == coluna) ? 1.0 : 0.0;
            }
        }

        int linhaPivo = 0;
        // Aplica eliminação de Gauss-Jordan em todas as colunas.
        for (int coluna = 0; coluna < tamanho; coluna++) {
            int linhaEscolhida = linhaPivo;

            // Busca o maior pivô disponível na coluna.
            for (int linha = linhaPivo + 1; linha < tamanho; linha++) {
                if (Math.abs(aumentada[linha][coluna]) > Math.abs(aumentada[linhaEscolhida][coluna])) {
                    linhaEscolhida = linha;
                }
            }

            // Nenhum pivô significa que a matriz não possui inversa.
            if (Math.abs(aumentada[linhaEscolhida][coluna]) < EPSILON) {
                return null;
            }

            // Troca linhas para posicionar o pivô na diagonal.
            double[] temporaria = aumentada[linhaPivo];
            aumentada[linhaPivo] = aumentada[linhaEscolhida];
            aumentada[linhaEscolhida] = temporaria;

            // Divide toda a linha pelo valor do pivô.
            double pivo = aumentada[linhaPivo][coluna];
            for (int j = 0; j < tamanho * 2; j++) {
                aumentada[linhaPivo][j] /= pivo;
            }

            // Zera o restante da coluna em todas as linhas.
            for (int linha = 0; linha < tamanho; linha++) {
                if (linha == linhaPivo) {
                    continue;
                }
                double fator = aumentada[linha][coluna];
                if (Math.abs(fator) < EPSILON) {
                    continue;
                }
                for (int j = 0; j < tamanho * 2; j++) {
                    aumentada[linha][j] -= fator * aumentada[linhaPivo][j];
                }
            }
            linhaPivo++;
        }

        // Copia a parte direita da matriz aumentada como resultado.
        double[][] inversa = new double[tamanho][tamanho];
        for (int linha = 0; linha < tamanho; linha++) {
            for (int coluna = 0; coluna < tamanho; coluna++) {
                inversa[linha][coluna] = aumentada[linha][coluna + tamanho];
            }
        }
        return criarMatriz(inversa);
    }

    public Matriz inversaMatriz() {
        // Método auxiliar com outro nome possível.
        return inversa();
    }

    private double[][] copiarDados() {
        // Cria uma cópia dos dados para preservar a matriz original.
        double[][] copia = new double[matriz.length][matriz[0].length];
        for (int linha = 0; linha < matriz.length; linha++) {
            System.arraycopy(matriz[linha], 0, copia[linha], 0, matriz[linha].length);
        }
        return copia;
    }

    private Matriz criarMatriz(double[][] dados) {
        // Converte um vetor bidimensional em um objeto Matriz.
        Matriz nova = new Matriz(dados.length, dados[0].length);
        nova.matriz = dados;
        return nova;
    }
    
}
