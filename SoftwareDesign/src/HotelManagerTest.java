import java.util.Random;

public class HotelManagerTest {
    public static void main(String[] args) {
        ManagerFactory factory = new ManagerFactory();
        Manager manager = factory.getManager("hotel");

        manager.showAllRoomStatus();
        manager.ListAllAvailableRooms();

        AssignRoom(manager);
        CheckoutRoom(manager);
        CleanRoom(manager);
        SetRoomRepair(manager);
        RepairRoom(manager);

        manager.ListAllAvailableRooms();


    }

    private static void AssignRoom(Manager m){
        System.out.println("Start assigning rooms...");
        Random random = new Random();
        int number = random.nextInt(m.getRooms().size());
        for(int i = 0; i<number; i++){
            System.out.println("Request "+i+", Room: "+m.assignRoom());
        }
    }

    private static void CheckoutRoom(Manager m){
        System.out.println("Start check out rooms...");
        Random random = new Random();
        int number = random.nextInt(m.getRooms().size());
        for(int i = 0; i<number; i++){
            int ranFloor = random.nextInt(5);
            int ranSuffix = random.nextInt(5);
            String n = ""+(ranFloor+1)+Suffix.values()[ranSuffix].name();
            System.out.println("Request "+i+", Room: "+n);
            m.checkOutRoom(n);
        }
        m.showAllRoomStatus();
    }

    private static void CleanRoom(Manager m){
        System.out.println("Start clean rooms...");
        Random random = new Random();
        int number = random.nextInt(m.getRooms().size());
        for(int i = 0; i<number; i++){
            int ranFloor = random.nextInt(5);
            int ranSuffix = random.nextInt(5);
            String n = ""+(ranFloor+1)+Suffix.values()[ranSuffix].name();
            System.out.println("Request "+i+", Room: "+n);
            m.cleanRoom(n);
        }
        m.showAllRoomStatus();
    }

    private static void SetRoomRepair(Manager m){
        System.out.println("Start setting rooms to repair...");
        Random random = new Random();
        int number = random.nextInt(m.getRooms().size());
        for(int i = 0; i<number; i++){
            int ranFloor = random.nextInt(5);
            int ranSuffix = random.nextInt(5);
            String n = ""+(ranFloor+1)+Suffix.values()[ranSuffix].name();
            System.out.println("Request "+i+", Room: "+n);
            m.setRepairRoom(n);
        }
        m.showAllRoomStatus();
    }


    private static void RepairRoom(Manager m){
        System.out.println("Start repairing rooms ...");
        Random random = new Random();
        int number = random.nextInt(m.getRooms().size());
        for(int i = 0; i<number; i++){
            int ranFloor = random.nextInt(5);
            int ranSuffix = random.nextInt(5);
            String n = ""+(ranFloor+1)+Suffix.values()[ranSuffix].name();
            System.out.println("Request "+i+", Room: "+n);
            m.RepairRoom(n);
        }
        m.showAllRoomStatus();
    }


}
