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
            String[] options = {"Adicionar", "Editar", "Remover", "Consultar", "Voltar"};

            int opcao = JOptionPane.showOptionDialog(null, "Escolha uma opção para endereços: ",
                    "Menu Endereços", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (opcao) {
                case 0:
                    adicionar();
                    break;
                case 1:
                    editar();
                    break;
                case 2:
                    remover();
                    break;
                case 3:
                    consultar();
                    break;
                case 4:
                    sair = true;
                    break;
                default:
                    // Tratar opção inválida, se necessário
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
        String logradouro = obterInputCancelamento("Cadastro de endereço \n\n Informe o logradouro:");
        if (logradouro == null) {
            return; // Operação cancelada pelo usuário
        }

        String cep = obterInputCancelamento("Cadastro de endereço \n\n Informe o CEP:");
        if (cep == null) {
            return;
        }

        Integer numero = obterNumero("Cadastro de endereço \n\n Informe o número:");
        if (numero == null) {
            return;
        }

        String bairro = obterInputCancelamento("Cadastro de endereço \n\n Informe o bairro:");
        if (bairro == null) {
            return;
        }

        String cidade = obterInputCancelamento("Cadastro de endereço \n\n Informe a cidade:");
        if (cidade == null) {
            return;
        }

        String estado = obterInputCancelamento("Cadastro de endereço \n\n Informe o estado:");
        if (estado == null) {
            return;
        }

        String pais = obterInputCancelamento("Cadastro de endereço \n\n Informe o país:");
        if (pais == null) {
            return;
        }

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

    private static void editar() {
        String id = obterInputCancelamento("Edição de endereço \n\n Digite o id para editar:");
        if (id == null) {
            return;
        }

        if (isValidObjectId(id) && enderecoExiste(id)) {
            String novoLogradouro = obterInputCancelamento("Informe o novo logradouro:");
            String novoCep = obterInputCancelamento("Informe o novo cep:");
            Integer novoNumero = obterNumero("Informe o novo número:");
            String novoBairro = obterInputCancelamento("Informe o novo bairro:");
            String novaCidade = obterInputCancelamento("Informe a nova cidade:");
            String novoEstado = obterInputCancelamento("Informe o novo estado:");
            String novoPais = obterInputCancelamento("Informe o novo país:");

            if (novoLogradouro != null && novoCep != null && novoNumero != null && novoBairro != null && novaCidade != null && novoEstado != null && novoPais != null) {
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
                JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ID inválido ou Endereço não encontrado.");
        }
    }

    private static void remover() {
        String id = obterInputCancelamento("Remover endereço \n\n Digite o id para remover:");
        if (id == null) {
            return;
        }

        if (isValidObjectId(id) && enderecoExiste(id)) {
            int opcao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este endereço?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (opcao == JOptionPane.YES_OPTION) {
                collection.deleteOne(getFiltro(id));
                JOptionPane.showMessageDialog(null, "Endereço removido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Remoção cancelada pelo usuário.");
            }
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

    private static Integer obterNumero(String mensagem) {
        try {
            String numeroInput = obterInputCancelamento(mensagem);
            return (numeroInput != null) ? Integer.parseInt(numeroInput) : null;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Número inválido. Certifique-se de fornecer um valor numérico válido.");
            return null;
        }
    }

    private static boolean enderecoExiste(String id) {
        Document endereco = collection.find(getFiltro(id)).first();
        return endereco != null;
    }

    private static Bson getFiltro(String id) {
        return Filters.eq("_id", new ObjectId(id));
    }

    private static boolean isValidObjectId(String id) {
        return id != null && id.length() == 24 && id.chars().allMatch(Character::isLetterOrDigit);
    }
}
