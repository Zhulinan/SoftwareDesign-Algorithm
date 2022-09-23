public class ManagerFactory {
    public Manager getManager(String name){
        return new HotelManager();
    }
}
