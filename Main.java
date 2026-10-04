import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String menu1 = "0";
        Scanner leitor = new Scanner(System.in);

        while(!menu1.equals("4")){
            System.out.printf("1 - Adicionar Matriz" + ("\n2 - Visualizar Matrizes") + ("\n3 - Operações com Matrizes") + ("\n4 - Sair") + ("\nDigite a opção desejada: "));
            menu1 = leitor.nextLine();

            switch (menu1) {
                case "1":
                    //logica de adicionar a matriz
                    break;
                case "2":
                    //logica de visualizar a matriz
                    break;
                case "3":
                    //menu com as demais operações
                    break;
                default:
                    System.out.println("Valor inválido! Tente novamente");
                    break;
            }




        }
        leitor.close();
    }
}
