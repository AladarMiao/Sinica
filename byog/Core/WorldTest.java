package byog.Core;

import byog.TileEngine.TERenderer;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;


public class WorldTest {
    TERenderer ter = new TERenderer();

    @Test
    public void testWorldGenerateRooms() {
        int roomWidth = 5;
        int roomHeight = 5;
        World w = new World(50, 50, roomWidth, roomHeight, 5);
        assertEquals(5, w.getNumberOfRooms());

        Room[] wRooms = w.getRooms();

        for (int i = 0; i < wRooms.length; i++) {
            System.out.print(wRooms[i].getHeight());
            System.out.println(wRooms[i].getWidth());
        }
    }

    @Test
    public void testWorldInitialize() throws InterruptedException {
        int worldWidth = 50;
        int worldHeight = 30;
        int roomWidth = 7;
        int roomHeight = 7;
        int rn = 1;
        ter.initialize(worldWidth, worldHeight);
        World w = new World(worldWidth, worldHeight, roomWidth, roomHeight, rn);
        while (true) {
            rn++;
            ter.renderFrame(w.returnWorld());
            w = new World(worldWidth, worldHeight, roomWidth, roomHeight, rn);
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    public void connectRoomTest() {
        int worldWidth = 50;
        int worldHeight = 30;
        int roomWidth = 5;
        int roomHeight = 5;
        ter.initialize(worldWidth, worldHeight);
        World w = new World(worldWidth, worldHeight, roomWidth, roomHeight, 2);
        w.connectRooms(w.getRooms()[0], w.getRooms()[1]);
        while (true) {
            ter.renderFrame(w.returnWorld());
        }
    }

    @Test
    public void renderTilesTest() throws InterruptedException {
        int worldWidth = 50;
        int worldHeight = 30;
        int roomWidth = 5;
        int roomHeight = 5;
        ter.initialize(worldWidth, worldHeight);
        World.setWorldRandom(new Random((long) 525));
        World w = new World(worldWidth, worldHeight, roomWidth, roomHeight, 2);
        while (true) {
            ter.renderFrame(w.returnWorld());
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
            ter.renderFrame(w.renderTiles(w.getPlayer().getX(), w.getPlayer().getY(), 1, 360));
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
        }
    }


    @Test
    public void roomOverlapTest() {
        int worldWidth = 50;
        int worldHeight = 30;
        int roomWidth = 5;
        int roomHeight = 5;
        World w = new World(worldWidth, worldHeight, roomWidth, roomHeight, 5);
        Room r01 = new Room(5, 5, 0, 0);
        Room r02 = new Room(5, 5, 0, 0);
        assertTrue(w.checkOverlap(r01, r02));
        Room r11 = new Room(5, 5, 0, 0);
        Room r12 = new Room(1, 1, 0, 0);
        assertTrue(w.checkOverlap(r11, r12));
        Room r21 = new Room(5, 5, 0, 0);
        Room r22 = new Room(4, 4, 4, 4);
        assertTrue(w.checkOverlap(r21, r22));
        Room r31 = new Room(5, 5, 0, 0);
        Room r32 = new Room(4, 4, 0, 4);
        assertTrue(w.checkOverlap(r31, r32));
        Room r41 = new Room(5, 5, 0, 0);
        Room r42 = new Room(4, 4, 6, 6);
        assertFalse(w.checkOverlap(r41, r42));
        Room r51 = new Room(5, 5, 0, 0);
        Room r52 = new Room(4, 4, 4, 0);
        assertTrue(w.checkOverlap(r51, r52));
        Room r61 = new Room(5, 5, 5, 5);
        Room r62 = new Room(4, 4, 4, 4);
        assertTrue(w.checkOverlap(r61, r62));
        Room r71 = new Room(5, 5, 5, 5);
        Room r72 = new Room(4, 4, 9, 4);
        assertTrue(w.checkOverlap(r71, r72));
    }
}
