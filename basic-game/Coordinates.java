public class Coordinates {

    private int row;
    private int column;

    public int getRow(){
        return this.row;
    }
    public void setRow(int row){
        this.row = row;
    }

    public int getColumn(){
        return this.column;
    }
    public void setColumn(int column){
        this.column = column;
    }

    public boolean isSameAs(Coordinates other){
        return this.row==other.row && this.column==other.column;
    }

}