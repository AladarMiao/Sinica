package byog.Core;


import java.io.Serializable;

public class Avatar implements Serializable {
    private int health;
    private int attack;
    private int gold;
    private boolean key;
    private int x;
    private int y;

    public Avatar(int x, int y, int health, int attack, int gold) {
        this.key = false;
        this.x = x;
        this.y = y;
        this.health = health;
        this.attack = attack;
        this.gold = gold;
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

    public int getAttack() {
        return this.attack;
    }

    public void setAttack(int a) {
        this.attack = a;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int h) {
        this.health = h;
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int g) {
        this.gold = g;
    }

    public boolean hasKey() {
        return this.key;
    }

    public void setKey(boolean k) {
        this.key = k;
    }

    public boolean isDead() {
        return this.health <= 0;
    }

}
