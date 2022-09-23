import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public interface Manager {
    HashMap<String, Room> getRooms();
    PriorityQueue<Room> getAvailableRooms();
    String assignRoom();
    void checkOutRoom(String number);
    void cleanRoom(String number);
    void setRepairRoom(String number);
    void RepairRoom(String number);
    ArrayList<String> ListAllAvailableRooms();
    void showAllRoomStatus();
}
