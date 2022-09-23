public class Room {
    private String number;
    private RoomStatus status;

    protected Room(String number, RoomStatus status){
        this.number = number;
        this.status = status;
    }

    public String getNumber(){ return number; }
    public void setNumber(String number) { this.number = number; }

    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
}
