import java.util.Random;

public class BasicGame{
   
   static final int GAME_LOOP_NUMBER = 1000;
   static final int HEIGHT = 30;
   static final int WIDTH = 30;
   static final Random RANDOM = new Random();
   static String gameResult = "GAME OVER";

   public static void main(String[] args) throws InterruptedException{
   
      // pálya inicializálása
      String[][] level = new String[HEIGHT][WIDTH];
      do{
         initLevel(level);
         addRandomWall(level, 2, 3);
      }while(!isPassable(level));
   
      // játékos inicializálása
      String playerMarker = "O";
      Coordinates playerCoordinates = getRandomStartingCoordinates(level);
      Coordinates playerEscapeCoordinates = getFarthestCorner(level, playerCoordinates);    
      Direction playerDirection = Direction.RIGHT;
   
      // ellenfél inicializálása
      String enemyMarker = "-";
      Coordinates enemyCoordinates = getRandomStartingCoordinatesAtLeastACertainDistanceFromGivenPoint(level, playerCoordinates, 10);
      Coordinates enemyEscapeCoordinates = getFarthestCorner(level, enemyCoordinates);    
      Direction enemyDirection = Direction.LEFT;
   
      // power-up inicializálása
      String powerUpMarker = "*";
      Coordinates powerUpCoordinates = getRandomStartingCoordinates(level);
      boolean isPowerUpOnBoard = false; //power-up nincs a pályán
      boolean isPowerUpActive = false; //játékos nem vette fel a power-up-ot
      int powerUpActiveCounter = 0; //hány kör óta vette fel a játékos a power-upot;
      int powerUpPresentCounter = 0;

      //-----------------GAME-LOOP-----------------
      for(int iterationNumber=1; iterationNumber<=GAME_LOOP_NUMBER; iterationNumber++){      
         // játékos irányválasztása
         if(isPowerUpActive){ //ha a power-up a pályán van, a játékos megpróbálja felvenni
            playerDirection = getShortestPath(level, playerDirection, playerCoordinates, enemyCoordinates);
         }else {
            if(isPowerUpOnBoard){
               playerDirection = getShortestPath(level, playerDirection, playerCoordinates, enemyCoordinates);
            }
            else{
               if(iterationNumber%100==0){
                  playerEscapeCoordinates = getFarthestCorner(level, playerCoordinates);
               }
               playerDirection = getShortestPath(level, playerDirection, playerCoordinates, playerEscapeCoordinates);
            }
         }
         playerCoordinates = makeMove(playerDirection, level, playerCoordinates); //játékos léptetése

         // ellenfél irányválasztása
         if(isPowerUpActive){
            if(iterationNumber%100 == 0){
               enemyEscapeCoordinates = getFarthestCorner(level, enemyCoordinates);
            }
            enemyDirection = getShortestPath(level, enemyDirection, enemyCoordinates, enemyEscapeCoordinates);
         }else{
            enemyDirection = getShortestPath(level, enemyDirection, enemyCoordinates, playerCoordinates);
         }
         // ellenfél léptetése (két körönként)
         if(iterationNumber%2==0){
            enemyCoordinates = makeMove(enemyDirection, level, enemyCoordinates);
         }

         // power-up frissítése
         if(isPowerUpActive){
            powerUpActiveCounter++;
         }else{
            powerUpPresentCounter++;
         }
         
         if(powerUpPresentCounter >= 60){
            if(isPowerUpOnBoard){
               powerUpCoordinates = getRandomStartingCoordinates(level);
            }
            isPowerUpOnBoard = !isPowerUpOnBoard;
            powerUpPresentCounter = 0;
         }
         if(powerUpPresentCounter <= 60){
            isPowerUpActive = false;
            powerUpActiveCounter = 0;
            powerUpCoordinates = getRandomStartingCoordinates(level);
            playerEscapeCoordinates = getFarthestCorner(level, playerCoordinates);
         }


         // játékos - power-up interakció
         if(isPowerUpOnBoard && playerCoordinates.isSameAs(powerUpCoordinates)){ //ha játékos rálép a power-up-ra
            isPowerUpActive = true;
            isPowerUpOnBoard = false;
            powerUpPresentCounter = 0;
            enemyEscapeCoordinates = getFarthestCorner(level, enemyCoordinates);
         }

         draw(level, playerMarker, playerCoordinates, enemyMarker, enemyCoordinates, powerUpMarker, powerUpCoordinates, isPowerUpOnBoard); //kirajzolás
         addSomeDelay(200L, iterationNumber, isPowerUpActive, powerUpActiveCounter); //késleltetés hozzáadása
     
         if(isPowerUpActive && playerCoordinates.isSameAs(enemyCoordinates)){//ha a játékos elkapja az ellenfelet
            gameResult += " - PLAYER WINS";
            break;
         }else if(playerCoordinates.isSameAs(enemyCoordinates)){//ha az ellenfél kapja el a játékost
            gameResult += " - ENEMY WINS";
            break;
         }

      }
      System.out.println(gameResult);
 }
 
 //--------------METHODS:

