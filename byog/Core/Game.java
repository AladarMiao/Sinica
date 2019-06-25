package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Random;

public class Game {

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    TERenderer ter = new TERenderer();

    public static long convertStringToLong(String s) {
        long l = 0;
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            l = l * 10;
            l += s.charAt(i) - '0';
        }
        return l;
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        String keyInput = "";
        // Game Settings
        String seedString = "";
        long seed = 0;
        int worldWidth = WIDTH;
        int worldHeight = HEIGHT;
        int maxRoomWidth = 8;
        int maxRoomHeight = 8;
        int numberOfRooms = 22;
        //gamestates
        boolean playing = true;
        boolean gameStarted = false;
        boolean inputtingSeed = false;

        //STD Draw initialization
        StdDraw.setCanvasSize(worldWidth, worldHeight);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.text(0.5, 0.5, "Hello");
        StdDraw.show();

        World world = new World(WIDTH, HEIGHT, maxRoomWidth, maxRoomHeight, numberOfRooms);
        TETile[][] tilesToRender = world.returnWorld();
        ter.initialize(WIDTH, HEIGHT);

        //game loop
        while (playing) {
            //process keys
            if (StdDraw.hasNextKeyTyped()) {
                Character key = StdDraw.nextKeyTyped();
                keyInput += key;
                if (Character.toUpperCase(key) == 'Q' && !gameStarted) {
                    //quit the game
                    playing = false;
                    StdDraw.clear();
                    System.exit(0);
                }
                if (Character.toUpperCase(key) == 'Q'
                        && keyInput.charAt(keyInput.length() - 2) == ':') {
                    playing = false;
                    if (gameStarted) {
                        keyInput = keyInput.substring(0, keyInput.length() - 2);
                        saveWorld(world);
                    }
                    System.exit(0);
                }
                if (!gameStarted && !inputtingSeed) {
                    if (Character.toUpperCase(key) == 'N') {
                        inputtingSeed = true;
                    } else if (key == 'l') {
                        world = loadWorld();
                        int playerX = world.getPlayer().getX();
                        int playerY = world.getPlayer().getY();
                        tilesToRender = world.renderTiles(playerX, playerY, 1, 17999);
                        gameStarted = true;
                    }
                } else if (inputtingSeed) {
                    if (Character.toUpperCase(key) == 'S') {
                        inputtingSeed = false;
                        gameStarted = true;
                        seed = convertStringToLong(seedString);
                        World.setWorldRandom(new Random(seed));
                        world = new World(worldWidth, worldHeight, maxRoomWidth,
                                maxRoomHeight, numberOfRooms);
                        int playerX = world.getPlayer().getX();
                        int playerY = world.getPlayer().getY();
                        tilesToRender = world.renderTiles(playerX, playerY, 1, 17999);
                    } else {
                        seedString += key;
                    }
                } else if (gameStarted) {
                    if (Character.toUpperCase(key) == 'S'
                            || Character.toUpperCase(key) == 'W'
                            || Character.toUpperCase(key) == 'A'
                            || Character.toUpperCase(key) == 'D') {
                        world.moveAvatar(key);
                        int playerX = world.getPlayer().getX();
                        int playerY = world.getPlayer().getY();
                        tilesToRender = world.renderTiles(playerX, playerY, 1, 17999);
                    }

                }
            }
            if (gameStarted) {
                if (world.getHasWon() || world.getPlayer().isDead()) {
                    playing = false;
                }
                ter.renderFrame(tilesToRender);
                drawHUD(world);
            } else if (inputtingSeed) {
                StdDraw.setPenColor(Color.WHITE);
                drawSeedInput(seedString);
            } else if (!gameStarted) {
                StdDraw.setPenColor(Color.WHITE);
                drawMenu();
            }
        }
        StdDraw.clear(Color.WHITE);
        showEndGameScreen(world);
    }

    //@Source saving and loading https://www.tutorialspoint.com/java/java_serialization.htm
    private void saveWorld(World w) {
        try {
            FileOutputStream fileOut = new FileOutputStream("/tmp/world.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(w);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /tmp/world.txt");
        } catch (IOException i) {
            System.out.println("IO Problem");
            i.printStackTrace();
        }
    }

    private World loadWorld() {
        World loadedWorld = null;
        try {
            FileInputStream fileIn = new FileInputStream("/tmp/world.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadedWorld = (World) in.readObject();
        } catch (FileNotFoundException f) {
            System.out.println("File Not Found");
            System.exit(0);
        } catch (IOException i) {
            System.out.println("InputOutput Problem");
            System.exit(0);
        } catch (ClassNotFoundException c) {
            System.out.println("Class Not Found");
            System.exit(0);
        }
        return loadedWorld;
    }

    private void drawMenu() {
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 3, "CS61B The Game");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 1, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Quit Game (Q)");
        StdDraw.show();
        StdDraw.pause(20);
        StdDraw.clear(Color.BLACK);
    }

    private void showEndGameScreen(World w) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(Math.floor(WIDTH / 2), Math.floor(HEIGHT / 2), w.getGameOverMessage());
        StdDraw.text(Math.floor(WIDTH / 2),
                Math.floor(HEIGHT / 2) - 1,
                "This game is brought you by Jack Jiang and Aladar Miao. If you like this game, " +
                        "feel free to support us through paypal!");
        StdDraw.text(Math.floor(WIDTH / 2), Math.floor(HEIGHT / 2) - 2, "Press (r) to restart");
        StdDraw.show();
        boolean choosing = true;
        while (choosing) {
            char key = 'o';
            if (StdDraw.hasNextKeyTyped()) {
                key = StdDraw.nextKeyTyped();
            }
            if (key == 'q') {
                choosing = false;
            }
            if (key == 'r') {
                playWithKeyboard();
                choosing = false;
            }
        }
    }

    private void drawWorld(World w) {
        ter.renderFrame(w.returnWorld());
    }

    public void drawHUD(World w) {

        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(1, HEIGHT - 1, "health: " + w.getPlayer().getHealth());
        StdDraw.textLeft(1, HEIGHT - 2, "gold: " + w.getPlayer().getGold());
        StdDraw.textLeft(1, HEIGHT - 3, "attack: " + w.getPlayer().getAttack());
        StdDraw.textLeft(1, HEIGHT - 4, w.getMessage());

        //Checks to see if mouse is inside the screen
        if ((StdDraw.mouseX() > 0 && StdDraw.mouseX() < w.getWidth())
                || (StdDraw.mouseY() > 0 && StdDraw.mouseY() < w.getHeight())) {
            int x = (int) Math.floor(StdDraw.mouseX());
            int y = (int) Math.floor(StdDraw.mouseY());
            TETile currentTile = w.returnWorld()[x][y];
            if (currentTile.description().equals(Tileset.MONSTER.description())) {
                Monster thisMonster = w.getMonsterAtPosition(x, y);
                StdDraw.textLeft(1, 1, "This is " + currentTile.description() + " with "
                        + Integer.toString(thisMonster.getHealth()) + " health and "
                        + Integer.toString(thisMonster.getDamage()) + " attack.");
            } else {
                StdDraw.textLeft(1, 1, "This is " + currentTile.description());
            }
        }

        StdDraw.show();
        StdDraw.pause(100);
        StdDraw.clear(Color.BLACK);
    }

    //:Q immediately saves and quits
    //quit and save and load to exact state
    private void writeObject(Game hue) {

    }

    private void drawSeedInput(String s) {
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Please Type Seed Below");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Seed: " + s);
        StdDraw.show();
        StdDraw.pause(20);
        StdDraw.clear(Color.BLACK);
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        // The settings for the world
        long seed = 0;
        String seedString = "";
        int worldWidth = WIDTH;
        int worldHeight = HEIGHT;
        int maxRoomWidth = 5;
        int maxRoomHeight = 5;
        int numberOfRooms = 15;
        String keyInput = input;

        World world = new World(0, 0, 0, 0, 1);
        for (int i = 0; i < input.length(); i++) {
            Character inputChar = Character.toUpperCase(input.charAt(i));
            if (inputChar == 'N') {
                //This gets the numbers after N and before S and stores it in seed
                for (int j = i + 1; j < input.length(); j++) {
                    if (Character.toUpperCase(input.charAt(j)) == 'S') {
                        break;
                    }
                    seedString += Character.toString((input.charAt(j)));
                }
                seed = convertStringToLong(seedString);
                i += seedString.length() + 1;
                World.setWorldRandom(new Random(seed));
                world = new World(worldWidth, worldHeight, maxRoomWidth,
                        maxRoomHeight, numberOfRooms);
            } else if (inputChar == 'L') {
                world = loadWorld();
            } else if (inputChar == ':' && Character.toUpperCase(input.charAt(i + 1)) == 'Q') {
                saveWorld(world);
                return world.returnWorld();
            } else if (inputChar == 'W') {
                world.moveAvatar('w');
            } else if (inputChar == 'S') {
                world.moveAvatar('s');
            } else if (inputChar == 'A') {
                world.moveAvatar('a');
            } else if (inputChar == 'D') {
                world.moveAvatar('d');

            }
        }
        TETile[][] finalWorldFrame = world.returnWorld();
        return finalWorldFrame;
    }
}
