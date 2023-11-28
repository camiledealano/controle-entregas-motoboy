import apresentation.Menu;
import persistence.DatabaseConnection;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseConnection.getDatabase("entregasMotoboy");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        new Menu().exibir();


    }
}
