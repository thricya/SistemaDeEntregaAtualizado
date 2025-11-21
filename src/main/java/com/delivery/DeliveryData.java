package com.delivery;

import java.sql.*;

public class DeliveryData {

    private static final String URL = "jdbc:sqlite:delivery.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTables() {
        String sqlClientes = """
            CREATE TABLE IF NOT EXISTS clientes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                telefone TEXT,
                email TEXT,
                endereco TEXT,
                cpf TEXT UNIQUE
            );
        """;

        String sqlRestaurantes = """
            CREATE TABLE IF NOT EXISTS restaurantes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                telefone TEXT,
                email TEXT,
                endereco TEXT,
                cpf TEXT,
                cnpj TEXT
            );
        """;

        String sqlPratos = """
            CREATE TABLE IF NOT EXISTS pratos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                preco REAL,
                descricao TEXT,
                restaurante_id INTEGER,
                FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id)
            );
        """;

        String sqlPedidos = """
            CREATE TABLE IF NOT EXISTS pedidos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_id INTEGER,
                restaurante_id INTEGER,
                prato_id INTEGER,
                horario TEXT,
                status TEXT,
                FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id),
                FOREIGN KEY (prato_id) REFERENCES pratos(id)
            );
        """;

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlClientes);
            stmt.execute(sqlRestaurantes);
            stmt.execute(sqlPratos);
            stmt.execute(sqlPedidos);
            System.out.println("Tabelas criadas/verificadas com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    public static int insertCliente(String nome, String telefone, String email, String endereco, String cpf) {

        String sql = "INSERT INTO clientes (nome, telefone, email, endereco, cpf) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, telefone);
            pstmt.setString(3, email);
            pstmt.setString(4, endereco);
            pstmt.setString(5, cpf);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }

        return -1;
    }

    public static int insertRestaurante(String nome, String telefone, String email, String endereco, String cpf, String cnpj) {

        String sql = "INSERT INTO restaurantes (nome, telefone, email, endereco, cpf, cnpj) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, telefone);
            pstmt.setString(3, email);
            pstmt.setString(4, endereco);
            pstmt.setString(5, cpf);
            pstmt.setString(6, cnpj);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar restaurante: " + e.getMessage());
        }

        return -1;
    }

    public static int insertPrato(String nome, double preco, String descricao, int restauranteId) {

        String sql = "INSERT INTO pratos (nome, preco, descricao, restaurante_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nome);
            pstmt.setDouble(2, preco);
            pstmt.setString(3, descricao);
            pstmt.setInt(4, restauranteId);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar prato: " + e.getMessage());
        }

        return -1;
    }

    public static int insertPedido(int clienteId, int restauranteId, int pratoId, String horario, String status) {

        String sql = "INSERT INTO pedidos (cliente_id, restaurante_id, prato_id, horario, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, clienteId);
            pstmt.setInt(2, restauranteId);
            pstmt.setInt(3, pratoId);
            pstmt.setString(4, horario);
            pstmt.setString(5, status);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar pedido: " + e.getMessage());
        }

        return -1;
    }

    public static void listarPedidos() {

        String sql = """
            SELECT p.id, c.nome AS cliente, r.nome AS restaurante, pr.nome AS prato,
                   p.horario, p.status
            FROM pedidos p
            JOIN clientes c ON c.id = p.cliente_id
            JOIN restaurantes r ON r.id = p.restaurante_id
            JOIN pratos pr ON pr.id = p.prato_id;
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== Pedidos cadastrados ===");

            while (rs.next()) {
                System.out.printf(
                    "ID: %d | Cliente: %s | Restaurante: %s | Prato: %s | Horário: %s | Status: %s%n",
                    rs.getInt("id"),
                    rs.getString("cliente"),
                    rs.getString("restaurante"),
                    rs.getString("prato"),
                    rs.getString("horario"),
                    rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        }
    }
}
