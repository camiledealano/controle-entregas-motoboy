import apresentation.Menu;
import persistence.DatabaseConnection;

public class Main {

    public static void main(String[] args) {
        //TODO: testar TODAS as funcionalidades

        try {
            DatabaseConnection.getDatabase("entregasMotoboy");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        new Menu().exibir();
        System.exit(0);
    }
}
