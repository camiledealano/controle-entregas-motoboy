import apresentation.Menu;
import persistence.DatabaseConnection;

public class Main {

    public static void main(String[] args) {
        //TODO: validar de outra forma quando pedir pro usuário digitar o id e ele digita algo errado ou que nao seja um objectId
        //TODO: testar todas as funcionalidades

        try {
            DatabaseConnection.getDatabase("entregasMotoboy");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        new Menu().exibir();
        System.exit(0);
    }
}
