import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//1 - Classe que representa o Item Doados
class ItemDoacao {
    private String nome;
    private String categoria; //Ex: Alimento, Roupa, Brinquedo
    private int quantidade;

    public ItemDoacao(String nome, String categoria, int quantidade) {
        this.nome = nome;
        this.categoria = categoria;
        this.quantidade = quantidade;
    }

    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    
    public void adicionarQuantidade(int qtd) {
        this.quantidade += qtd;
    }

    public boolean removerQuantidade(int qtd) {
        if (this.quantidade >= qtd) {
            this.quantidade -= qtd;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Item: %-15s | Categoria: %-10s | Quantidade: %d", nome, categoria, quantidade);
    }
}

//2 - Classe que gerencia o Estoque da ONG
class ControleEstoqueDB {
    //Caminho onde o arquivo do banco de dados será salvo
    private final String url = "jdbc:sqlite:estoque_ong.db";

    public ControleEstoqueDB() {
        criarTabelaSeNaoExistir();
    }

    //Cria a conexão com o banco
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url);
    }

    //Cria a estrutura inicial do banco de dados
    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS doacoes ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "nome TEXT UNIQUE NOT NULL, "
                   + "categoria TEXT NOT NULL, "
                   + "quantidade INTEGER NOT NULL"
                   + ");";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("❌ Erro ao criar banco de dados: " + e.getMessage());
        }
    }

    public void registrarDoacao(String nome, String categoria, int quantidade) {
        //Primeiro, tentam atualizar se o item já existir (nome é UNIQUE)
        String sqlUpdate = "UPDATE doacoes SET quantidade = quantidade + ? WHERE nome = ?";
        String sqlInsert = "INSERT INTO doacoes(nome, categoria, quantidade) VALUES(?, ?, ?)";

        try (Connection conn = conectar()) {
            //Tenta atualizar
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                pstmtUpdate.setInt(1, quantidade);
                pstmtUpdate.setString(2, nome);
                int linhasAfetadas = pstmtUpdate.executeUpdate();

                //Se nenhuma linha foi atualizada, significa que o item é novo, então insere
                if (linhasAfetadas == 0) {
                    try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                        pstmtInsert.setString(1, nome);
                        pstmtInsert.setString(2, categoria);
                        pstmtInsert.setInt(3, quantidade);
                        pstmtInsert.executeUpdate();
                        System.out.println("\n✅ Nova doação registrada no banco de dados!");
                    }
                } else {
                    System.out.println("\n✅ Quantidade atualizada no banco de dados!");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao registrar doação: " + e.getMessage());
        }
    }

    public void retirarItem(String nome, int quantidadeRetirar) {
        String sqlSelect = "SELECT quantidade FROM doacoes WHERE nome = ?";
        String sqlUpdate = "UPDATE doacoes SET quantidade = quantidade - ? WHERE nome = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
            
            pstmtSelect.setString(1, nome);
            ResultSet rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                int qtdAtual = rs.getInt("quantidade");
                
                if (qtdAtual >= quantidadeRetirar) {
                    try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                        pstmtUpdate.setInt(1, quantidadeRetirar);
                        pstmtUpdate.setString(2, nome);
                        pstmtUpdate.executeUpdate();
                        System.out.println("\n✅ Retirada realizada com sucesso!");
                    }
                } else {
                    System.out.println("\n❌ Erro: Quantidade insuficiente em estoque. Disponível: " + qtdAtual);
                }
            } else {
                System.out.println("\n❌ Erro: Item não encontrado no banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao retirar item: " + e.getMessage());
        }
    }

    public void listarEstoque() {
        String sql = "SELECT * FROM doacoes";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- ESTOQUE ATUAL (BANCO DE DADOS) ---");
            boolean temItens = false;

            while (rs.next()) {
                temItens = true;
                System.out.printf("ID: %-3d | Item: %-15s | Categoria: %-10s | Quantidade: %d\n",
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade"));
            }

            if (!temItens) {
                System.out.println("📦 O estoque está vazio no momento.");
            }
            System.out.println("--------------------------------------");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar estoque: " + e.getMessage());
        }
    }
}

//3 - Classe Principal com o Menu Interativo
public class SistemaONG {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ControleEstoqueDB estoque = new ControleEstoqueDB();
        int opcao = 0;

        System.out.println("Bem-vindo ao Sistema de Gestão da ONG!");

        while (opcao != 4) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1. Registrar nova doação (Entrada)");
            System.out.println("2. Distribuir/Retirar item (Saída)");
            System.out.println("3. Ver relatório de estoque");
            System.out.println("4. Sair");
            System.out.print("Opção: ");
            
            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        System.out.print("Nome do item (ex: Arroz): ");
                        String nomeEntrada = scanner.nextLine();
                        System.out.print("Categoria (ex: Alimento): ");
                        String categoria = scanner.nextLine();
                        System.out.print("Quantidade: ");
                        int qtdEntrada = Integer.parseInt(scanner.nextLine());
                        estoque.registrarDoacao(nomeEntrada, categoria, qtdEntrada);
                        break;
                    case 2:
                        System.out.print("Nome do item a ser retirado: ");
                        String nomeSaida = scanner.nextLine();
                        System.out.print("Quantidade a retirar: ");
                        int qtdSaida = Integer.parseInt(scanner.nextLine());
                        estoque.retirarItem(nomeSaida, qtdSaida);
                        break;
                    case 3:
                        estoque.listarEstoque();
                        break;
                    case 4:
                        System.out.println("Encerrando o sistema. Obrigado por ajudar a comunidade!");
                        break;
                    default:
                        System.out.println("❌ Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor, digite um número válido.");
            }
        }
        scanner.close();
    }
}