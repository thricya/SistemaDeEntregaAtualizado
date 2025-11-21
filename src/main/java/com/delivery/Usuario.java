package com.delivery;

/**
 * Classe abstrata que representa um usuário genérico do sistema.
 */
public abstract class Usuario {
    protected String nome;
    protected String telefone;
    protected String email;
    protected String endereco;
    protected String cpf;

    public Usuario(String nome, String telefone, String email, String endereco, String cpf) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.cpf = cpf;
    }

    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public String getEndereco() { return endereco; }
    public String getCpf() { return cpf; }

    public abstract void mostrarInfo();
}
