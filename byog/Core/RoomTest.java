package byog.Core;

import org.junit.Test;

public class RoomTest {

    @Test
    public void testRoomInitialize() {
        Room r1 = new Room(5, 5);
        System.out.println(r1.getHeight());
        System.out.println(r1.getWidth());
        Room r2 = new Room(5, 5);
        System.out.println(r2.getHeight());
        System.out.println(r2.getWidth());
    }
}
