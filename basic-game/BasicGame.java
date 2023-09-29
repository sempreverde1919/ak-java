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
            if(i==0 || i==level.length-1 || j==0 || j==level[i].length-1){
               level[i][j] = "X";
            }else{
               level[i][j] = " ";         
            }
         }
      }
   

   for(int k=1; k<=100; k++){
      if(k%10==0){ //irányváltás 10 körönként
         direction = changeDirection(direction);
      }
      
      switch(direction){//irányváltás ha falhoz ér
         case UP:
            if(level[row-1][col].equals(" ")){
               row--;
            }else{
               direction = Direction.RIGHT;
            }
            break;
         case DOWN:
            if(level[row+1][col].equals(" ")){
               row++;
            }else{
               direction = Direction.LEFT;
            }
            break;
         case LEFT:
            if(level[row][col-1].equals(" ")){
               col--;
            }else{
               direction = Direction.UP;
            }
            break;
         case RIGHT:
            if(level[row][col+1].equals(" ")){
               col++;
            }else{
               direction = Direction.DOWN;
            }
            break;
      }
   
      draw(level, playerMarker, row, col);//pálya és játékos kirajzolása

      System.out.println("------------");
      Thread.sleep(1000);
   }
 }

   static void draw(String[][] board, String mark, int x, int y){
      for(int i = 0; i<board.length; i++){
         for(int j = 0; j<board[i].length; j++){ 
            if(i==x && j==y){
               System.out.print(mark);
            }else{
               System.out.print(board[i][j]);
            }   
         }
         System.out.println();
      }
   }

   static Direction changeDirection(Direction direction){
      switch(direction){
         case UP:
            direction = Direction.RIGHT;
            break;
         case DOWN:
            direction = Direction.LEFT;
            break;
         case LEFT:
            direction = Direction.UP;
            break;
         case RIGHT:
            direction = Direction.DOWN;
            break;
      }
      return direction;
   }

}
