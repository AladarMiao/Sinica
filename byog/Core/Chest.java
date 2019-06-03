package byog.Core;

import java.io.Serializable;

public class Chest implements Serializable {
    private String interior;
    private int x;
    private int y;

    public Chest(int x, int y) {
        this.x = x;
        this.y = y;
        generateInterior();
    }

    public int getChestX() {
        return this.x;
    }

    public int getChestY() {
        return this.y;
    }

    public void generateInterior() {
        int rand = World.getWorldRandom().nextInt(3);

        if (rand == 0) {
            this.interior = "gold";
        }
        if (rand == 1) {
            this.interior = "potion";
        }
        if (rand == 2) {
            this.interior = "buff";
        }
    }

    public String getInterior() {
        return this.interior;
    }
}
