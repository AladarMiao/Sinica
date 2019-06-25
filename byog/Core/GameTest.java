package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import org.junit.Test;

public class GameTest {
    TERenderer ter = new TERenderer();

    @Test
    public void testConvertingStringToLong() {
        long s = Game.convertStringToLong("10102200");
        System.out.println(s);
    }

    @Test
    public void testSaveLoad() throws InterruptedException {
        ter.initialize(80, 30);
        Game g = new Game();
        int x = 1;
        TETile[][] te1 = g.playWithInputString("n1392967723524655428sddsaawwsaddw");

        TETile[][] te2 = g.playWithInputString("n1392967723524655428sddsaawws:q");
        te2 = g.playWithInputString("laddw");
        System.out.println(te1.equals(te2));
        while (x == 1) {
            ter.renderFrame(te1);
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
            ter.renderFrame(te2);
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
        }
    }
}
