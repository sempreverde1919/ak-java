import java.util.Random;

public class BasicGame{
   
   static int gameLoopNumber = 100;
   static int height = 15;
   static int width = 15;

   public static void main(String []args) throws InterruptedException{
      String playerMarker = "O";
      int playerRow = 2;
      int playerCol = 2;
      Direction playerDirection = Direction.RIGHT;

      String enemyMarker = "-";
      int enemyRow = 7;
      int enemyCol = 4;
      Direction enemyDirection = Direction.LEFT;

      //pálya inicializálása
      String[][] level = new String[height][width];
      initLevel(level);

      for(int iterationNumber=1; iterationNumber<=gameLoopNumber; iterationNumber++){
         /*
         //játékos léptetése
         if(iterationNumber%15==0){ 
            playerDirection = changeDirection(playerDirection);
         }
         int[] playeCoordinates = makeMove(playerDirection, level, playerRow, playerCol);
         playerRow = playeCoordinates[0];
         playerCol = playeCoordinates[1];   

         //ellenfél léptetése
         if(iterationNumber%10==0){ 
            enemyDirection = changeDirection(enemyDirection);
         }
         int[] enemyCoordinates = makeMove(enemyDirection, level, enemyRow, enemyCol);
         enemyRow = enemyCoordinates[0];
         enemyCol = enemyCoordinates[1];  
      
         draw(level, playerMarker, playerRow, playerCol, enemyMarker, enemyRow, enemyCol); //pálya és játékos kirajzolása
         addSomeDelay(200L, iterationNumber); //késleltetés hozzáadása

         if(playerRow==enemyRow && playerCol==enemyCol) break;

         */
        addRandomWall();
      }
      System.out.println("GAME OVER");
 }
 
 //--------------METHODS:

   static void initLevel(String[][] board){
      for(int row = 0; row<board.length; row++){
         for(int col = 0; col<board[row].length; col++){
            if(row==0 || row==height-1 || col==0 || col==width-1){
               board[row][col] = "X";
            }else{
               board[row][col] = " ";         
            }
         }
      }
   }

   static void addRandomWall(){
      Random random = new Random();
            
      int[] horizontalWallCoordinates = new int[2];
      horizontalWallCoordinates[0] = random.nextInt(height);
      horizontalWallCoordinates[1] = random.nextInt(width);
      int horizontalWallLength = random.nextInt(width-3);
      
      int[] verticalWallCoordinates = new int[2];
      int verticalWallLength = random.nextInt(height-3);
      verticalWallCoordinates[0] = random.nextInt(verticalWallLength);
      verticalWallCoordinates[1] = random.nextInt(width);

      System.out.println(verticalWallLength + "    " + verticalWallCoordinates[0] + " " + verticalWallCoordinates[1]);
   }

   static void draw(String[][] board, String playerMark, int playerX, int playerY, String enemyMark, int enemyX, int enemyY){
      for(int row = 0; row<height; row++){
         for(int col = 0; col<width; col++){ 
            if(row==playerX && col==playerY){
               System.out.print(playerMark);
            }else if(row==enemyX && col==enemyY){
               System.out.print(enemyMark);
            }else{
               System.out.print(board[row][col]);
            }   
         }
         System.out.println();
      }
   }

   static void addSomeDelay(long timeout, int iteration) throws InterruptedException{
      System.out.println("------"+iteration+"-------");
      Thread.sleep(timeout);
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
