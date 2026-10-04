import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       
        Scanner leitor = new Scanner(System.in);
        ArrayList<Matriz> matrizes = new ArrayList<>();
        char nomeMatriz = 'A';


        String menu1 = "0";
        while(!menu1.equals("4")){
            System.out.printf(("\n---------- MENU PRINCIPAL ----------")+"\n1 - Adicionar Matriz" + ("\n2 - Visualizar Matrizes") + ("\n3 - Operações com Matrizes") + ("\n4 - Sair") + ("\nDigite a opção desejada: "));
            menu1 = leitor.nextLine();

            switch (menu1) {
                case "1":
                    
                    System.out.printf("\n---------- CADASTRO DA MATRIZ [%c] -----------\n", nomeMatriz);

                    int linhas;
                    int colunas;

                    while(true){
                        try{
                            System.out.printf("\nDigite quantas linhas terá a matriz: ");
                            linhas = Integer.parseInt(leitor.nextLine());

                            System.out.printf("Digite quantas colunas terá a matriz: ");
                            colunas = Integer.parseInt(leitor.nextLine());
                            System.out.println();
                            if(linhas <= 0 || colunas <=0){
                                System.out.println("Linhas e Colunas precisam ser maior do que zero!");

                                continue;
                            }

                            break;


                        } catch(NumberFormatException e){
                            System.out.println("Digite um número inteiro!");
                        }
                    }
                    Matriz novaMatriz = new Matriz(linhas, colunas);

                    novaMatriz.preencher(leitor, nomeMatriz);
                    
                    matrizes.add(novaMatriz);

                    nomeMatriz++;

                    break;
                case "2":

                    if(matrizes.isEmpty()){
                        System.out.println("\nNenhuma matriz cadastrada! Cadastre uma Matriz na opção [1] do menu principal!");
                        break;
                    }
                    
                    System.out.println("\n---------- MATRIZES CADASTRADAS ----------");
                    for (int i = 0; i < matrizes.size(); i++) {

                        System.out.printf("Matriz " +(char) ('A' + i) + ": \n");

                        matrizes.get(i).imprimir();
                        
                        System.out.println();
                    }
                    System.out.println("----------------------------------------");
                    break;
                case "3":
                    //menu com as demais operações
                    break;
                default:
                    System.out.println("\nValor inválido! Tente novamente\n");
                    break;
            }




        }
        leitor.close();
    }
}
