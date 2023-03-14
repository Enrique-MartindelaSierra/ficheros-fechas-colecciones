package com.enrique.ficheros_fechas_colecciones.entities;

public class Cuenta {

	private String dni;
	private String nombre; 
	private String fecha;
	private String pais;
	private double saldo;
    
    public Cuenta() {}

    public Cuenta(String dni, String nombre, String fecha, String pais, double saldo) {
    	super();
    	this.dni = dni;
    	this.nombre = nombre;
    	this.fecha = fecha;
    	this.pais = pais;
    	this.saldo = saldo;
    }

    public Cuenta(Cuenta c) {
    	super();
    	this.dni = c.dni;
    	this.nombre = c.nombre;
    	this.fecha = c.fecha;
    	this.pais = c.pais;
    	this.saldo = c.saldo;
    }
    
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	@Override
	public String toString() {
		return "Cuenta [dni=" + dni + ", nombre=" + nombre + ", fecha=" + fecha + ", pais=" + pais + ", saldo=" + saldo
				+ "]";
	} 

}