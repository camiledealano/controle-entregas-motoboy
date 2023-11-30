package repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import enuns.StatusEntrega;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import persistence.DatabaseConnection;

import javax.swing.*;

public class EntregaRepository {

    private static final MongoCollection<Document> collection = DatabaseConnection.getDatabase("entregasMotoboy")
            .getCollection("Entregas");

    public static void crud() {
        boolean sair = false;

        while (!sair) {
            String[] options = { "Editar", "Consultar", "Voltar" };

            int opcao = JOptionPane.showOptionDialog(null, "Escolha uma opção para entregas: ",
                    "Menu Entregas", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (opcao) {
                case 0 :
                    String idEdicao = JOptionPane.showInputDialog("Edição de entrega \n\n Digite o id para editar:");
                    editar(idEdicao);
                    break;
                case 1:
                    consultar();
                    break;
                case 2:
                    sair = true;
                    break;
            }
        }
    }

    private static void editar(String idEntrega) {
        if (isValidObjectId(idEntrega)) {
        if (entregaExiste(idEntrega)) {
            String novoIdCliente = JOptionPane.showInputDialog("Informe o novo identificador do cliente:");
            String novoIdMotoboy = JOptionPane.showInputDialog("Informe o novo identificador do motoboy:");
            String novoStatus = JOptionPane.showInputDialog("Informe o novo status da entrega (A ou E):");
            String novaDtEntrega = JOptionPane.showInputDialog("Informe a nova data de entrega:");
            String novoComentarioCliente = JOptionPane.showInputDialog("Informe o novo comentário do cliente:");

            Document entregaAtualizada = new Document()
                    .append("idCliente", novoIdCliente)
                    .append("idMotoboy", novoIdMotoboy)
                    .append("status", novoStatus)
                    .append("dtEntrega", novaDtEntrega)
                    .append("comentarioCliente", novoComentarioCliente);

            collection.replaceOne(getFiltro(idEntrega), entregaAtualizada);
            JOptionPane.showMessageDialog(null, "Entrega editada com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Entrega não encontrada.");
        }
        } else {
            JOptionPane.showMessageDialog(null, "ID inválido. Certifique-se de fornecer um Object ID válido.");
        }
    }

    private static void consultar() {
        FindIterable<Document> documentos = collection.find();

        for (Document documento : documentos) {
            String idDocumento = documento.getObjectId("_id").toString();
            String idCliente = documento.getString("idCliente");
            String idMotoboy = documento.getString("idMotoboy");
            String status = documento.getString("status");
            String dtEntrega = documento.getString("dtEntrega");
            String comentarioCliente = documento.getString("comentarioCliente");

            if (isValidObjectId(idDocumento)) {
                System.out.println("Identificador: " + idDocumento);
                System.out.println("Identificador do cliente: " + idCliente);
                System.out.println("Identificador do motoboy: " + idMotoboy);
                System.out.println("Status: " + status);
                System.out.println("Data da entrega: " + dtEntrega);
                System.out.println("Comentário do cliente: " + comentarioCliente);
                System.out.println("------------------------------");
            } else {
                JOptionPane.showMessageDialog(null, "ID inválido: " + idDocumento);
            }
        }
    }


    public static void criarEntrega(Document pedidoDocument) {
        String idCliente =  pedidoDocument.getString("idCliente");
        String idMotoboy = pedidoDocument.getString("idMotoboy");

        Document entregaDocument = new Document()
                .append("idCliente", idCliente)
                .append("idMotoboy", idMotoboy)
                .append("status", StatusEntrega.AGUARDANDO.toString())
                .append("comentarioCliente", null)
                .append("dtEntrega", null);

        try {
            collection.insertOne(entregaDocument);
            System.out.println("Registro de entrega criado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao criar registro de entrega: " + e.getMessage());
        }
    }

    private static boolean entregaExiste(String id) {
        Document pedido = collection.find(getFiltro(id)).first();
        return pedido != null;
    }

    private static Bson getFiltro(String id) {
        return  Filters.eq("_id", new ObjectId(id));
    }

    private static boolean isValidObjectId(String id) {
        if (id == null || id.length() != 24) {
            return false;
        }

        try {
            new ObjectId(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
