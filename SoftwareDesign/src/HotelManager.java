import java.util.*;

public class HotelManager implements Manager {
    private HashMap<String, Room> rooms;
    private PriorityQueue<Room> availableRooms;

    HotelManager(){
        availableRooms = new PriorityQueue<>(20, new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                String num1 = o1.getNumber();
                String num2 = o2.getNumber();
                if(num1.charAt(0)<num2.charAt(0)) return -1;
                else if(num1.charAt(0)==num2.charAt(0)){
                    if(num1.charAt(1)<num2.charAt(1)) return -1;
                    else return 1;
                }
                else return 1;
            }
        });

        rooms = new HashMap<>();

        for(int i = 0; i<5; i++){
            for(Suffix s : Suffix.values()){
                String number = String.valueOf(i+1)+s.name();
                Room newRoom = new Room(number, RoomStatus.available);
                availableRooms.add(newRoom);
                rooms.put(number, newRoom);
            }
        }
    }

    public HashMap<String, Room> getRooms() { return rooms; }
    public void setRooms(HashMap<String, Room> rooms) { this.rooms = rooms; }

    public PriorityQueue<Room> getAvailableRooms() { return availableRooms; }
    public void setAvailableRooms(PriorityQueue<Room> availableRooms) { this.availableRooms = availableRooms; }

    @Override
    public String assignRoom(){
        Room room = availableRooms.poll();
        room.setStatus(RoomStatus.occupied);
        return room.getNumber();
    }

    private boolean checkNumber(String number){
        if(!rooms.containsKey(number)){
            System.out.println("This room number is not in the hotel, please choose another number");
            return false;
        }
        return true;
    }

    @Override
    public void checkOutRoom(String number){
        if(!checkNumber(number)) return;
        Room room = rooms.get(number);
        if(room.getStatus()!=RoomStatus.occupied){
            System.out.println("This room is not occupied, can not check out");
            return;
        }
        room.setStatus(RoomStatus.vacant);
    }

    @Override
    public void cleanRoom(String number){
        if(!checkNumber(number)) return;
        Room room = rooms.get(number);
        if(room.getStatus()!=RoomStatus.vacant){
            System.out.println("This room is not vacant, can not clean");
            return;
        }
        room.setStatus(RoomStatus.available);
        availableRooms.add(room);
    }

    @Override
    public void setRepairRoom(String number){
        if(!checkNumber(number)) return;
        Room room = rooms.get(number);
        if(room.getStatus()!=RoomStatus.vacant){
            System.out.println("This room is not vacant, can not set to repair");
            return;
        }
        room.setStatus(RoomStatus.repair);
    }

    @Override
    public void RepairRoom(String number){
        if(!checkNumber(number)) return;
        Room room = rooms.get(number);
        if(room.getStatus()!=RoomStatus.repair){
            System.out.println("This room is not in repair, can not repair");
            return;
        }
        room.setStatus(RoomStatus.vacant);
    }



    @Override
    public ArrayList<String> ListAllAvailableRooms(){
        System.out.println("Below are the available rooms");
        ArrayList<String> res = new ArrayList<>();
        ArrayList<Room> temp = new ArrayList<>();
        temp.addAll(availableRooms);
        for(Room r : temp){
            res.add(r.getNumber());
            System.out.println(r.getNumber());
        }
        return res;
    }

    @Override
    public void showAllRoomStatus(){
        for(Map.Entry<String, Room> entry: rooms.entrySet()){
            System.out.print("Room: "+entry.getKey()+", Status: "+entry.getValue().getStatus().name());
            System.out.print("\n");
        }
    }
}
