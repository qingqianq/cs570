package Maze;

public class PairInt {
    private int x;
    private int y;
    public PairInt(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Object p){
        if(p instanceof PairInt){
            PairInt pairInt = (PairInt) p;
            return this.getX() == pairInt.getX() && this.getY() == pairInt.getY();
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + x +", " + y + ")";
    }

    public PairInt copy(){
        return new PairInt(x, y);
    }
}
