import java.util.Scanner;

public class Matriz {
    private int[][] matriz;

    public Matriz(int linhas, int colunas){
        matriz = new int[linhas][colunas];

    }
    public void preencher(Scanner leitor, char nomeMatriz){
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("Digite o valor da posição %c%d%d: ",Character.toLowerCase(nomeMatriz), i+1, j+1);
                matriz[i][j] = Integer.parseInt(leitor.nextLine());
            }
        }
    }
    public void imprimir(){
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("| %d |", matriz[i][j]);
            }
            System.out.println();
        }
    }
    public int getLinhas(){
        return matriz.length;
    }
    public int getColunas(){
        return matriz[0].length;
    }

    public Matriz somar(Matriz outra){
        if(getLinhas() != outra.getLinhas() || getColunas() != outra.getColunas()){
            return null;
        }

        Matriz resultado = new Matriz(getLinhas(), getColunas());

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                resultado.matriz[i][j] = matriz[i][j] + outra.matriz[i][j];
            }
        }
        return resultado;
    }

    public Matriz subtrair(Matriz outra){
        if(getLinhas() != outra.getLinhas() || getColunas() != outra.getColunas()){
            return null;
        }

        Matriz resultado = new Matriz(getLinhas(), getColunas());

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                resultado.matriz[i][j] = matriz[i][j] - outra.matriz[i][j];
            }
        }
        return resultado;
    }

    public Matriz multiplicar(Matriz outra){
        if(getLinhas() != outra.getColunas()){
            return null;
        }

        Matriz resultado = new Matriz(getLinhas(), getColunas());

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < outra.getColunas(); j++) {
                for (int k = 0; k < getColunas(); k++) {
                    resultado.matriz[i][j] += matriz[i][k] * outra.matriz[k][j];
                }
            }
        }
        return resultado;
    }
    
}
