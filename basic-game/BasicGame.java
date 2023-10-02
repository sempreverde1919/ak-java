import java.util.Random;

public class BasicGame{
   
   static final int GAME_LOOP_NUMBER = 100;
   static final int HEIGHT = 15;
   static final int WIDTH = 15;
   static Random RANDOM = new Random();

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
      String[][] level = new String[HEIGHT][WIDTH];
      initLevel(level);
      addRandomWall(level, 1, 1);

      for(int iterationNumber=1; iterationNumber<=GAME_LOOP_NUMBER; iterationNumber++){
      
         //játékos léptetése
         if(iterationNumber%15==0){ 
            playerDirection = changeDirection(playerDirection);
         }
         int[] playeCoordinates = makeMove(playerDirection, level, playerRow, playerCol);
         playerRow = playeCoordinates[0];
         playerCol = playeCoordinates[1];   

         //ellenfél léptetése
         enemyDirection = changeEnemyDirection(level, enemyDirection, playerRow, playerCol, enemyRow, enemyCol);
         if(iterationNumber%2==0){ 
            int[] enemyCoordinates = makeMove(enemyDirection, level, enemyRow, enemyCol);
            enemyRow = enemyCoordinates[0];
            enemyCol = enemyCoordinates[1];  
         }
         draw(level, playerMarker, playerRow, playerCol, enemyMarker, enemyRow, enemyCol); //pálya és játékos kirajzolása
         addSomeDelay(200L, iterationNumber); //késleltetés hozzáadása

         if(playerRow==enemyRow && playerCol==enemyCol) break;
     
      }
      System.out.println("GAME OVER");
 }
 
 //--------------METHODS:

   static void initLevel(String[][] board){
      for(int row = 0; row<board.length; row++){
         for(int col = 0; col<board[row].length; col++){
            if(row==0 || row==HEIGHT-1 || col==0 || col==WIDTH-1){
               board[row][col] = "X";
            }else{
               board[row][col] = " ";         
            }
         }
      }
   }

   static void addRandomWall(String[][]board, int numberOfHorizontalWalls, int numberOfVerticalWalls){
      for(int i=0; i<numberOfHorizontalWalls; i++){
         addHorizontalWall(board);
      }
      for(int i=0; i<numberOfVerticalWalls; i++){
         addVerticalWall(board);
      }
   }

   static void addHorizontalWall(String[][] board){
      int wallWidth = RANDOM.nextInt(WIDTH-3);
      int wallRow = RANDOM.nextInt(HEIGHT-2)+1;
      int wallColumn = RANDOM.nextInt(WIDTH-2-wallWidth);
      for(int i=0; i<wallWidth; i++){
         board[wallRow][wallColumn+i] = "X";
      }
   }

   static void addVerticalWall(String[][] board){
      int wallHeight = RANDOM.nextInt(HEIGHT-3);
      int wallRow = RANDOM.nextInt(HEIGHT-2-wallHeight);
      int wallColumn = RANDOM.nextInt(WIDTH-2)+1;
      for(int i=0; i<wallHeight; i++){
         board[wallRow+i][wallColumn] = "X";
      }
   }

   static void draw(String[][] board, String playerMark, int playerX, int playerY, String enemyMark, int enemyX, int enemyY){
      for(int row = 0; row<HEIGHT; row++){
         for(int col = 0; col<WIDTH; col++){ 
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

   static Direction changeEnemyDirection(String[][] board, Direction enemyDirection, int playerX, int playerY, int enemyX, int enemyY){
      if(playerX < enemyX && board[enemyX-1][enemyY].equals(" ")){
         return Direction.UP;
      };
      if(playerX > enemyX && board[enemyX+1][enemyY].equals(" ")){
         return Direction.DOWN;
      };
      if(playerY < enemyY && board[enemyX][enemyY-1].equals(" ")){
         return Direction.LEFT;
      };
      if(playerY > enemyY && board[enemyX][enemyY+1].equals(" ")){
         return Direction.RIGHT;
      };
      return enemyDirection;
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
