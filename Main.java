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
    public static Matriz selecionarMatriz(ArrayList<Matriz> matrizes, Scanner leitor){
        while(true){
            System.out.printf("Digite a matriz desejada: ");

            String entrada = leitor.nextLine().trim().toUpperCase();

            if(entrada.length() != 1){
                System.out.println("Digite apenas uma letra!");
                continue;
            }

            char escolha = entrada.charAt(0);
            int indice = escolha - 'A';

            if(indice < 0 || indice >= matrizes.size()){
                System.out.println("Essa matriz não está cadastrada!");
                continue;
            }
            return matrizes.get(indice);
        }
    }
    public static void main(String[] args) {
       
        Scanner leitor = new Scanner(System.in);
        ArrayList<Matriz> matrizes = new ArrayList<>();
        char nomeMatriz = 'A';


        String menu1 = "0";
        while(!menu1.equals("4")){
            //#region -- Menu Principal
            System.out.printf(("\n---------- MENU PRINCIPAL ----------")+"\n1 - Adicionar Matriz" + ("\n2 - Visualizar Matrizes") + ("\n3 - Operações com Matrizes") + ("\n4 - Sair") + ("\nDigite a opção desejada: "));
            //#endregion

            menu1 = leitor.nextLine();

            switch (menu1) {
                case "1":
                    int linhas;
                    int colunas;

                    System.out.printf("\n---------- CADASTRO DA MATRIZ [%c] -----------\n", nomeMatriz);
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
                    
                    imprimirMatrizes(matrizes);
                    break;
                case "3":
                    if(matrizes.size() >= 1){

                    String menu2 = "0";

                    while (!menu2.equals("5")) {
                        //#region -- Menu Operações
                        System.out.println("\n---------- MENU OPERAÇÕES ----------");
                        System.out.printf("\n1 - Soma de Matrizes" + "\n2 - Subtração de Matrizes" + "\n3 - Multiplicação de Matrizes"+"\n4 - Operações mais complexas" + "\n5 - Voltar" + "\nDigite a opção que deseja: ");
                        //#endregion

                        menu2 = leitor.nextLine();

                        switch (menu2) {
                            case "1":
                                
                                imprimirMatrizes(matrizes);
                                Matriz matriz1 = selecionarMatriz(matrizes, leitor);
                                Matriz matriz2 = selecionarMatriz(matrizes, leitor);

                                Matriz resultado = matriz1.somar(matriz2);
                                if(resultado == null){
                                    System.out.println("\nErro: A soma de matrizes requer que elas tenham o mesmo número de linhas e colunas.");
                                    break;
                                }else{
                                    System.out.println("\nResultado da soma: ");
                                    resultado.imprimir();
                                }
                                break;

                            case "2":

                                imprimirMatrizes(matrizes);
                                Matriz matriz3 = selecionarMatriz(matrizes, leitor);
                                Matriz matriz4 = selecionarMatriz(matrizes, leitor);

                                Matriz resultado2 = matriz3.subtrair(matriz4);
                                if(resultado2 == null){
                                    System.out.println("\nErro: A soma de matrizes requer que elas tenham o mesmo número de linhas e colunas.");
                                    break;
                                }else{
                                    System.out.println("\nResultado da subtração: ");
                                    resultado2.imprimir();
                                }
                                break;

                            case "3":

                                imprimirMatrizes(matrizes);
                                Matriz matriz5 = selecionarMatriz(matrizes, leitor);
                                Matriz matriz6 = selecionarMatriz(matrizes, leitor);

                                Matriz resultado3 = matriz5.multiplicar(matriz6);

                                if(resultado3 == null){
                                    System.out.println("Erro: O número de colunas da primeira matriz deve ser igual ao número de linhas da segunda.");
                                    break;
                                }
                                else{
                                    System.out.println("\nResultado da multiplicação: ");
                                    resultado3.imprimir();
                                }
                                break;
                            
                            case "4":
                                String menu3 = "0";

                                while (!menu3.equals("5")) {
                                    //#region Menu Operações Complexas
                                    System.out.printf("\n---------- MENU OPERAÇÕES COMPLEXAS ----------" + "\n1 - Determinante" + "\n2 - Transposta" + "\n3 - Inversa" + "\n4 - Voltar ao menu de Operações");
                                    //#endregion

                                }

                                break;
                            
                            case "5":
                                System.out.println("\n Voltando ao menu principal...");
                                break;


                            default:
                                System.out.println("\nValor inválido! Tente novamente");
                                break;
                                
                        }
                        
                        
                    }
                    }else{
                        System.out.println("\nVocê precisa ter pelo menos uma matriz cadastrada!");
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
