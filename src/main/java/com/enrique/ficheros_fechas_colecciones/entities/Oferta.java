package com.enrique.ficheros_fechas_colecciones.entities;

public class Oferta {

	private int edadMinima;
	private int edadMaxima; 
	private int saldoMinimo;
	private int saldoMaximo;
	private String tarjeta;
    
    public Oferta() {}

    public Oferta(int edadMinima, int edadMaxima, int saldoMinimo, int saldoMaximo, String tarjeta) {
    	super();
    	this.edadMinima = edadMinima;
    	this.edadMaxima = edadMaxima;
    	this.saldoMinimo = saldoMinimo;
    	this.saldoMaximo = saldoMaximo;
    	this.tarjeta = tarjeta;
    }

    public Oferta(Oferta c) {
    	super();
    	this.edadMinima = c.edadMinima;
    	this.edadMaxima = c.edadMaxima;
    	this.saldoMinimo = c.saldoMinimo;
    	this.saldoMaximo = c.saldoMaximo;
    	this.tarjeta = c.tarjeta;
    }
   
	public int getEdadMinima() {
		return edadMinima;
	}

	public void setEdadMinima(int edadMinima) {
		this.edadMinima = edadMinima;
	}

	public int getEdadMaxima() {
		return edadMaxima;
	}

	public void setEdadMaxima(int edadMaxima) {
		this.edadMaxima = edadMaxima;
	}

	public int getSaldoMinimo() {
		return saldoMinimo;
	}

	public void setSaldoMinimo(int saldoMinimo) {
		this.saldoMinimo = saldoMinimo;
	}

	public int getSaldoMaximo() {
		return saldoMaximo;
	}

	public void setSaldoMaximo(int saldoMaximo) {
		this.saldoMaximo = saldoMaximo;
	}

	public String getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}

	@Override
	public String toString() {
		return "Cuenta [edadMinima=" + edadMinima + ", edadMaxima=" + edadMaxima + ", saldoMinimo=" + saldoMinimo + ", saldoMaximo=" + saldoMaximo + ", tarjeta=" + tarjeta
				+ "]";
	} 

}