   static void initLevel(String[][] level){
      for(int row = 0; row<level.length; row++){
         for(int col = 0; col<level[row].length; col++){
            if(row==0 || row==HEIGHT-1 || col==0 || col==WIDTH-1){
               level[row][col] = "X";
            }else{
               level[row][col] = " ";         
            }
         }
      }
   }

   static void addRandomWall(String[][]level, int numberOfHorizontalWalls, int numberOfVerticalWalls){
      for(int i=0; i<numberOfHorizontalWalls; i++){
         addHorizontalWall(level);
      }
      for(int i=0; i<numberOfVerticalWalls; i++){
         addVerticalWall(level);
      }
   }

   static void addHorizontalWall(String[][] level){
      int wallWidth = RANDOM.nextInt(WIDTH-3);
      int wallRow = RANDOM.nextInt(HEIGHT-2)+1;
      int wallColumn = RANDOM.nextInt(WIDTH-2-wallWidth);
      for(int i=0; i<wallWidth; i++){
         level[wallRow][wallColumn+i] = "X";
      }
   }

   static void addVerticalWall(String[][] level){
      int wallHeight = RANDOM.nextInt(HEIGHT-3);
      int wallRow = RANDOM.nextInt(HEIGHT-2-wallHeight);
      int wallColumn = RANDOM.nextInt(WIDTH-2)+1;
      for(int i=0; i<wallHeight; i++){
         level[wallRow+i][wallColumn] = "X";
      }
   }

   static boolean isPassable(String[][] level) {
      String[][] levelCopy = copy(level);
      outer: for(int row=0; row<HEIGHT; row++){
         for(int col=0; col<WIDTH; col++){
            if(" ".equals(levelCopy[row][col])){
               levelCopy[row][col] = "*";
               break outer;
            }
         }
      }
      while(spreadAsterisks(levelCopy)){
      };
      for(int row=0; row<HEIGHT; row++){
         for(int col=0; col<WIDTH; col++){
            if(" ".equals(levelCopy[row][col])){
              return false;
            }
         }
      }
      return true;
   }

   static String[][] copy(String[][] level){
      String[][] copy = new String[HEIGHT][WIDTH];
      for(int row=0; row<HEIGHT; row++){
         for(int col=0; col<WIDTH; col++){
            copy[row][col] = level[row][col];
         }
      }
      return copy;
   }
   
   static boolean spreadAsterisks(String[][] level){
      boolean changed = false;
      for(int row=0; row<HEIGHT; row++){   
         for(int col=0; col<WIDTH; col++){
            if("*".equals(level[row][col])){
               if(" ".equals(level[row-1][col])){
                  level[row-1][col] = "*";
                  changed = true;
               }
               if(" ".equals(level[row+1][col])){
                  level[row+1][col] = "*";
                  changed = true;
               }
               if(" ".equals(level[row][col-1])){
                  level[row][col-1] = "*";
                  changed = true;
               }
               if(" ".equals(level[row][col+1])){
                  level[row][col+1] = "*";
                  changed = true;
               }
            }
         }
      }
      return changed;
   }

   static boolean spreadAsterisksWithCheck(String[][] levelCopy) {
      boolean[][] mask = new boolean[HEIGHT][WIDTH];
      for (int row = 0; row < HEIGHT; row++) {
          for (int column = 0; column < WIDTH; column++) {
              if ("*".equals(levelCopy[row][column])) {
                  mask[row][column] = true;
              }
          }
      }
      boolean changed = false;
      for (int row = 0; row < HEIGHT; row++) {
          for (int column = 0; column < WIDTH; column++) {
              if ("*".equals(levelCopy[row][column]) && mask[row][column]) {
                  if (" ".equals(levelCopy[row - 1][column])) {
                      levelCopy[row - 1][column] = "*";
                      changed = true;
                  }
                  if (" ".equals(levelCopy[row + 1][column])) {
                      levelCopy[row + 1][column] = "*";
                      changed = true;
                  }
                  if (" ".equals(levelCopy[row][column - 1])) {
                      levelCopy[row][column - 1] = "*";
                      changed = true;
                  }
                  if (" ".equals(levelCopy[row][column + 1])) {
                      levelCopy[row][column + 1] = "*";
                      changed = true;
                  }
              }
          }
      }
      return changed;
  }

   /*
   static int[] getRandomPosition(String[][] board){
      int[] randomPosition = new int[2];
      while(!board[randomPosition[0]][randomPosition[1]].equals(" ")){
         randomPosition[0] = RANDOM.nextInt(HEIGHT);
         randomPosition[1] = RANDOM.nextInt(WIDTH);
      }
      return randomPosition;
   }
   */

   static Coordinates getRandomStartingCoordinates(String[][] level) {
      int randomRow;
      int randomColumn;
      do {
          randomRow = RANDOM.nextInt(HEIGHT);
          randomColumn = RANDOM.nextInt(WIDTH);
      } while (!level[randomRow][randomColumn].equals(" "));
      Coordinates randomStartingCoordinates = new Coordinates();
      randomStartingCoordinates.setRow(randomRow);
      randomStartingCoordinates.setColumn(randomColumn);
      return randomStartingCoordinates;
   }

