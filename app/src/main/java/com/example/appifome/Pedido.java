package com.example.appifome;

public class Pedido {
    private int idusr;
    private boolean pizza;
    private String tamanho;
    private String sabor;
    private boolean bebida;
    private String descBebida;
    private String tele;
    private String endereco;

    public Pedido(int idusr, boolean pizza, String tamanho, String sabor, boolean bebida, String descBebida, String tele, String endereco) {
        this.idusr = idusr;
        this.pizza = pizza;
        this.tamanho = tamanho;
        this.sabor = sabor;
        this.bebida = bebida;
        this.descBebida = descBebida;
        this.tele = tele;
        this.endereco = endereco;
    }

    public int getIdusr() { return idusr; }
    public boolean isPizza() { return pizza; }
    public String getTamanho() { return tamanho; }
    public String getSabor() { return sabor; }
    public boolean isBebida() { return bebida; }
    public String getDescBebida() { return descBebida; }
    public String getTele() { return tele; }
    public String getEndereco() { return endereco; }
}
