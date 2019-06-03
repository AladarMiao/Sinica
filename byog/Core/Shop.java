package byog.Core;

import java.io.Serializable;

public class Shop implements Serializable {
    private int x;
    private int y;

    public Shop(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}

