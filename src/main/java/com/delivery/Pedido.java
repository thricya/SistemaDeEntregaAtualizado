package com.delivery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pedido {

    private int id;
    private Cliente cliente;
    private Restaurante restaurante;
    private Prato prato;
    private LocalDateTime horario;
    private String status;

    public Pedido(Cliente cliente, Restaurante restaurante, Prato prato) {
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.prato = prato;
        this.horario = LocalDateTime.now();
        this.status = "Em andamento";
    }

    public Pedido(int id, Cliente cliente, Restaurante restaurante, Prato prato, LocalDateTime horario, String status) {
        this.id = id;
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.prato = prato;
        this.horario = horario;
        this.status = status;
    }

    public int getId(){ return id; }
    public String getStatus(){ return status; }
    public void marcarEntregue(){ this.status = "Entregue"; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Pedido de " + cliente.getNome() + " | Restaurante: " + restaurante.getNome() +
                " | Prato: " + prato.getNome() + " | Status: " + status +
                " | Horário: " + horario.format(fmt);
    }
}
