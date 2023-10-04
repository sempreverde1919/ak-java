import java.util.Random;

public class BasicGame{
   
   static final int GAME_LOOP_NUMBER = 100;
   static final int HEIGHT = 15;
   static final int WIDTH = 15;
   static Random RANDOM = new Random();
   static String gameResult = "GAME OVER";

   public static void main(String []args) throws InterruptedException{
   
      //pálya inicializálása
      String[][] level = new String[HEIGHT][WIDTH];
      initLevel(level);
      addRandomWall(level, 2, 3);
   
      //játékos inicializálása
      String playerMarker = "O";
      int playerRow = getRandomPosition(level)[0];
      int playerCol = getRandomPosition(level)[1];
      Direction playerDirection = Direction.RIGHT;
   
      //ellenfél inicializálása
      String enemyMarker = "-";
      int enemyRow;
      int enemyCol;
      do{
         enemyRow = getRandomPosition(level)[0];
         enemyCol = getRandomPosition(level)[1];
      }while((Math.abs(playerRow-enemyRow) + Math.abs(playerCol-enemyCol) < 10)); //ellenfél és játékos legalább 10 lépésre legyen egymástól
      Direction enemyDirection = Direction.LEFT;
   
      //power-up inicializálása
      String powerUpMarker = "*";
      int powerUpRow = getRandomPosition(level)[0];
      int powerUpCol = getRandomPosition(level)[1];
      boolean isPowerUpOnBoard = false; //power-up nincs a pályán
      boolean isPowerUpActive = false; //játékos nem vette fel a power-up-ot
      int activePowerUpCounter = 0; //hány kör óta vette fel a játékos a power-upot;

      //-----------------GAME-LOOP-----------------
      for(int iterationNumber=1; iterationNumber<=GAME_LOOP_NUMBER; iterationNumber++){      
         //játékos irányválasztása
         if(isPowerUpOnBoard){ //ha a power-up a pályán van, a játékos megpróbálja felvenni
            playerDirection = changeDirectionTowards(level, playerDirection, powerUpRow, powerUpCol, playerRow, playerCol);
         }else if(isPowerUpActive){
            playerDirection = changeDirectionTowards(level, playerDirection, enemyRow, enemyCol, playerRow, playerCol);
         }else if(iterationNumber%15==0){
            playerDirection = changeDirection(playerDirection);
         }
         //játékos léptetése
         int[] playerCoordinates = makeMove(playerDirection, level, playerRow, playerCol);
         playerRow = playerCoordinates[0];
         playerCol = playerCoordinates[1];   

         //ellenfél irányválasztása
         if(isPowerUpActive){
            enemyDirection = changeDirectionTowards(level, enemyDirection, enemyRow, enemyCol, playerRow, playerCol);
         }else{
            enemyDirection = changeDirectionTowards(level, enemyDirection, playerCol, playerRow, enemyRow, enemyCol);
         }
         //ellenfél léptetése
         if(iterationNumber%2==0){ //két körönként lép
            int[] enemyCoordinates = makeMove(enemyDirection, level, enemyRow, enemyCol);
            enemyRow = enemyCoordinates[0];
            enemyCol = enemyCoordinates[1];  
         }

         //power-up feltűnése/eltűnése
         if(iterationNumber%20==0 && !isPowerUpActive){
            isPowerUpOnBoard = !isPowerUpOnBoard;
            powerUpRow = getRandomPosition(level)[0];
            powerUpCol = getRandomPosition(level)[1];
         }

         if(isPowerUpActive && activePowerUpCounter<20){ //ha a power-up-ot felvette a játékos, az húsz körig aktív
            activePowerUpCounter++;
         }else{
            isPowerUpActive = false;
         }

         if(isPowerUpOnBoard && playerRow==powerUpRow && playerCol==powerUpCol){ //ha játékos rálép a power-up-ra
            isPowerUpActive = true;
            activePowerUpCounter = 1;
            isPowerUpOnBoard = false;
         }

         draw(level, playerMarker, playerRow, playerCol, enemyMarker, enemyRow, enemyCol, powerUpMarker, powerUpRow, powerUpCol, isPowerUpOnBoard); //kirajzolás
         addSomeDelay(200L, iterationNumber, isPowerUpActive, activePowerUpCounter); //késleltetés hozzáadása
     
         if(isPowerUpActive && playerRow==enemyRow && playerCol==enemyCol){//ha a játékos elkapja az ellenfelet
            gameResult += " - PLAYER WINS";
            break;
         }else if(playerRow==enemyRow && playerCol==enemyCol){//ha az ellenfél kapja el a játékost
            gameResult += " - ENEMY WINS";
            break;
         }

      }
      System.out.println(gameResult);
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

   static int[] getRandomPosition(String[][] board){
      int[] randomPosition = new int[2];
      while(!board[randomPosition[0]][randomPosition[1]].equals(" ")){
         randomPosition[0] = RANDOM.nextInt(HEIGHT-2)+1;
         randomPosition[1] = RANDOM.nextInt(WIDTH-2)+1;
      }
      return randomPosition;
   }

   static void draw(String[][] board, String playerMark, int playerX, int playerY, String enemyMark, int enemyX, int enemyY, String powerUpMark, int powerUpX, int powerUpY, boolean powerUpOn){
      for(int row = 0; row<HEIGHT; row++){
         for(int col = 0; col<WIDTH; col++){ 
            if(row==playerX && col==playerY){
               System.out.print(playerMark);
            }else if(row==enemyX && col==enemyY){
               System.out.print(enemyMark);
            }else if(row==powerUpX && col==powerUpY && powerUpOn){
               System.out.print(powerUpMark);
            }else{
               System.out.print(board[row][col]);
            }   
         }
         System.out.println();
      }
   }

   static void addSomeDelay(long timeout, int iteration, boolean isPowerUpActive, int powerUpCounter) throws InterruptedException{
      if(isPowerUpActive){
         System.out.println("---"+"POWER-UP"+" ("+ (20-powerUpCounter) + ")---");
      }else{
         System.out.println("------"+iteration+"-------");
      }
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
   /*
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
   */

   static Direction changeDirectionTowards(String[][] board, Direction originalDirection, int aimX, int aimY, int chaserX, int chaserY){
      if(aimX < chaserX && board[chaserX-1][chaserY].equals(" ")){
         return Direction.UP;
      };
      if(aimX > chaserX && board[chaserX+1][chaserY].equals(" ")){
         return Direction.DOWN;
      };
      if(aimY < chaserY && board[chaserX][chaserY-1].equals(" ")){
         return Direction.LEFT;
      };
      if(aimY > chaserY && board[chaserX][chaserY+1].equals(" ")){
         return Direction.RIGHT;
      };
      return originalDirection;
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
