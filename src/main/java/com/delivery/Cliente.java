package com.delivery;

public class Cliente extends Usuario {

    public Cliente(String nome, String telefone, String email, String endereco, String cpf) {
        super(nome, telefone, email, endereco, cpf);
    }

    @Override
    public void mostrarInfo() {
        System.out.println("Cliente: " + nome + " | CPF: " + cpf + " | Telefone: " + telefone);
    }
    }