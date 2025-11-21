package com.delivery;

public class Prato {

    private int id;
    private String nome;
    private double preco;
    private String descricao;
    private int restauranteId; // Novo atributo de chave estrangeira

    // Construtor para NOVO Prato (ID não é conhecido, mas o Restaurante é)
    public Prato(String nome, double preco, String descricao, int restauranteId) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.restauranteId = restauranteId;
    }

    // Construtor para Prato LIDO do Banco de Dados (Todos os atributos conhecidos)
    public Prato(int id, String nome, double preco, String descricao, int restauranteId) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.restauranteId = restauranteId;
    }

    // --- Getters ---
    public int getId(){ return id; }
    public String getNome(){ return nome; }
    public double getPreco(){ return preco; }
    public String getDescricao(){ return descricao; }
    public int getRestauranteId() { return restauranteId; } // Novo Getter

    // --- Setters (Adicionados para possibilitar atualização futura) ---
    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setRestauranteId(int restauranteId) { this.restauranteId = restauranteId; }


    @Override
    public String toString() {
        return nome + " - R$" + preco + " (" + descricao + ") - ID Restaurante: " + restauranteId;
    }

    public void setId(int id) {
        this.id = id;
    }
}