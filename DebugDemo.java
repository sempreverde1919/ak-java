public class DebugDemo {
    public static void main(String[] args){
        System.out.println("Helló!");
        int apples = 10;
        int pears = 15;
        int fruits = calculate(apples, pears);
        printResult(fruits);
        System.out.println("Viszlát!");
    }

    static int calculate(int apples, int pears){
        if(apples<0){
            System.out.println("Az almák száma nem lehet negatív");
        }
        if(pears<0){
            System.out.println("A körték száma nem lehet negatív");
        }
        return apples + pears;
    }

    static void printResult(int fruits){
        System.out.print("A gyümölcsök száma ");
        System.out.println(fruits);
    }
}
