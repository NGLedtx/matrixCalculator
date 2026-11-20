import java.util.Scanner;

public class Matriz {
    // Letra que identifica a matriz
    private char nome;
    // Valores da matriz
    private double[][] matriz;

    // Tolerância para considerar um valor como zero
    private static final double EPSILON = 1e-10;

    public Matriz(char nome, int linhas, int colunas) {
        this.nome = nome;
        matriz = new double[linhas][colunas];
    }

    public char getNome() {
        return nome;
    }

    public int getLinhas() {
        return matriz.length;
    }

    public int getColunas() {
        return matriz[0].length;
    }

    public double getValor(int linha, int coluna) {
        return matriz[linha][coluna];
    }

    public void definirValor(int linha, int coluna, double valor) {
        matriz[linha][coluna] = valor;
    }

    public boolean isQuadrada() {
        return getLinhas() == getColunas();
    }

    // Preenche a matriz pelo teclado
    public void preencher(Scanner leitor) {
        for (int i = 0; i < getLinhas(); i++) {
            for (int j = 0; j < getColunas(); j++) {
                while (true) {
                    System.out.printf("Digite o valor da posição %c%d%d: ", Character.toLowerCase(nome), i + 1, j + 1);
                    try {
                        matriz[i][j] = Double.parseDouble(leitor.nextLine().trim().replace(',', '.'));
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido, digite um número.");
                    }
                }
            }
        }
    }

    // Exibe a matriz na tela
    public void imprimir() {
        int largura = 1;
        for (double[] linha : matriz) {
            for (double valor : linha) {
                largura = Math.max(largura, formatar(valor).length());
            }
        }

        for (double[] linha : matriz) {
            StringBuilder sb = new StringBuilder("[ ");
            for (double valor : linha) {
                sb.append(String.format("%" + largura + "s ", formatar(valor)));
            }
            sb.append("]");
            System.out.println(sb);
        }
    }

    // Formata o número para exibição
    private String formatar(double valor) {
        if (Math.abs(valor) < EPSILON) {
            return "0";
        }
        if (Math.abs(valor - Math.rint(valor)) < EPSILON) {
            return String.format("%.0f", valor);
        }
        return String.format("%.3f", valor);
    }

    // Soma duas matrizes
    public Matriz somar(Matriz outra) {
        if (getLinhas() != outra.getLinhas() || getColunas() != outra.getColunas()) {
            return null;
        }
        Matriz resultado = new Matriz('R', getLinhas(), getColunas());
        for (int i = 0; i < getLinhas(); i++) {
            for (int j = 0; j < getColunas(); j++) {
                resultado.matriz[i][j] = matriz[i][j] + outra.matriz[i][j];
            }
        }
        return resultado;
    }

    // Subtrai duas matrizes
    public Matriz subtrair(Matriz outra) {
        if (getLinhas() != outra.getLinhas() || getColunas() != outra.getColunas()) {
            return null;
        }
        Matriz resultado = new Matriz('R', getLinhas(), getColunas());
        for (int i = 0; i < getLinhas(); i++) {
            for (int j = 0; j < getColunas(); j++) {
                resultado.matriz[i][j] = matriz[i][j] - outra.matriz[i][j];
            }
        }
        return resultado;
    }

    // Multiplica duas matrizes
    public Matriz multiplicar(Matriz outra) {
        if (getColunas() != outra.getLinhas()) {
            return null;
        }
        Matriz resultado = new Matriz('R', getLinhas(), outra.getColunas());
        for (int i = 0; i < getLinhas(); i++) {
            for (int j = 0; j < outra.getColunas(); j++) {
                for (int k = 0; k < getColunas(); k++) {
                    resultado.matriz[i][j] += matriz[i][k] * outra.matriz[k][j];
                }
            }
        }
        return resultado;
    }

    // Calcula a transposta
    public Matriz transposta() {
        Matriz resultado = new Matriz('T', getColunas(), getLinhas());
        for (int i = 0; i < getLinhas(); i++) {
            for (int j = 0; j < getColunas(); j++) {
                resultado.matriz[j][i] = matriz[i][j];
            }
        }
        return resultado;
    }

    // Calcula o determinante (Sarrus até 3x3, Laplace acima)
    public double determinante() {
        if (getLinhas() <= 3) {
            return determinanteSarrus();
        }
        return determinanteLaplace(matriz);
    }