   static Coordinates getRandomStartingCoordinatesAtLeastACertainDistanceFromGivenPoint(String[][] level, Coordinates playerStartingCoordinates, int distance) {
      int playerStartingRow = playerStartingCoordinates.getRow();
      int playerStartingColumn = playerStartingCoordinates.getColumn();
      int randomRow;
      int randomColumn;
      int counter = 0;
      do {
         randomRow = RANDOM.nextInt(HEIGHT);
         randomColumn = RANDOM.nextInt(WIDTH);
      } while (counter++ < 1_000
            && (!level[randomRow][randomColumn].equals(" ") || calculateDistance(randomRow, randomColumn, playerStartingRow, playerStartingColumn) < distance));
      Coordinates randomStartingCoordinatesAtLeastACertainDistanceFromGivenPoint = new Coordinates();
      randomStartingCoordinatesAtLeastACertainDistanceFromGivenPoint.setRow(randomRow);
      randomStartingCoordinatesAtLeastACertainDistanceFromGivenPoint.setColumn(randomColumn);
      return randomStartingCoordinatesAtLeastACertainDistanceFromGivenPoint;
   }

   static int calculateDistance(int row1, int column1, int row2, int column2) {
      int rowDifference = Math.abs(row1 - row2);
      int columnDifference = Math.abs(column1 - column2);
      return rowDifference + columnDifference;
   }

   static Coordinates getFarthestCorner(String[][] level, Coordinates from) {
      String[][] levelCopy = copy(level); // pálya lemásolása
      levelCopy[from.getRow()][from.getColumn()] = "*"; // első csillag lehelyezése a célpontra
      int farthestRow = 0;
      int farthestColumn = 0;
      while (spreadAsterisksWithCheck(levelCopy)) {
          outer: for (int row = 0; row < HEIGHT; row++) {
              for (int column = 0; column < WIDTH; column++) {
                  if (" ".equals(levelCopy[row][column])) {
                      farthestRow = row;
                      farthestColumn = column;
                      break outer;
                  }
              }
          }
      }
      Coordinates farthestCorner = new Coordinates();
      farthestCorner.setRow(farthestRow);
      farthestCorner.setColumn(farthestColumn);
      return farthestCorner;
   }

   static Direction getShortestPath(String[][] level, Direction defaultDirection, Coordinates from, Coordinates to) {
      String[][] levelCopy = copy(level); // pálya lemásolása
      levelCopy[to.getRow()][to.getColumn()] = "*"; // első csillag lehelyezése a célpontra
      // *-ok terjesztése a szabad helyekre
      while (spreadAsterisksWithCheck(levelCopy)) {
          if ("*".equals(levelCopy[from.getRow() - 1][from.getColumn()])) {
              return Direction.UP;
          }
          if ("*".equals(levelCopy[from.getRow() + 1][from.getColumn()])) {
              return Direction.DOWN;
          }
          if ("*".equals(levelCopy[from.getRow()][from.getColumn() - 1])) {
              return Direction.LEFT;
          }
          if ("*".equals(levelCopy[from.getRow()][from.getColumn() + 1])) {
              return Direction.RIGHT;
          }
      }
      return defaultDirection;
   }

   static void draw(String[][] level, String playerMark, Coordinates player, String enemyMark, Coordinates enemy, String powerUpMark, Coordinates powerUp, boolean powerUpOn){
      for(int row = 0; row<HEIGHT; row++){
         for(int col = 0; col<WIDTH; col++){ 
            if(row==player.getRow() && col==player.getColumn()){
               System.out.print(playerMark);
            }else if(row==enemy.getRow() && col==enemy.getColumn()){
               System.out.print(enemyMark);
            }else if(row==powerUp.getRow() && col==powerUp.getColumn() && powerUpOn){
               System.out.print(powerUpMark);
            }else{
               System.out.print(level[row][col]);
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

   static Coordinates makeMove(Direction direction, String[][] level, Coordinates oldCoords){
      Coordinates newCoords = new Coordinates();
      newCoords.setRow(oldCoords.getRow());
      newCoords.setColumn(oldCoords.getColumn());
      switch(direction){
         case UP:
            if(level[oldCoords.getRow()-1][oldCoords.getColumn()].equals(" ")){
               newCoords.setRow(oldCoords.getRow()-1);
            }
            break;
         case DOWN:
            if(level[oldCoords.getRow()+1][oldCoords.getColumn()].equals(" ")){
               newCoords.setRow(oldCoords.getRow()+1);
            }
            break;
         case LEFT:
            if(level[oldCoords.getRow()][oldCoords.getColumn()-1].equals(" ")){
               newCoords.setColumn(oldCoords.getColumn()-1);
            }
            break;
         case RIGHT:
            if(level[oldCoords.getRow()][oldCoords.getColumn()+1].equals(" ")){
               newCoords.setColumn(oldCoords.getColumn()+1);
            }
            break;
         }
         return newCoords;
   }

}
