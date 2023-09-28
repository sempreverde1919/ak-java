import java.util.Arrays;
import java.util.Scanner;

public class CurrencyCalculator{

    public static void main(String []args){
        Scanner scanner = new Scanner(System.in);
        int[] currencies = {20000,10000,5000,2000,1000,500,200,100,50,20,10,5};
    //felhasználói input bekérése
        System.out.print("Adja meg a kért pénzösszeget: ");
        int moneyAmount = scanner.nextInt();
        scanner.nextLine();
    //felhasználói input validációja
        while(moneyAmount<1 || moneyAmount%5!=0){
            if(moneyAmount<1){
                System.out.println("HIBA! Csak 0-nál nagyobb összeg fizethető ki.");
                System.out.print("Adja meg a kért pénzösszeget: ");
                moneyAmount = scanner.nextInt();
                scanner.nextLine();
            }
            if(moneyAmount%5!=0){
                System.out.println("HIBA! A kért címlet nem fizethető ki.");
                System.out.print("Adja meg a kért pénzösszeget: ");
                moneyAmount = scanner.nextInt();
                scanner.nextLine();
            }
        }
    //szükséges címletek kiszámítása és kiírása
        int remainingAmount = moneyAmount;
        int[] pieces = new int[currencies.length];
        System.out.println("A kért összeg " + moneyAmount + " Ft.");
        System.out.println("A gép a következő címleteket fizeti ki:");
        for(int i=0; i<currencies.length; i++){
            pieces[i] = remainingAmount / currencies[i];
            remainingAmount = remainingAmount % currencies[i];
            if(pieces[i]!=0){
                System.out.println("    " + currencies[i] + " Ft - " + pieces[i] + " db");
            }
        }
    }

}
