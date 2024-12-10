package es.dsw.controllers;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import es.dsw.models.Diccionario;
import es.dsw.models.Palabra;


@Controller
@SessionAttributes({"ultPal","dic", "busquedas"})
public class MainController {

	private Diccionario dic = new Diccionario();
	private Palabra palabraPrueba = new Palabra("Prueba", "definicion");
	private Palabra ultPal;
	
	
	//A)
	@GetMapping(value= {"/", "index"})
	public String inde(Model modelo,
			SessionStatus estadoSesion,
			@RequestParam(name="errorCode", defaultValue="0") int errorCode) 
	{
		String msgError = "";
		if(errorCode==1) {
			msgError="ERROR: NO SE HA INDICADO UNA PALABRA A BUSCAR";
		}
		// Recuperamos la variable de sesión 'busquedas' si existe, o la inicializamos
        Integer busquedas = (Integer) modelo.getAttribute("busquedas");
        if (busquedas == null) {
            busquedas = 0;  // Inicializamos a 0 si no existe
        }
		modelo.addAttribute("errorCode", errorCode);
		modelo.addAttribute("msgError", msgError);
        modelo.addAttribute("busquedas", busquedas);  // Enviamos el contador al modelo

			
		return "/index";
	}
	
	 @PostMapping(value="buscar")
	    public String busc(Model modelo,
	                       @RequestParam(name="palabraABuscar") String palabraABuscar) {
	        
	        // Si la palabra es válida, incrementamos el contador de búsquedas
	        if (palabraABuscar != null && !palabraABuscar.trim().isEmpty()) {
	            // Recuperamos el contador de búsquedas
	            Integer busquedas = (Integer) modelo.getAttribute("busquedas");
	            if (busquedas == null) {
	                busquedas = 0;
	            }
	            busquedas++;  // Incrementamos el contador de búsquedas exitosas
	            
	            // Guardamos de nuevo el contador en la sesión
	            modelo.addAttribute("busquedas", busquedas);

	            // Realizamos la búsqueda y cualquier otra lógica necesaria
	            if (dic.getLista().stream().noneMatch(p -> p.getPalabra().equalsIgnoreCase(palabraABuscar))) {
	                // La palabra no existe en el diccionario, vamos a la vista 'nueva'
	                ultPal = new Palabra(palabraABuscar, "definición por defecto");
	                modelo.addAttribute("ultPal", ultPal);
	                return "/views/nueva";
	            }
	        } else {
	            // Si no se ha ingresado una palabra, mostramos un error
	            return "redirect:/?errorCode=1";
	        }

	        // Si todo va bien, vamos a la vista de definición
	        return "/views/definicion";
	    }
	
	private Palabra buscarPalabraEnDiccionario(String palabra) {
	    for (Palabra p : dic.getLista()) {
	        if (p.getPalabra().equalsIgnoreCase(palabra)) {
	            return p; // Si se encuentra, devolver la palabra
	        }
	    }
	    return null; // Si no se encuentra, devolver null
	}
	
	
	
	
	
	@GetMapping(value= {"definicion"})
	public String defi(Model modelo, SessionStatus estadoSesion) {
		
		return "/views/definicion";
	}
	

	
	@GetMapping(value= {"nueva"})
	public String nue(Model modelo, SessionStatus estadoSesion){
		
		String palabra = ultPal.getPalabra();
	    modelo.addAttribute("ultPal", palabra);
	    
		return "/views/nueva";
	}
	
	@PostMapping(value = "/agregar")
	public String agregar(Model modelo, 
	                      @RequestParam(name = "palabra") String palabra,
	                      @RequestParam(name = "definicion") String definicion) {

	    // Validar que la palabra y definición no estén vacías
	    if (palabra.trim().isEmpty() || definicion.trim().isEmpty()) {
	        return "/views/nueva";  // Si hay campos vacíos, volvemos a la página "nueva"
	    }

	    // Crear una nueva palabra con su definición
	    Palabra nuevaPalabra = new Palabra(palabra.trim(), definicion.trim());

	    // Agregar la nueva palabra al diccionario
	    dic.agregarPalabra(nuevaPalabra);

	    // Establecer la palabra agregada como la última palabra en la sesión (opcional)
	    modelo.addAttribute("ultPal", nuevaPalabra);

	    // Pasar el diccionario actualizado al modelo
	    modelo.addAttribute("dic", dic);  // El diccionario completo

	    // Devolver la vista "lista" para mostrar todas las palabras
	    return "/views/lista";  // Aquí simplemente retornamos la vista "lista"
	}
	@GetMapping(value = {"lista"})
	public String lista(Model modelo) {
	    // Pasar el diccionario con todas las palabras a la vista
	    modelo.addAttribute("dic", dic);  // El diccionario contiene todas las palabras
	    return "/views/lista";  // Devuelve la vista que mostrará todas las palabras
	}
	
	
}
