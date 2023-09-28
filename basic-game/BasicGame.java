public class BasicGame{
 
   public static void main(String []args) throws InterruptedException{
      String[][] level = new String[10][10];
      String playerMarker = "O";
      int row = 2;
      int col = 2;
      Direction direction = Direction.RIGHT;

   //pálya inicializálása
      for(int i = 0; i<level.length; i++){
         for(int j = 0; j<level[i].length; j++){
            level[i][j] = "X";          
         }
      }
   

   for(int k=0; k<10; k++){
      switch(direction){
         case UP:row++;break;
         case DOWN:row--;break;
         case LEFT:col--;break;
         case RIGHT:col++;break;
      }
   //pálya és játékos kirajzolása
      for(int i = 0; i<level.length; i++){
         for(int j = 0; j<level[i].length; j++){
            if(i==row && j==col){
               System.out.print(playerMarker);
            }else{
               System.out.print(level[i][j]);
            }
         }
         System.out.println();
      }
      System.out.println("------------");
      Thread.sleep(1000);
   }
 }
}
