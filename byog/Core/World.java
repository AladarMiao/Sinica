package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class World implements Serializable {
    private static Random random = new Random();
    ArrayList<Monster> allMonsters;
    Chest[] allChests;
    TETile[][] worldTiles;
    private boolean hasWon;
    private int width;
    private int height;
    private int numberOfRooms;
    private Room[] rooms;
    private Avatar player;
    private Monster monster;
    private Chest chest;
    private Shop shop;
    private Shop[] shops = new Shop[2];
    private String messageToDisplay;
    private String gameOverMessage;

    public World(int worldW, int worldH, int roomW, int roomH, int numOfRoom) {
        this.messageToDisplay = "";
        this.width = worldW;
        this.height = worldH;
        numberOfRooms = numOfRoom;
        worldTiles = new TETile[width][height];
        hasWon = false;
        messageToDisplay = "";
        generateRooms(roomW, roomH);
        initializeWorld();
        putRoomsInWorld();
        connectAllRooms();
        wallOffWorld();
        generateLockedDoor();
        generateAvatar();
        spawnMonsters();
        spawnChest();

        spawnShop();

    }

    public static Random getWorldRandom() {
        return random;
    }

    public static void setWorldRandom(Random r) {
        random = r;
    }

    public void goToShop() {
        if (player.getGold() < 300) {
            messageToDisplay = "You don't have enough money for my keys kid. A key costs 300. Hue.";
        } else {
            player.setKey(true);
            player.setGold(player.getGold() - 300);
            messageToDisplay = "You bought a key from Aladar the merchant!";
        }
    }

    public String getMessage() {
        return messageToDisplay;
    }

    public void spawnShop() {
        int countShop = 0;
        while (countShop != 2) {
            int x = World.getWorldRandom().nextInt(width);
            int y = World.getWorldRandom().nextInt(height);
            if (!checkFloor(x, y, Tileset.WALL)) {
                if (worldTiles[x][y] == Tileset.FLOOR) {
                    worldTiles[x][y] = Tileset.SHOP;
                    shop = new Shop(x, y);
                    shops[countShop] = shop;
                    countShop += 1;
                }
            }
        }
    }

    //opens chest
    public void openChest(Chest obj) {
        String inside = obj.getInterior();
        worldTiles[obj.getChestX()][obj.getChestY()] = Tileset.FLOOR;
        if (inside.equals("gold")) {
            player.setGold(player.getGold() + 50);
            messageToDisplay = "You found 50 gold";
        } else if (inside.equals("buff")) {
            player.setAttack(player.getAttack() + 1);
            messageToDisplay = "You found an attack buff! +1 attack buff";
        } else if (inside.equals("potion")) {
            player.setHealth(player.getHealth() + 2);
            messageToDisplay = "You found a potion: +2 health";
        }
    }

    //fights monster
    public void fightMonster(Monster obj) {
        int startHealth = player.getHealth();
        while (player.getHealth() > 0 && obj.getHealth() > 0) {
            player.setHealth(player.getHealth() - obj.getDamage());
            obj.setHealth(obj.getHealth() - player.getAttack());
        }
        int endHealth = player.getHealth();
        if (player.getHealth() <= 0) {
            gameOverMessage = "You have been slain by a monster.";
            return;
        }
        removeMonsterAtPosition(obj.getMonsterX(), obj.getMonsterY());
        worldTiles[obj.getMonsterX()][obj.getMonsterY()] = Tileset.FLOOR;
        String helper = obj.getInside();
        if (helper.equals("gold")) {
            player.setGold(player.getGold() + 50);
            messageToDisplay = "You killed a monster and found 50 gold but lost "
                    + Integer.toString(startHealth - endHealth) + " health";
        } else if (helper.equals("potion")) {
            player.setAttack(player.getAttack() + 1);
            messageToDisplay = "You killed a monster and got +1 attack but lost "
                    + Integer.toString(startHealth - endHealth) + " health";
        }
    }

    public void spawnChest() {
        allChests = new Chest[numberOfRooms];
        int chestCount = 0;
        for (Room room : rooms) {
            int chestX = random.nextInt(room.getWidth()) + room.getX();
            int chestY = random.nextInt(room.getHeight()) + room.getY();
            while (worldTiles[chestX][chestY] != Tileset.CHEST) {
                chestX = random.nextInt(room.getWidth()) + room.getX();
                chestY = random.nextInt(room.getHeight()) + room.getY();
                if (worldTiles[chestX][chestY] == Tileset.FLOOR) {
                    chest = new Chest(chestX, chestY);
                    worldTiles[chestX][chestY] = Tileset.CHEST;
                    allChests[chestCount] = chest;
                    chestCount++;
                    break;
                }
            }
        }
    }

    public void spawnMonsters() {
        allMonsters = new ArrayList<>(rooms.length);
        int monsterCount = 0;
        for (Room room : rooms) {
            if (World.getWorldRandom().nextInt(2) == 0) {
                int monsterX = random.nextInt(room.getWidth()) + room.getX();
                int monsterY = random.nextInt(room.getHeight()) + room.getY();
                while (worldTiles[monsterX][monsterY] != Tileset.MONSTER) {
                    monsterX = random.nextInt(room.getWidth()) + room.getX();
                    monsterY = random.nextInt(room.getHeight()) + room.getY();
                    if (worldTiles[monsterX][monsterY] == Tileset.FLOOR) {
                        monster = new Monster(World.getWorldRandom().nextInt(10) + 1,
                                World.getWorldRandom().nextInt(2) + 1, monsterX, monsterY);
                        worldTiles[monsterX][monsterY] = Tileset.MONSTER;
                        allMonsters.add(monster);
                        monsterCount++;
                        break;
                    }
                }
            }
        }
    }

    public void moveMonsters() {
        for (int i = 0; i < allMonsters.size(); i++) {
            int x = allMonsters.get(i).getMonsterX();
            int y = allMonsters.get(i).getMonsterY();
            int moveX = 0;
            int moveY = 0;
            if (random.nextInt(2) == 0) {
                if (random.nextInt(2) == 0) {
                    moveX = 1;
                } else {
                    moveX = -1;
                }
            } else {
                if (random.nextInt(2) == 0) {
                    moveY = 1;
                } else {
                    moveY = -1;
                }
            }
            if (!isCollider(worldTiles[x + moveX][y + moveY])) {
                allMonsters.get(i).setMonsterX(x + moveX);
                allMonsters.get(i).setMonsterY(y + moveY);
                worldTiles[x + moveX][y + moveY] = Tileset.MONSTER;
                worldTiles[x][y] = Tileset.FLOOR;
            }
            if (worldTiles[x + moveX][y + moveY].description().equals(Tileset.PLAYER.description())) {
                fightMonster(allMonsters.get(i));


            }
        }
    }

    public void generateAvatar() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (worldTiles[x][y] == Tileset.LOCKED_DOOR) {
                    worldTiles[x + 1][y] = Tileset.PLAYER;
                    player = new Avatar(x + 1, y, 5, 1, 50);
                }
            }
        }
    }

    private boolean isCollider(TETile tile) {
        if (tile.description().equals(Tileset.WALL.description())
                || tile.description().equals(Tileset.LOCKED_DOOR.description())
                || tile.description().equals(Tileset.MONSTER.description())
                || tile.description().equals(Tileset.CHEST.description())
                || tile.description().equals(Tileset.NOTHING.description())
                || tile.description().equals(Tileset.SHOP.description())
                || tile.description().equals(Tileset.PLAYER.description())) {
            return true;
        }
        return false;
    }

    private boolean isVisionBlocker(TETile tile) {
        if (tile.description().equals(Tileset.WALL.description())
                || tile.description().equals(Tileset.LOCKED_DOOR.description())
                || tile.description().equals(Tileset.NOTHING.description())) {
            return true;
        }
        return false;
    }

    //Moves the avatar in the world depending on key
    public void moveAvatar(Character key) {
        int x = player.getX();
        int y = player.getY();
        int moveX = x;
        int moveY = y;
        if (Character.toUpperCase(key) == 'W') {
            moveY += 1;
        } else if (Character.toUpperCase(key) == 'S') {
            moveY -= 1;
        } else if (Character.toUpperCase(key) == 'A') {
            moveX -= 1;
        } else if (Character.toUpperCase(key) == 'D') {
            moveX += 1;
        }
        if (worldTiles[moveX][moveY].description().equals(Tileset.CHEST.description())) {
            for (int i = 0; i < allChests.length; i++) {
                if (allChests[i].getChestX() == moveX && allChests[i].getChestY() == moveY) {
                    openChest(allChests[i]);
                }
            }
        }
        if (worldTiles[moveX][moveY].description().equals(Tileset.MONSTER.description())) {
            for (int i = 0; i < allMonsters.size(); i++) {
                if (allMonsters.get(i).getMonsterX() == moveX
                        && allMonsters.get(i).getMonsterY() == moveY) {
                    fightMonster(allMonsters.get(i));
                }
            }
        }
        if (worldTiles[moveX][moveY].description().equals(Tileset.SHOP.description())) {
            for (int i = 0; i < 2; i++) {
                if (shops[i].getX() == moveX && shops[i].getY() == moveY) {
                    goToShop();
                }
            }
        }
        if (worldTiles[moveX][moveY].description().equals(Tileset.LOCKED_DOOR.description())) {
            if (player.hasKey()) {
                gameOverMessage = "You won! Good job.";
                hasWon = true;
            }
        }
        if (!isCollider(worldTiles[moveX][moveY])) {
            player.setX(moveX);
            player.setY(moveY);
            worldTiles[moveX][moveY] = Tileset.PLAYER;
            worldTiles[x][y] = Tileset.FLOOR;
            moveMonsters();
        }
    }


    //Returns the Tileset of the world
    public TETile[][] returnWorld() {
        return worldTiles;
    }

    public Monster getMonsterAtPosition(int x, int y) {
        for (int i = 0; i < allMonsters.size(); i++) {
            if (allMonsters.get(i).getMonsterX() == x && allMonsters.get(i).getMonsterY() == y) {
                return allMonsters.get(i);
            }
        }
        return allMonsters.get(0);
    }

    public void removeMonsterAtPosition(int x, int y) {
        for (int i = 0; i < allMonsters.size(); i++) {
            if (allMonsters.get(i).getMonsterX() == x && allMonsters.get(i).getMonsterY() == y) {
                allMonsters.remove(i);
            }
        }
    }

    //Creates the random rooms
    public void generateRooms(int maxW, int maxH) {
        Room[] rms = new Room[numberOfRooms];
        for (int i = 0; i < numberOfRooms; i++) {
            rms[i] = new Room(maxW, maxH);
        }
        this.rooms = rms;
    }


    //Creates a world of width by height of empty tiles
    private void initializeWorld() {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                worldTiles[w][h] = Tileset.NOTHING;
            }
        }
    }

    //Randomly picks the x and y position of the room
    private void putRoomsInWorld() {
        ArrayList<Room> otherRooms = new ArrayList<>();
        int buffer = 2; // buffer from the edge of the screen
        for (Room r : rooms) {
            otherRooms.add(r);
            r.setX(random.nextInt(width - r.getWidth() - 2 * buffer) + buffer);
            r.setY(random.nextInt(height - r.getHeight() - 2 * buffer) + buffer);
            boolean overlapped;
            int maxAttempts = 100000;
            int c = 0;
            do {
                overlapped = false;
                for (Room otherRoom : otherRooms) {
                    if (checkOverlap(r, otherRoom) && r != otherRoom) {
                        overlapped = true;
                    }
                }
                if (overlapped) {
                    r.setX(random.nextInt(width - r.getWidth() - 2 * buffer) + buffer);
                    r.setY(random.nextInt(height - r.getHeight() - 2 * buffer) + buffer);
                }
                if (c > maxAttempts) {
                    System.out.println("max attempts reached, breaking out of loop");
                    break;
                }
                c++;
            } while (overlapped);

            tileRoom(r);
        }
    }

    //walls off rooms and hallways
    public void wallOffWorld() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (worldTiles[x][y] == Tileset.NOTHING && checkFloor(x, y, Tileset.FLOOR)) {
                    worldTiles[x][y] = Tileset.WALL;
                }
            }
        }
    }

    //create locked door
    public void generateLockedDoor() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (worldTiles[x][y] == Tileset.WALL && (worldTiles[x + 1][y] == Tileset.FLOOR
                        || worldTiles[x - 1][y] == Tileset.FLOOR
                        || worldTiles[x][y + 1] == Tileset.FLOOR
                        || worldTiles[x][y - 1] == Tileset.FLOOR)) {
                    worldTiles[x][y] = Tileset.LOCKED_DOOR;
                    return;
                }
            }
        }
    }


    //checks if there is a certain tile around a position
    private boolean checkFloor(int x, int y, TETile tile) {
        if (x == width - 1 || x == 0) {
            return false;
        }
        if (y == height - 1 || y == 0) {
            return false;
        }
        if (worldTiles[x + 1][y] == tile
                || worldTiles[x - 1][y] == tile
                || worldTiles[x][y + 1] == tile
                || worldTiles[x][y - 1] == tile
                || worldTiles[x + 1][y + 1] == tile
                || worldTiles[x - 1][y + 1] == tile
                || worldTiles[x + 1][y - 1] == tile
                || worldTiles[x - 1][y - 1] == tile) {
            return true;
        }
        return false;
    }

    //creates the tiles in the room
    public void tileRoom(Room r) {
        int rX = r.getX();
        int rY = r.getY();
        for (int x = rX; x < r.getWidth() + rX; x++) {
            for (int y = rY; y < r.getHeight() + rY; y++) {
                worldTiles[x][y] = Tileset.FLOOR;
            }
        }
    }

    //Checks to see if two rooms overlap returns true if it does overlap
    public boolean checkOverlap(Room r1, Room r2) {
        //4 corners of room 1
        int[] r1TopLeftPoint = new int[]{r1.getX(), r1.getY()};
        int[] r1TopRightPoint = new int[]{r1.getX() + r1.getWidth(), r1.getY()};
        int[] r1BottomLeftPoint = new int[]{r1.getX(), r1.getY() + r1.getHeight()};
        int[] r1BottomRightPoint = new int[]{r1.getX() + r1.getWidth(), r1.getY() + r1.getHeight()};
        //4 corners of room 2
        int[] r2TopLeftPoint = new int[]{r2.getX(), r2.getY()};
        int[] r2TopRightPoint = new int[]{r2.getX() + r2.getWidth(), r2.getY()};
        int[] r2BottomLeftPoint = new int[]{r2.getX(), r2.getY() + r2.getHeight()};
        int[] r2BottomRightPoint = new int[]{r2.getX() + r2.getWidth(), r2.getY() + r2.getHeight()};

        if ((r2TopLeftPoint[0] >= r1TopLeftPoint[0]
                && r2TopLeftPoint[0] <= r1TopRightPoint[0]
                && r2TopLeftPoint[1] >= r1TopLeftPoint[1]
                && r2TopLeftPoint[1] <= r1BottomLeftPoint[1])
                || (r2TopRightPoint[0] >= r1TopLeftPoint[0]
                && r2TopRightPoint[0] <= r1TopRightPoint[0]
                && r2TopRightPoint[1] >= r1TopLeftPoint[1]
                && r2TopRightPoint[1] <= r1BottomLeftPoint[1])
                || (r2BottomLeftPoint[0] >= r1TopLeftPoint[0]
                && r2BottomLeftPoint[0] <= r1TopRightPoint[0]
                && r2BottomLeftPoint[1] >= r1TopLeftPoint[1]
                && r2BottomLeftPoint[1] <= r1BottomLeftPoint[1])
                || (r2BottomRightPoint[0] >= r1TopLeftPoint[0]
                && r2BottomRightPoint[0] <= r1TopRightPoint[0]
                && r2BottomRightPoint[1] >= r1TopLeftPoint[1]
                && r2BottomRightPoint[1] <= r1BottomLeftPoint[1])) {
            return true;
        }
        return false;
    }

    //Connects every room to the nearest room with hallways
    public void connectAllRooms() {
        ArrayList<Room> otherRooms = new ArrayList<>();
        for (Room r : rooms) {
            otherRooms.add(r);
        }
        for (int i = rooms.length - 1; i > 0; i--) {
            connectRooms(rooms[i], findNearestRoom(rooms[i], otherRooms));
            otherRooms.remove(rooms[i]);
        }
    }

    //Finds the nearest room to room in a list of rooms
    public Room findNearestRoom(Room room, ArrayList<Room> rms) {
        if (rms.size() < 1) {
            System.out.println("rooms must be more than 1");
            return null;
        }
        double shortestDistance = 0;
        Room roomToReturn = room;
        for (Room r : rms) {
            if (r != room) {
                shortestDistance = getDistance(r, room);
                break;
            }
        }
        for (Room r : rms) {
            if (r != room) {
                double distance = getDistance(room, r);
                if (distance <= shortestDistance) {
                    shortestDistance = getDistance(room, r);
                    roomToReturn = r;
                }
            }
        }
        return roomToReturn;
    }

    //gets the distance between two rooms
    public double getDistance(Room r1, Room r2) {
        double x2 = Math.pow(r1.getX() - r2.getX(), 2);
        double y2 = Math.pow(r1.getY() - r2.getY(), 2);
        double result = Math.pow((x2 + y2), 0.5);
        return result;
    }

    //connects two given rooms with a hallway
    public void connectRooms(Room r1, Room r2) {
        int[] point;
        int r1RandomX = r1.getX() + random.nextInt(r1.getWidth());
        int r2RandomX = r2.getX() + random.nextInt(r2.getWidth());
        int r1RandomY = r1.getY() + random.nextInt(r1.getHeight());
        int r2RandomY = r2.getY() + random.nextInt(r2.getHeight());
        //There will be a 50% chance of selecting which interescting to use
        if (random.nextInt(2) == 1) {
            point = new int[]{r1RandomX, r2RandomY};
        } else {
            point = new int[]{r2RandomX, r1RandomY};
        }
        createLine(point[0], point[1], r1RandomX - point[0], r2RandomY - point[1], Tileset.FLOOR);
        createLine(point[0], point[1], r2RandomX - point[0], r1RandomY - point[1], Tileset.FLOOR);
    }

    //connects between two points (only works with a straight line) with tile
    private void createLine(int x1, int y1, int w, int h, TETile t) {
        if (w > 0) {
            for (int x = x1; x < x1 + w; x++) {
                worldTiles[x][y1] = t;
            }
        } else {
            for (int x = x1; x > x1 + w; x--) {
                worldTiles[x][y1] = t;
            }
        }
        if (h > 0) {
            for (int y = y1; y < y1 + h; y++) {
                worldTiles[x1][y] = t;
            }
        } else {
            for (int y = y1; y > y1 + h; y--) {
                worldTiles[x1][y] = t;
            }
        }

    }

    //Returns the tiles that is in player's line of vision
    public TETile[][] renderTiles(double x1, double y1, double k, double lines) {
        double degreePerLine = 360.0 / lines;
        TETile[][] tilesToRender = new TETile[width][height];
        tilesToRender[(int) x1][(int) y1] = worldTiles[(int) x1][(int) y1];
        for (int line = 0; line < lines; line++) {
            double x = x1 + Math.cos(Math.toRadians((double) line * degreePerLine)) * k;
            double y = y1 + Math.sin(Math.toRadians((double) line * degreePerLine)) * k;
            int tileX = (int) Math.ceil(x);
            int tileY = (int) Math.ceil(y);
            int maxAttempts = 10000;
            tilesToRender[tileX][tileY] = worldTiles[tileX][tileY];
            if (!isVisionBlocker(worldTiles[tileX][tileY])) {
                while (true) {
                    x += Math.cos((double) line * degreePerLine) * k;
                    y += Math.sin((double) line * degreePerLine) * k;
                    tileX = (int) Math.ceil(x);
                    tileY = (int) Math.ceil(y);
                    tilesToRender[tileX][tileY] = worldTiles[tileX][tileY];
                    if (isVisionBlocker(worldTiles[tileX][tileY])) {
                        break;
                    }
                    maxAttempts -= 1;
                    if (maxAttempts < 0) {
                        break;
                    }
                }
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tilesToRender[x][y] == null) {
                    tilesToRender[x][y] = Tileset.NOTHING;
                }
            }
        }
        return tilesToRender;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public Avatar getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getHasWon() {
        return hasWon;
    }

    public String getGameOverMessage() {
        return gameOverMessage;
    }

}