    // Determinante pela regra de Sarrus
    private double determinanteSarrus() {
        double[][] m = matriz;
        if (m.length == 1) {
            return m[0][0];
        }
        if (m.length == 2) {
            return m[0][0] * m[1][1] - m[0][1] * m[1][0];
        }
        return m[0][0] * m[1][1] * m[2][2]
             + m[0][1] * m[1][2] * m[2][0]
             + m[0][2] * m[1][0] * m[2][1]
             - m[0][2] * m[1][1] * m[2][0]
             - m[0][0] * m[1][2] * m[2][1]
             - m[0][1] * m[1][0] * m[2][2];
    }

    // Determinante por Laplace
    private double determinanteLaplace(double[][] m) {
        int n = m.length;
        if (n == 1) {
            return m[0][0];
        }
        if (n == 2) {
            return m[0][0] * m[1][1] - m[0][1] * m[1][0];
        }

        double det = 0;
        for (int coluna = 0; coluna < n; coluna++) {
            double[][] menor = new double[n - 1][n - 1];
            for (int i = 1; i < n; i++) {
                int k = 0;
                for (int j = 0; j < n; j++) {
                    if (j != coluna) {
                        menor[i - 1][k++] = m[i][j];
                    }
                }
            }
            int sinal = (coluna % 2 == 0) ? 1 : -1;
            det += sinal * m[0][coluna] * determinanteLaplace(menor);
        }
        return det;
    }

    // Escalona a matriz (Gauss)
    public Matriz escalonar() {
        double[][] m = copiarDados();
        int linhas = m.length;
        int colunas = m[0].length;
        int linhaPivo = 0;

        for (int coluna = 0; coluna < colunas && linhaPivo < linhas; coluna++) {
            int troca = linhaPivo;
            while (troca < linhas && Math.abs(m[troca][coluna]) < EPSILON) {
                troca++;
            }
            if (troca == linhas) {
                continue;
            }
            trocarLinhas(m, linhaPivo, troca);

            for (int i = linhaPivo + 1; i < linhas; i++) {
                double fator = m[i][coluna] / m[linhaPivo][coluna];
                for (int j = coluna; j < colunas; j++) {
                    m[i][j] -= fator * m[linhaPivo][j];
                }
            }
            linhaPivo++;
        }
        return criarMatriz(m);
    }

    // Escalona na forma reduzida (Gauss-Jordan)
    public Matriz escalonarReduzida() {
        double[][] m = copiarDados();
        reduzir(m, getColunas());
        return criarMatriz(m);
    }

    // Calcula a inversa
    public Matriz inversa() {
        if (!isQuadrada()) {
            return null;
        }
        int n = getLinhas();
        double[][] aumentada = new double[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aumentada[i][j] = matriz[i][j];
            }
            aumentada[i][n + i] = 1;
        }

        int pivos = reduzir(aumentada, n);
        if (pivos < n) {
            return null;
        }

        double[][] inv = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv[i][j] = aumentada[i][n + j];
            }
        }
        return criarMatriz(inv);
    }

    // Aplica Gauss-Jordan e retorna a quantidade de pivôs
    private int reduzir(double[][] m, int colunasPivo) {
        int linhas = m.length;
        int totalColunas = m[0].length;
        int linhaPivo = 0;

        for (int coluna = 0; coluna < colunasPivo && linhaPivo < linhas; coluna++) {
            int melhor = linhaPivo;
            for (int i = linhaPivo + 1; i < linhas; i++) {
                if (Math.abs(m[i][coluna]) > Math.abs(m[melhor][coluna])) {
                    melhor = i;
                }
            }
            if (Math.abs(m[melhor][coluna]) < EPSILON) {
                continue;
            }
            trocarLinhas(m, linhaPivo, melhor);

            double pivo = m[linhaPivo][coluna];
            for (int j = 0; j < totalColunas; j++) {
                m[linhaPivo][j] /= pivo;
            }

            for (int i = 0; i < linhas; i++) {
                if (i == linhaPivo) {
                    continue;
                }
                double fator = m[i][coluna];
                for (int j = 0; j < totalColunas; j++) {
                    m[i][j] -= fator * m[linhaPivo][j];
                }
            }
            linhaPivo++;
        }
        return linhaPivo;
    }

    // Troca duas linhas
    private void trocarLinhas(double[][] m, int a, int b) {
        double[] aux = m[a];
        m[a] = m[b];
        m[b] = aux;
    }

    // Copia os valores da matriz
    private double[][] copiarDados() {
        double[][] copia = new double[getLinhas()][];
        for (int i = 0; i < getLinhas(); i++) {
            copia[i] = matriz[i].clone();
        }
        return copia;
    }

    private Matriz criarMatriz(double[][] dados) {
        Matriz nova = new Matriz(nome, dados.length, dados[0].length);
        nova.matriz = dados;
        return nova;
    }
}
