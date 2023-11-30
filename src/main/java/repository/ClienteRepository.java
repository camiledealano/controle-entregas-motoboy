package repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import persistence.DatabaseConnection;

import javax.swing.*;

public class ClienteRepository {
    private static final MongoCollection<Document> collection = DatabaseConnection.getDatabase("entregasMotoboy")
            .getCollection("Clientes");

    public static void crud() {
        boolean sair = false;

        while (!sair) {
            String[] options = { "Adicionar", "Editar", "Remover", "Consultar", "Voltar" };

            int opcao = JOptionPane.showOptionDialog(null, "Escolha uma opção para cliente: ",
                    "Menu Cliente", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (opcao) {
                case 0:
                    adicionar();
                    break;
                case 1:
                    String idEdicao = JOptionPane.showInputDialog("Edição de cliente \n\n Digite o id para editar:");
                    editar(idEdicao);
                    break;
                case 2:
                    String idExclusao = JOptionPane.showInputDialog("Remover cliente \n\n Digite o id para remover:");
                    remover(idExclusao);
                    break;
                case 3:
                    consultar();
                    break;
                case 4:
                    sair = true;
                    break;
            }
        }
    }

    private static void adicionar() {
        String nome = JOptionPane.showInputDialog("Cadastro de cliente \n\n Informe o nome:");
        String cpf = JOptionPane.showInputDialog("Cadastro de cliente \n\n Informe o cpf:");
        String telefone = JOptionPane.showInputDialog("Cadastro de cliente \n\n Informe o telefone:");
        String email = JOptionPane.showInputDialog("Cadastro de cliente \n\n Informe o e-mail:");

        Document clienteDocument = new Document()
                .append("nome", nome)
                .append("cpf", cpf)
                .append("telefone", telefone)
                .append("email", email);

        try {
            collection.insertOne(clienteDocument);
            JOptionPane.showMessageDialog(null, "Cliente inserido com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editar(String id) {
        if (isValidObjectId(id)){
        Bson filtro = Filters.eq("_id", new ObjectId(id));
        Document clienteExistente = collection.find(filtro).first();

        if (clienteExistente != null) {
            String novoNome = JOptionPane.showInputDialog("Informe o novo nome:");
            String novoCpf = JOptionPane.showInputDialog("Informe o novo CPF:");
            String novoTelefone = JOptionPane.showInputDialog("Informe o novo telefone:");
            String novoEmail = JOptionPane.showInputDialog("Informe o novo e-mail:");

            Document clienteAtualizado = new Document()
                    .append("nome", novoNome)
                    .append("cpf", novoCpf)
                    .append("telefone", novoTelefone)
                    .append("email", novoEmail);

            collection.replaceOne(filtro, clienteAtualizado);

            JOptionPane.showMessageDialog(null, "Cliente editado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Cliente não encontrado.");
        }  } else {
            JOptionPane.showMessageDialog(null, "ID inválido. Certifique-se de fornecer um Object ID válido.");
        }
    }


    private static void remover(String id) {
        if (isValidObjectId(id)){
        Bson filtro = Filters.eq("_id", new ObjectId(id));
        Document clienteExistente = collection.find(filtro).first();

        if (clienteExistente != null) {
            collection.deleteOne(filtro);
            JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Cliente não encontrado.");
        } } else {
            JOptionPane.showMessageDialog(null, "ID inválido. Certifique-se de fornecer um Object ID válido.");
        }
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

    private static void consultar() {
        FindIterable<Document> documentos = collection.find();

        for (Document documento : documentos) {
            String id = documento.getObjectId("_id").toString();
            String nome = documento.getString("nome");
            String cpf = documento.getString("cpf");
            String telefone = documento.getString("telefone");
            String email = documento.getString("email");

            System.out.println("Identificador: " + id);
            System.out.println("Nome: " + nome);
            System.out.println("CPF: " + cpf);
            System.out.println("Telefone: " + telefone);
            System.out.println("E-mail: " + email);
            System.out.println("------------------------------");
        }
    }
}

