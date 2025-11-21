package com.delivery;

import java.util.ArrayList;
import java.util.List;

public class Restaurante extends Usuario {

    private int id;
    private String cnpj;
    private List<Prato> pratos = new ArrayList<>();

    public Restaurante(String nome, String telefone, String email, String endereco, String cpf, String cnpj) {
        super(nome, telefone, email, endereco, cpf);
        this.cnpj = cnpj;
    }

    public Restaurante(int id, String nome, String telefone, String email, String endereco, String cpf, String cnpj) {
        super(nome, telefone, email, endereco, cpf);
        this.id = id;
        this.cnpj = cnpj;
    }

    public void adicionarPrato(Prato prato) {
        pratos.add(prato);
    }

    public List<Prato> getPratos() {
        return pratos;
    }

    public int getId() { return id; }
    public String getCnpj() { return cnpj; }

    @Override
    public void mostrarInfo() {
        System.out.println("Restaurante: " + nome + " | CNPJ: " + cnpj + " | Telefone: " + telefone);
    }
}
