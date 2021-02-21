package br.com.gmfonseca.mine.autominer.util;

public enum Direction {

    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0),
    NORTH_EAST(NORTH, EAST),
    NORTH_WEST(NORTH, WEST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),
    ;

    int x, z;

    Direction(int x, int z){
        this.x = x;
        this.z = z;
    }

    Direction(Direction dir1, Direction dir2){
        this.x = dir1.x + dir2.x;
        this.z = dir1.z + dir2.z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
