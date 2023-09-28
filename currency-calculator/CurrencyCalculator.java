import java.util.Arrays;
import java.util.Scanner;

public class CurrencyCalculator{

    public static void main(String []args){
        Scanner scanner = new Scanner(System.in);
        int[] currencies = {20000,10000,5000,2000,1000,500,200,100,50,20,10,5};
        System.out.print("Adja meg a kért pénzösszeget: ");
        int moneyAmount = scanner.nextInt();
        scanner.nextLine();

        int remainingAmount = moneyAmount;
        int[] pieces = new int[currencies.length];
        for(int i=0; i<currencies.length; i++){
            pieces[i] = remainingAmount / currencies[i];
            remainingAmount = remainingAmount % currencies[i];
        }

        System.out.println(Arrays.toString(pieces));      

        
    }

}
