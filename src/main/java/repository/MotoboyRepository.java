package repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import persistence.DatabaseConnection;

import javax.swing.*;

public class MotoboyRepository {
    private static final MongoCollection<Document> collection = DatabaseConnection.getDatabase("entregasMotoboy")
            .getCollection("Motoboys");

    public static void crud() {
        boolean sair = false;

        while (!sair) {
            String[] options = {"Adicionar", "Editar", "Remover", "Consultar", "Voltar"};

            int opcao = JOptionPane.showOptionDialog(null, "Escolha uma opção para motoboy: ",
                    "Menu Motoboy", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (opcao) {
                case 0:
                    adicionar();
                    break;
                case 1:
                    String idEdicao = JOptionPane.showInputDialog("Edição de motoboy \n\n Digite o id para editar:");
                    editar(idEdicao);
                    break;
                case 2:
                    String idExclusao = JOptionPane.showInputDialog("Remover motoboy \n\n Digite o id para remover:");
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
        String nome = JOptionPane.showInputDialog("Cadastro de motoboy \n\n Informe o nome:");
        String cpf = JOptionPane.showInputDialog("Cadastro de motoboy \n\n Informe o cpf:");
        String telefone = JOptionPane.showInputDialog("Cadastro de motoboy \n\n Informe o telefone:");
        String placaMoto = JOptionPane.showInputDialog("Cadastro de motoboy \n\n Informe a placa da moto:");
        String disponivel = JOptionPane.showInputDialog("Cadastro de motoboy \n\n Está disponível? (S ou N):");


        Document motoboyDocument = new Document()
                .append("nome", nome)
                .append("cpf", cpf)
                .append("telefone", telefone)
                .append("placaMoto", placaMoto)
                .append("disponivel", disponivel);

        try {
            collection.insertOne(motoboyDocument);
            JOptionPane.showMessageDialog(null, "Motoboy inserido com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editar(String id) {
        if (isValidObjectId(id)) {
            if (motoboyExiste(id)) {
                String novoNome = JOptionPane.showInputDialog("Informe o novo nome:");
                String novoCpf = JOptionPane.showInputDialog("Informe o novo CPF:");
                String novoTelefone = JOptionPane.showInputDialog("Informe o novo telefone:");
                String novaPlaca = JOptionPane.showInputDialog("Informe a nova placa da moto:");
                String disponivel = JOptionPane.showInputDialog("Informe a nova disponibilidade (S ou N):");

                Document motoboyAtualizado = new Document()
                        .append("nome", novoNome)
                        .append("cpf", novoCpf)
                        .append("telefone", novoTelefone)
                        .append("placaMoto", novaPlaca)
                        .append("disponivel", disponivel);

                collection.replaceOne(getFiltro(id), motoboyAtualizado);

                JOptionPane.showMessageDialog(null, "Motoboy editado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Motoboy não encontrado.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ID inválido. Certifique-se de fornecer um Object ID válido.");
        }
    }


    private static void remover(String id) {
        if(isValidObjectId(id)) {
            if (motoboyExiste(id)) {
                collection.deleteOne(getFiltro(id));
                JOptionPane.showMessageDialog(null, "Motoboy removido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Motoboy não encontrado.");
            }
        } else {JOptionPane.showMessageDialog(null, "ID inválido. Certifique-se de fornecer um Object ID válido.");
        }

    }

    private static void consultar() {
        FindIterable<Document> documentos = collection.find();

        for (Document documento : documentos) {
            String idDocumento = documento.getObjectId("_id").toString();

            if (isValidObjectId(idDocumento)) {
                String nome = documento.getString("nome");
                String cpf = documento.getString("cpf");
                String telefone = documento.getString("telefone");
                String placaMoto = documento.getString("placaMoto");
                String disponivel = documento.getString("disponivel");

                System.out.println("Identificador: " + idDocumento);
                System.out.println("Nome: " + nome);
                System.out.println("CPF: " + cpf);
                System.out.println("Telefone: " + telefone);
                System.out.println("Placa da moto: " + placaMoto);
                System.out.println("Disponível: " + disponivel);
                System.out.println("------------------------------");
            } else {
                JOptionPane.showMessageDialog(null, "ID inválido: " + idDocumento);
            }
        }
    }



    private static boolean motoboyExiste(String id) {
        Document motoboy = collection.find(getFiltro(id)).first();
        return motoboy != null;
    }

    private static Bson getFiltro(String id){
        return Filters.eq("_id", new ObjectId(id));
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
