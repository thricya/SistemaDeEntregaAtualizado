package com.delivery;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SistemaDelivery {

    private Scanner input = new Scanner(System.in);

    public void menuPrincipal() {

        DeliveryData.createTables();

        int opcao;

        do {

            System.out.println("\n=== SISTEMA DE ENTREGAS ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Cadastrar Restaurante");
            System.out.println("3. Cadastrar Prato");
            System.out.println("4. Fazer Pedido");
            System.out.println("5. Listar Pedidos");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> cadastrarRestaurante();
                case 3 -> cadastrarPrato();
                case 4 -> fazerPedido();
                case 5 -> DeliveryData.listarPedidos();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    private void cadastrarCliente() {

        System.out.print("Nome: ");
        String nome = input.nextLine();

        System.out.print("Telefone: ");
        String telefone = input.nextLine();

        System.out.print("Email: ");
        String email = input.nextLine();

        System.out.print("Endereço: ");
        String endereco = input.nextLine();

        System.out.print("CPF: ");
        String cpf = input.nextLine();

        int id = DeliveryData.insertCliente(nome, telefone, email, endereco, cpf);

        if (id != -1)
            System.out.println("Cliente cadastrado com ID: " + id);
    }

    private void cadastrarRestaurante() {

        System.out.print("Nome: ");
        String nome = input.nextLine();

        System.out.print("Telefone: ");
        String telefone = input.nextLine();

        System.out.print("Email: ");
        String email = input.nextLine();

        System.out.print("Endereço: ");
        String endereco = input.nextLine();

        System.out.print("CPF do responsável: ");
        String cpf = input.nextLine();

        System.out.print("CNPJ: ");
        String cnpj = input.nextLine();

        int id = DeliveryData.insertRestaurante(nome, telefone, email, endereco, cpf, cnpj);

        if (id != -1)
            System.out.println("Restaurante cadastrado com ID: " + id);
    }

    private void cadastrarPrato() {

        try (Connection conn = DeliveryData.connect()) {

            System.out.println("Restaurantes disponíveis:");
            // Uso de Statement aqui é aceitável, pois não há entrada de usuário na query
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT id, nome FROM restaurantes");

            while (rs.next())
                System.out.println(rs.getInt("id") + " - " + rs.getString("nome"));

            System.out.print("ID do restaurante: ");
            int restauranteId = Integer.parseInt(input.nextLine());

            System.out.print("Nome do prato: ");
            String nome = input.nextLine();

            System.out.print("Preço: ");
            double preco = Double.parseDouble(input.nextLine());

            System.out.print("Descrição: ");
            String descricao = input.nextLine();

            int id = DeliveryData.insertPrato(nome, preco, descricao, restauranteId);

            if (id != -1)
                System.out.println("Prato cadastrado com ID: " + id);

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar prato: " + e.getMessage());
        }
    }

    private void fazerPedido() {
        
        // Abre e fecha a conexão automaticamente
        try (Connection conn = DeliveryData.connect();
             Statement st = conn.createStatement()) {

            // 1. Seleção do Cliente (usando Statement para SELECT geral)
            System.out.println("Clientes disponíveis:");
            ResultSet rc = st.executeQuery("SELECT id, nome FROM clientes");
            while (rc.next())
                System.out.println(rc.getInt("id") + " - " + rc.getString("nome"));

            System.out.print("ID do cliente: ");
            int clienteId = Integer.parseInt(input.nextLine());

            // 2. Seleção do Restaurante (usando Statement para SELECT geral)
            System.out.println("Restaurantes disponíveis:");
            ResultSet rr = st.executeQuery("SELECT id, nome FROM restaurantes");
            while (rr.next())
                System.out.println(rr.getInt("id") + " - " + rr.getString("nome"));

            System.out.print("ID do restaurante: ");
            int restauranteId = Integer.parseInt(input.nextLine());

            // 3. Seleção do Prato (SEGURANÇA APLICADA AQUI)
            // Usando PreparedStatement para a consulta que tem uma variável do usuário no WHERE
            System.out.println("Pratos disponíveis:");
            
            String sqlPratos = "SELECT id, nome, preco FROM pratos WHERE restaurante_id = ?";

            // Novo bloco try-with-resources para o PreparedStatement
            try (PreparedStatement psPratos = conn.prepareStatement(sqlPratos)) {
                psPratos.setInt(1, restauranteId);
                
                // Executa a consulta segura
                ResultSet rp = psPratos.executeQuery(); 
                
                while (rp.next())
                    System.out.println(rp.getInt("id") + " - " + rp.getString("nome") + " - R$" + rp.getDouble("preco"));
                
            } // psPratos e rp são fechados automaticamente aqui

            System.out.print("ID do prato: ");
            int pratoId = Integer.parseInt(input.nextLine());

            // 4. Inserção do Pedido
            String horario = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String status = "Em andamento";

            int id = DeliveryData.insertPedido(clienteId, restauranteId, pratoId, horario, status);

            if (id != -1)
                System.out.println("Pedido realizado com ID: " + id);

        } catch (SQLException e) {
            System.out.println("Erro ao fazer pedido: " + e.getMessage());
        }
    }
}