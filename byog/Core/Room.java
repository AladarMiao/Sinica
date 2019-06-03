package byog.Core;

import java.io.Serializable;

public class Room implements Serializable {
    private int height;
    private int width;
    private int positionX;
    private int positionY;

    //Randomly Generate the room
    public Room(int maxW, int maxH) {
        height = World.getWorldRandom().nextInt(maxH - 3 + 1) + 3;
        width = World.getWorldRandom().nextInt(maxW - 3 + 1) + 3;
        positionX = 0;
        positionY = 0;
    }

    //Generates the room with parameters
    public Room(int h, int w, int x, int y) {
        this.height = h;
        this.width = w;
        this.positionX = x;
        this.positionY = y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return positionX;
    }

    public void setX(int x) {
        positionX = x;
    }

    public int getY() {
        return positionY;
    }

    public void setY(int y) {
        positionY = y;
    }
}
