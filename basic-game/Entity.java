public class Entity {
    
    private String marker;
    private Coordinates coordinates;
    private Coordinates escapeCoordinates;
    private Direction direction;

    public Entity(String marker, Coordinates coords, Coordinates escape, Direction direction){
        this.marker = marker;
        this.coordinates = coords;
        this.escapeCoordinates = escape;
        this.direction = direction;
    }

    public void setMarker(String marker){
        this.marker = marker;
    }
    public String getMarker(){
        return this.marker;
    }

    public void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }
    public Coordinates getCoordinates(){
        return this.coordinates;
    }

    public void setEscapeCoordinates(Coordinates escapeCoordinates){
        this.escapeCoordinates = escapeCoordinates;
    }
    public Coordinates getEscapeCoordinates(){
        return this.escapeCoordinates;
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }
    public Direction getDirection(){
        return this.direction;
    }
}
