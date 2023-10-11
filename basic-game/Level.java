import java.util.Random;

public class Level {
    
    private final int height;
    private final int width;
    private String[][] level;
    private final Random RANDOM;

    public Level(Random random, int height, int width){
        this.RANDOM = random;
        this.height = height;
        this.width = width;
        this.level = new String[height][width];
        for(int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                if(row==0 || row==height-1 || col==0 || col==width-1){
                    level[row][col] = "X";
                }else{
                    level[row][col] = " ";
                }
            }
        }
    }

    public void setLevel(String[][] level){
        this.level = level;
    }
    public String[][] getLevel(){
        return this.level;
    }

    public void addRandomWall(int numberOfHorizontalWalls, int numberOfVerticalWalls){
        for(int i=0; i<numberOfHorizontalWalls; i++){
           addHorizontalWall();
        }
        for(int i=0; i<numberOfVerticalWalls; i++){
           addVerticalWall();
        }
     }

    private void addHorizontalWall(){
        int wallWidth = RANDOM.nextInt(width-3);
        int wallRow = RANDOM.nextInt(height-2)+1;
        int wallColumn = RANDOM.nextInt(width-2-wallWidth);
        for(int i=0; i<wallWidth; i++){
            level[wallRow][wallColumn+i] = "X";
        }
    }

    private void addVerticalWall(){
        int wallHeight = RANDOM.nextInt(height-3);
        int wallRow = RANDOM.nextInt(height-2-wallHeight);
        int wallColumn = RANDOM.nextInt(width-2)+1;
        for(int i=0; i<wallHeight; i++){
            level[wallRow+i][wallColumn] = "X";
        }
    }

    public boolean isPassable() {
        String[][] levelCopy = copy(level);
        outer: for(int row=0; row<height; row++){
           for(int col=0; col<width; col++){
              if(" ".equals(levelCopy[row][col])){
                 levelCopy[row][col] = "*";
                 break outer;
              }
           }
        }
        while(spreadAsterisks(levelCopy)){
        };
        for(int row=0; row<height; row++){
           for(int col=0; col<width; col++){
              if(" ".equals(levelCopy[row][col])){
                return false;
              }
           }
        }
        return true;
     }
  
     private String[][] copy(String[][] level){
        String[][] copy = new String[height][width];
        for(int row=0; row<height; row++){
           for(int col=0; col<width; col++){
              copy[row][col] = level[row][col];
           }
        }
        return copy;
     }
     
     private boolean spreadAsterisks(String[][] levelCopy){
        boolean changed = false;
        for(int row=0; row<height; row++){   
           for(int col=0; col<width; col++){
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

    private boolean spreadAsterisksWithCheck(String[][] levelCopy) {
        boolean[][] mask = new boolean[height][width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if ("*".equals(levelCopy[row][column])) {
                    mask[row][column] = true;
                }
            }
        }
        boolean changed = false;
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
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

    public boolean isEmpty(Coordinates coordinates){
        return " ".equals(level[coordinates.getRow()][coordinates.getColumn()]);
    }

    public Coordinates getFarthestCorner(Coordinates from) {
        String[][] levelCopy = copy(level); // pálya lemásolása
        levelCopy[from.getRow()][from.getColumn()] = "*"; // első csillag lehelyezése a célpontra
        int farthestRow = 0;
        int farthestColumn = 0;
        while (spreadAsterisksWithCheck(levelCopy)) {
            outer: for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    if (" ".equals(levelCopy[row][column])) {
                        farthestRow = row;
                        farthestColumn = column;
                        break outer;
                    }
                }
            }
        }
        return new Coordinates(farthestRow, farthestColumn);
     }
  
    public Direction getShortestPath(Direction defaultDirection, Coordinates from, Coordinates to) {
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

     public String getCell(Coordinates coordinates){
        return level[coordinates.getRow()][coordinates.getColumn()];
     }

}
