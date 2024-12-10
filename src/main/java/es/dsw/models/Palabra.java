package es.dsw.models;

public class Palabra {

	private String Palabra;
	private String Definicion;
	
	public Palabra(String palabra, String definicion) {
		Palabra = palabra.trim();
		Definicion = definicion.trim();
	}

	public String getPalabra() {
		return Palabra;
	}

	public void setPalabra(String palabra) {
		Palabra = palabra;
	}

	public String getDefinicion() {
		return Definicion;
	}

	public void setDefinicion(String definicion) {
		Definicion = definicion;
	}
	
	
}
