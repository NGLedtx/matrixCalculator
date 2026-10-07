import java.util.ArrayList;
import java.util.Scanner;

public class Main {
//#region -- Imprimir Lista de Matrizes Cadastradas
    public static void imprimirMatrizes(ArrayList<Matriz> matrizes){
        System.out.println("---------- MATRIZES CADASTRADAS ----------");
        for (int i = 0; i < matrizes.size(); i++) {
            System.out.printf("Matriz " +(char) ('A' + i) + ": \n");

                        matrizes.get(i).imprimir();
                        
                        System.out.println();
        }
        System.out.println("----------------------------------------");
    }
//#endregion    
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

                        
                    }
                    
                    break;
                case "3":
                    if(matrizes.size() <= 2){

                    String menu2 = "0";

                    while (!menu2.equals("4")) {
                        System.out.println("---------- MENU OPERAÇÕES ----------");
                        System.out.printf("\n1 - Soma de Matrizes" + "\n2 - Subtração de Matrizes" + "\n3 - Multiplicação de Matrizes"+ "\n4 - Divisão de Matrizes"+ "\n5 - Operações mais complexas" + "\n6 - Voltar" + "\nDigite a opção que deseja: ");

                        menu2 = leitor.nextLine();

                        switch (menu2) {
                            case "1":
                                System.out.println("Soma!");
                                break;
                        
                            default:
                                System.out.println("Valor inválido! Tente novamente");
                                break;
                        }
                        
                    }
                    }else{
                        System.out.println("Você precisa ter pelo menos duas matrizes cadastradas!");
                        break;
                    }

                    break;

                case "4":
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
