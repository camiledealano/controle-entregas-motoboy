package repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import persistence.DatabaseConnection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PedidoRepository {
    private static final MongoCollection<Document> collection = DatabaseConnection.getDatabase("entregasMotoboy")
            .getCollection("Pedidos");

    public static void crud() {
        boolean sair = false;

        while (!sair) {
            String[] options = { "Adicionar", "Editar", "Remover", "Consultar", "Voltar" };

            int opcao = JOptionPane.showOptionDialog(null, "Escolha uma opção para pedidos: ",
                    "Menu Pedidos", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (opcao) {
                case 0:
                    adicionar();
                    break;
                case 1:
                    String idEdicao = JOptionPane.showInputDialog("Edição de pedido \n\n Digite o id para editar:");
                    editar(idEdicao);
                    break;
                case 2:
                    String idExclusao = JOptionPane.showInputDialog("Remover pedido \n\n Digite o id para remover:");
                    remover(idExclusao);
                    break;
                case 3:
                    consultar();
                    break;
                case 4:
                    sair = true;
                    break;
            }
            if (sair) {
                break;
            }
        }
    }

    private static String obterInputCancelamento(String mensagem) {
        String input = JOptionPane.showInputDialog(mensagem);

        if (input == null) {
            JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
            return null;
        }

        return input;
    }
    private static void adicionar() {
        String idCliente = JOptionPane.showInputDialog("Cadastro de pedido \n\n Informe o identificador do cliente:");

        if(idCliente!= null) {
        String item = JOptionPane.showInputDialog("Cadastro de pedido \n\n Qual item você gostaria de realizar entrega?");
        String endereco = JOptionPane.showInputDialog("Cadastro de pedido \n\n Informe o códido do endereço:");
        String observacoesAux = JOptionPane.showInputDialog("Cadastro de pedido \n\n Observações:");
        String observacoes = observacoesAux != null ? observacoesAux : " ";
        String idMotoboys = getIdAleatorioMotoboyDisponivel();

        if (getIdAleatorioMotoboyDisponivel() != null) {

            Document pedidoDocument = new Document()
                    .append("idCliente", idCliente)
                    .append("item", item)
                    .append("endereco", endereco)
                    .append("observacoes", observacoes)
                    .append("idMotoboy", idMotoboys);

            collection.insertOne(pedidoDocument);
            EntregaRepository.criarEntrega(pedidoDocument);

            JOptionPane.showMessageDialog(null, "Pedido inserido com sucesso!");
        } else {
            Document pedidoSemMotoboy = new Document()
                    .append("idCliente", idCliente)
                    .append("item", item)
                    .append("endereco", endereco)
                    .append("observacoes", observacoes);

            EntregaRepository.criarEntrega(pedidoSemMotoboy);
            JOptionPane.showMessageDialog(null, "Nenhum motoboy disponível no momento.");
        }}else {
            JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
        }
  }

    private static void editar(String id) {
        if (id == null) {
            JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
            return;
        }
        if (isValidObjectId(id)) {
            if (pedidoExiste(id)) {
                String novoIdCliente = obterInputCancelamento("Informe o novo identificador do cliente:");
                String novoItem = obterInputCancelamento("Informe o novo item:");
                String novoEndereco = obterInputCancelamento("Informe o novo código do endereço:");
                String novaObservacao = obterInputCancelamento("Informe a nova observação:");
                String idMotoboy = getIdAleatorioMotoboyDisponivel();

                if (novoIdCliente != null && novoItem != null && novoEndereco != null && novaObservacao != null && idMotoboy != null) {
                Document pedidoAtualizado = new Document()
                        .append("idCliente", novoIdCliente)
                        .append("nome", novoItem)
                        .append("nome", novoEndereco)
                        .append("novaObservacao", novaObservacao)
                        .append("idMotoboy", idMotoboy);

                    try {
                        collection.replaceOne(getFiltro(id), pedidoAtualizado);
                        JOptionPane.showMessageDialog(null, "Pedido editado com sucesso!");
                    } catch (Exception e) {
                        System.out.println("Erro ao editar pedido: " + e.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pedido não encontrado.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ID inválido. Certifique-se de fornecer um Object ID válido.");
        }
    }
    private static void remover(String id) {
        if (id == null) {
            JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
            return;
        } if (isValidObjectId(id)){
        if (pedidoExiste(id)) {
            try {
                collection.deleteOne(getFiltro(id));
                JOptionPane.showMessageDialog(null, "Pedido removido com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro ao remover pedido: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pedido não encontrado.");
        }} else {JOptionPane.showMessageDialog(null, "ID inválido. Certifique-se de fornecer um Object ID válido.");
        }

    }

    private static void consultar() {
        FindIterable<Document> documentos = collection.find();

        for (Document documento : documentos) {
            String id = documento.getObjectId("_id").toString();
            String idCliente = documento.getString("idCliente");
            String idMotoboy = documento.getString("idMotoboy");
            String item = documento.getString("item");
            String endereco = documento.getString("endereco");
            String observacoes = documento.getString("observacoes");

            System.out.println("Identificador: " + id);
            System.out.println("Identificador do cliente: " + idCliente);
            System.out.println("Identificador do motoboy: " + idMotoboy);
            System.out.println("Item: " + item);
            System.out.println("Endereço: " + endereco);
            System.out.println("Observações: " + observacoes);
            System.out.println("------------------------------");
        }
    }

    private static String getIdAleatorioMotoboyDisponivel() {
        List<String> idsMotoboys = new ArrayList<>();
        MongoCollection<Document> motoboysCollection = DatabaseConnection.getDatabase("entregasMotoboy")
                .getCollection("Motoboys");

        Bson filtroDisponibilidade = Filters.eq("disponivel", "S");
        FindIterable<Document> motoboys = motoboysCollection.find(filtroDisponibilidade);

        for (Document motoboy : motoboys) {
            idsMotoboys.add(motoboy.getObjectId("_id").toString());
        }

        if (!idsMotoboys.isEmpty()) {
            return idsMotoboys.get(new Random().nextInt(idsMotoboys.size()));
        } else {
            return null;
        }
    }

    private static boolean pedidoExiste(String id) {
        Document pedido = collection.find(getFiltro(id)).first();
        return pedido != null;
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
