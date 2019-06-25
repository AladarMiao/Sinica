package byog.Core;

import java.io.Serializable;

public class Monster implements Serializable {

    private int rand = World.getWorldRandom().nextInt(2);
    private String inside;
    private int health;
    private int damage;
    private int x;
    private int y;

    public Monster(int health, int damage, int x, int y) {
        this.damage = damage;
        this.health = health;
        this.x = x;
        this.y = y;
        generateMonsterLoot();
    }

    public int getMonsterX() {
        return this.x;
    }

    public void setMonsterX(int newX) {
        this.x = newX;
    }

    public int getMonsterY() {
        return this.y;
    }

    public void setMonsterY(int newY) {
        this.y = newY;
    }

    public void generateMonsterLoot() {
        if (rand == 0) {
            this.inside = "gold";
        }
        if (rand == 1) {
            this.inside = "potion";
        }
    }

    public String getInside() {
        return this.inside;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int h) {
        this.health = h;
    }

    public int getDamage() {
        return this.damage;
    }

}
