import java.util.Random;

public class BasicGame{
   
   static final int GAME_LOOP_NUMBER = 1000;
   static final int HEIGHT = 30;
   static final int WIDTH = 30;
   static final Random RANDOM = new Random();
   static String gameResult = "GAME OVER";

   public static void main(String[] args) throws InterruptedException{
     
      // pálya inicializálása
      Level level;
      do{
         level = new Level(RANDOM, HEIGHT, WIDTH);
         level.addRandomWall(2, 3);
      }while(!level.isPassable());
      
  
      // játékos inicializálása
      Coordinates playerCoordinates = getRandomStartingCoordinates(level);
      Entity player = new Entity("O", playerCoordinates, level.getFarthestCorner(playerCoordinates), Direction.RIGHT);
   
      // ellenfél inicializálása
      Coordinates enemyCoordinates = getRandomStartingCoordinatesAtLeastACertainDistanceFromGivenPoint(level, playerCoordinates, 10);
      Entity enemy = new Entity("-", enemyCoordinates, level.getFarthestCorner(enemyCoordinates), Direction.LEFT);
   
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
            player.setDirection(level.getShortestPath(player.getDirection(), playerCoordinates, enemyCoordinates));
         }else {
            if(isPowerUpOnBoard){
               player.setDirection(level.getShortestPath(player.getDirection(), playerCoordinates, enemyCoordinates));
            }
            else{
               if(iterationNumber%100==0){
                  player.setEscapeCoordinates(level.getFarthestCorner(playerCoordinates));
               }
               player.setDirection(level.getShortestPath(player.getDirection(), playerCoordinates, player.getEscapeCoordinates()));
            }
         }
         playerCoordinates = makeMove(player.getDirection(), level, playerCoordinates); //játékos léptetése

         // ellenfél irányválasztása
         if(isPowerUpActive){
            if(iterationNumber%100 == 0){
               enemy.setEscapeCoordinates(level.getFarthestCorner(enemyCoordinates));
            }
            enemy.setDirection(level.getShortestPath(enemy.getDirection(), enemyCoordinates, enemy.getEscapeCoordinates()));
         }else{
            enemy.setDirection(level.getShortestPath(enemy.getDirection(), enemyCoordinates, playerCoordinates));
         }
         // ellenfél léptetése (két körönként)
         if(iterationNumber%2==0){
            enemyCoordinates = makeMove(enemy.getDirection(), level, enemyCoordinates);
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
            player.setEscapeCoordinates(level.getFarthestCorner(playerCoordinates));
         }


         // játékos - power-up interakció
         if(isPowerUpOnBoard && playerCoordinates.isSameAs(powerUpCoordinates)){ //ha játékos rálép a power-up-ra
            isPowerUpActive = true;
            isPowerUpOnBoard = false;
            powerUpPresentCounter = 0;
            enemy.setEscapeCoordinates(level.getFarthestCorner(enemyCoordinates));
         }

         draw(level, player.getMarker(), playerCoordinates, enemy.getMarker(), enemyCoordinates, powerUpMarker, powerUpCoordinates, isPowerUpOnBoard); //kirajzolás
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

   static Coordinates getRandomStartingCoordinates(Level level) {
      Coordinates randomCoordinates;
      do {
         randomCoordinates = new Coordinates(RANDOM.nextInt(HEIGHT), RANDOM.nextInt(WIDTH));
      } while(!level.isEmpty(randomCoordinates));
      return randomCoordinates;
   }

   static Coordinates getRandomStartingCoordinatesAtLeastACertainDistanceFromGivenPoint(Level level, Coordinates playerStartingCoordinates, int distance) {
      Coordinates randomCoordinates;
      int counter = 0;
      do{
         randomCoordinates = getRandomStartingCoordinates(level);
      } while(counter++ < 1_000 && randomCoordinates.distanceFrom(playerStartingCoordinates)<distance);
      return randomCoordinates;
   }
   static void draw(Level level, String playerMark, Coordinates player, String enemyMark, Coordinates enemy, String powerUpMark, Coordinates powerUp, boolean powerUpOn){
      for(int row = 0; row<HEIGHT; row++){
         for(int col = 0; col<WIDTH; col++){ 
            Coordinates coordinatesToDraw = new Coordinates(row, col);
            if(coordinatesToDraw.isSameAs(player)){
               System.out.print(playerMark);
            }else if(coordinatesToDraw.isSameAs(enemy)){
               System.out.print(enemyMark);
            }else if(coordinatesToDraw.isSameAs(powerUp) && powerUpOn){
               System.out.print(powerUpMark);
            }else{
               System.out.print(level.getCell(coordinatesToDraw));
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

   static Coordinates makeMove(Direction direction, Level level, Coordinates oldCoords){
      Coordinates newCoords = new Coordinates(oldCoords.getRow(), oldCoords.getColumn());
      switch(direction){
         case UP:
            if(level.isEmpty(new Coordinates(oldCoords.getRow()-1, oldCoords.getColumn()))){
               newCoords.setRow(oldCoords.getRow()-1);
            }
            break;
         case DOWN:
            if(level.isEmpty(new Coordinates(oldCoords.getRow()+1, oldCoords.getColumn()))){
               newCoords.setRow(oldCoords.getRow()+1);
            }
            break;
         case LEFT:
            if(level.isEmpty(new Coordinates(oldCoords.getRow(), oldCoords.getColumn()-1))){
               newCoords.setColumn(oldCoords.getColumn()-1);
            }
            break;
         case RIGHT:
            if(level.isEmpty(new Coordinates(oldCoords.getRow(), oldCoords.getColumn()+1))){
               newCoords.setColumn(oldCoords.getColumn()+1);
            }
            break;
         }
         return newCoords;
   }

}
