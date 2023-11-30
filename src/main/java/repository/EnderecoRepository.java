package repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import persistence.DatabaseConnection;

import javax.swing.*;

public class EnderecoRepository {

    private static final MongoCollection<Document> collection = DatabaseConnection.getDatabase("entregasMotoboy")
            .getCollection("Enderecos");

    public static void crud() {
        boolean sair = false;

        while (!sair) {
            String[] options = { "Adicionar", "Editar", "Remover", "Consultar", "Voltar" };

            int opcao = JOptionPane.showOptionDialog(null, "Escolha uma opção para endereços: ",
                    "Menu Endereços", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (opcao) {
                case 0:
                    adicionar();
                    break;
                case 1:
                    String idEdicao = JOptionPane.showInputDialog("Edição de endeço \n\n Digite o id para editar:");
                    editar(idEdicao);
                    break;
                case 2:
                    String idExclusao = JOptionPane.showInputDialog("Remover endeço \n\n Digite o id para remover:");
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
        String logradouro = JOptionPane.showInputDialog("Cadastro de endereço \n\n Informe o logradouro:");
        String cep        = JOptionPane.showInputDialog("Cadastro de endereço \n\n Informe o CEP:");
        Integer numero    = Integer.parseInt(JOptionPane.showInputDialog("Cadastro de endereço \n\n Informe o número:"));
        String bairro     = JOptionPane.showInputDialog("Cadastro de endereço \n\n Informe o bairro:");
        String cidade     = JOptionPane.showInputDialog("Cadastro de endereço \n\n Informe a cidade:");
        String estado     = JOptionPane.showInputDialog("Cadastro de endereço \n\n Informe o estado:");
        String pais       = JOptionPane.showInputDialog("Cadastro de endereço \n\n Informe o pais:");

        Document enderecoDocument = new Document()
                .append("logradouro", logradouro)
                .append("cep", cep)
                .append("numero", numero)
                .append("bairro", bairro)
                .append("cidade", cidade)
                .append("estado", estado)
                .append("pais", pais);

        collection.insertOne(enderecoDocument);

        JOptionPane.showMessageDialog(null, "Endereço inserido com sucesso!");
    }

    private static void editar(String id) {
        if (isValidObjectId(id) && enderecoExiste(id)) {
            String novoLogradouro = JOptionPane.showInputDialog("Informe o novo logradouro:");
            String novoCep = JOptionPane.showInputDialog("Informe o novo cep:");
            Integer novoNumero = Integer.parseInt(JOptionPane.showInputDialog("Informe o novo número:"));
            String novoBairro = JOptionPane.showInputDialog("Informe o novo bairro:");
            String novaCidade = JOptionPane.showInputDialog("Informe a nova cidade:");
            String novoEstado = JOptionPane.showInputDialog("Informe o novo estado:");
            String novoPais = JOptionPane.showInputDialog("Informe o novo país:");

            Document enderecoAtualizado = new Document()
                    .append("logradouro", novoLogradouro)
                    .append("cep", novoCep)
                    .append("numero", novoNumero)
                    .append("bairro", novoBairro)
                    .append("cidade", novaCidade)
                    .append("estado", novoEstado)
                    .append("pais", novoPais);

            collection.replaceOne(getFiltro(id), enderecoAtualizado);

            JOptionPane.showMessageDialog(null, "Endereço editado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Id inválido ou Endereço não encontrado.");
        }
    }

    private static void remover(String id) {
        if (isValidObjectId(id) && enderecoExiste(id)) {
            collection.deleteOne(getFiltro(id));
            JOptionPane.showMessageDialog(null, "Endereço removido com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "ID inválido ou Endereço não encontrado.");
        }
    }

    private static void consultar() {
        FindIterable<Document> documentos = collection.find();

        for (Document documento : documentos) {
            String id = documento.getObjectId("_id").toString();
            String logradouro = documento.getString("logradouro");
            String cep = documento.getString("cep");
            Integer numero = documento.getInteger("numero");
            String bairro = documento.getString("bairro");
            String cidade = documento.getString("cidade");
            String estado = documento.getString("estado");
            String pais = documento.getString("pais");

            if (isValidObjectId(id)) {
                System.out.println("Identificador: " + id);
                System.out.println("Logradouro: " + logradouro);
                System.out.println("CEP: " + cep);
                System.out.println("Número: " + numero);
                System.out.println("Bairro: " + bairro);
                System.out.println("Cidade: " + cidade);
                System.out.println("Estado: " + estado);
                System.out.println("País: " + pais);
                System.out.println("------------------------------");
            } else {
                JOptionPane.showMessageDialog(null, "ID inválido: " + id);
            }
        }
    }


    private static boolean enderecoExiste(String id) {
        Document endereco = collection.find(getFiltro(id)).first();
        return endereco != null;
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

