public class BasicGame{
 
   public static void main(String []args) throws InterruptedException{
      String[][] level = new String[10][10];
      String playerMarker = "O";
      int row = 2;
      int col = 2;
      Direction direction = Direction.RIGHT;

      initLevel(level); //pálya inicializálása

      for(int k=1; k<=100; k++){
         if(k%10==0){ //irányváltás 10 körönként
            direction = changeDirection(direction);
         }
         
        int[] coordinates = makeMove(direction, level, row, col); //irányváltás ha falhoz ér
        row = coordinates[0];
        col = coordinates[1];   
      
        draw(level, playerMarker, row, col);//pálya és játékos kirajzolása

        System.out.println("------------");
        Thread.sleep(500);
      }
 }
 
 //--------------METHODS:

   static void initLevel(String[][] board){
      for(int i = 0; i<board.length; i++){
         for(int j = 0; j<board[i].length; j++){
            if(i==0 || i==board.length-1 || j==0 || j==board[i].length-1){
               board[i][j] = "X";
            }else{
               board[i][j] = " ";         
            }
         }
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
            return Direction.RIGHT;
         case DOWN:
            return Direction.LEFT;
         case LEFT:
            return Direction.UP;
         case RIGHT:
            return Direction.DOWN;
      }
      return direction;
   }

   static int[] makeMove(Direction direction, String[][] board, int x, int y){
      switch(direction){
         case UP:
            if(board[x-1][y].equals(" ")){
               x--;
            }else{
               direction = Direction.RIGHT;
            }
            break;
         case DOWN:
            if(board[x+1][y].equals(" ")){
               x++;
            }else{
               direction = Direction.LEFT;
            }
            break;
         case LEFT:
            if(board[x][y-1].equals(" ")){
               y--;
            }else{
               direction = Direction.UP;
            }
            break;
         case RIGHT:
            if(board[x][y+1].equals(" ")){
               y++;
            }else{
               direction = Direction.DOWN;
            }
            break;
         }
         return new int[] {x,y};
   }

}
