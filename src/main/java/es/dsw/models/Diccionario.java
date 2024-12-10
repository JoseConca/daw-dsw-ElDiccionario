package es.dsw.models;

import java.util.ArrayList;

public class Diccionario {
	
	private ArrayList<Palabra> palabras;

    public Diccionario() {
        palabras = new ArrayList<Palabra>();
    }

    // Método para agregar una palabra y su definición
    public void agregarPalabra(Palabra objPalabra) {
    	palabras.add(objPalabra);
    }

    public ArrayList<Palabra> getLista(){
    	return palabras;
    }


  
}
