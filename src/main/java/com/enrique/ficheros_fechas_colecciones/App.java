package com.enrique.ficheros_fechas_colecciones;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.enrique.ficheros_fechas_colecciones.entities.Cuenta;
import com.enrique.ficheros_fechas_colecciones.entities.Oferta;

/**
 * Somos una entidad financiera que trabaja con varios bancos y que quiere
 * ofrecer productos a sus clientes dependiendo de ciertas características del
 * mismo. Se pedirá por consola el dni-cif del cliente, a partir de ese momento
 * buscaremos en una ruta (que tendremos definida como una variable global)
 * todos los ficheros txt que recogen información de los clientes en los
 * diferentes bancos. Hemos conseguido que todos los bancos nos envíen toda la
 * información en un formato unificado de este tipo:
 * 
 * dni-cif;nombre_cliente;fecha_nacimiento(dd/mm/aaaa);código_país;saldo
 * 
 * El programa al recibir un dni-cif hará lo siguiente:
 * 
 * Sacar un mensaje de bienvenida en el idioma del cliente (en español para los
 * clientes de España y en inglés para el resto) donde incluya su nombre (puedes
 * coger el primero que encuentres).
 * 
 * La hora actual (en formato "largo", y poniendo el día antes que el mes para
 * los clientes españoles y el mes antes que el día para el resto)
 * 
 * Posteriormente, comprobará si el cliente tiene más de una fecha de nacimiento
 * en los ficheros, en cuyo caso, sacará por pantalla las mismas para que el
 * cliente seleccione cuál es la correcta (si solo hay una no preguntará).
 * 
 * Una vez confirmada la fecha de nacimiento, buscaremos en otro fichero de
 * productos (donde tenemos criterios de que productos ofrecer basándonos en el
 * saldo y la edad del cliente) acorde al siguiente formato:
 * 
 * edad_mínima;edad_maxima;saldo_mínimo;saldo_máximo;nombre_producto
 * 
 * (si el cliente puede acceder a más de un producto financiero, mostraremos
 * aquel cuyo saldo mínimo sea mayor). Para obtener el saldo del cliente deberás
 * sumar (puede haber saldos negativos), su saldo de todos los bancos.
 * 
 * Nota: Intenta estructurar en clases el proyecto Maven. Utiliza las clases ya
 * vistas en el curso hasta el momento.
 * 
 * Se adjuntan datos de 3 bancos (caixa, santander y sabadell) y el fichero de
 * productos (productosofertados)
 * 
 * 
 * caixa.txt caixa.txt
 * 
 * productosofertados.txt productosofertados.txt
 * 
 * sabadell.txt sabadell.txt
 * 
 * santander.txt santander.txt !
 *
 */
public class App {
	static Scanner sc= new Scanner(System.in);
	static Path ficheros = Paths.get("assets/ficheros");
	static Path productos = Paths.get("assets/productos-ofertados.txt");
	static List<Cuenta> caixa = new ArrayList<>();
	static List<Cuenta> sabadell = new ArrayList<>();
	static List<Cuenta> santander = new ArrayList<>();


	public static String pedirDNI_CIF() {
//		Scanner sc = new Scanner(System.in);
		System.out.println("Introduzca DNI o CIF: ");
		String dni = sc.nextLine();
		// refinar

//		sc.close();
		return dni;
	}

	
	public static Cuenta leerFicherosEnCarpetaTodasLasLineasAClaseCuenta(String dnicif) {

		Cuenta elegida = new Cuenta();
		Cuenta error = new Cuenta("error","error","error","error",0);
		if (Files.isDirectory(ficheros)) {
			try {
				Files.list(ficheros).filter(Files::isRegularFile).forEach(archivo -> {
					if (Files.isRegularFile(archivo)) {
						try (BufferedReader lector = new BufferedReader(new FileReader(archivo.toFile()))) {
							String linea;
							while ((linea = lector.readLine()) != null) {
								Pattern patron = Pattern.compile(
										"^(?<dni>[^;]+);(?<nombre>[^;]+);(?<fecha>[^;]+);(?<pais>[^;]+);(?<saldo>[^;]+)$");
								Matcher matcher = patron.matcher(linea);
								if (matcher.matches()) {
									String dni = matcher.group("dni");
									String nombre = matcher.group("nombre");
									String fecha = matcher.group("fecha");
									String pais = matcher.group("pais");
									double saldo = Double.parseDouble(matcher.group("saldo"));

									if (dni.equals(dnicif)) {

										Cuenta cuenta = new Cuenta(dni, nombre, fecha, pais, saldo);
										
										if (archivo.getFileName().toString().equals("caixa.txt")) {
											caixa.add(cuenta);
										} else if (archivo.getFileName().toString().equals("sabadell.txt")) {
											sabadell.add(cuenta);
										} else if (archivo.getFileName().toString().equals("santander.txt")) {
											santander.add(cuenta);
										} 
									}	
									
									
								} 
							}		
						
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (caixa.isEmpty()) {
					caixa.add(error);
					sabadell.add(error);
					santander.add(error);
				}
			
			if(caixa.get(0).getNombre().equals("error")) {
				System.out.println("Cuenta con DNI: "+ dnicif + " no existente");
			}
		
	
		/*dni*/elegida.setDni(dnicif);
		/*nombre*/if(caixa.get(0).getNombre().equals(sabadell.get(0).getNombre())&&
				   caixa.get(0).getNombre().equals(santander.get(0).getNombre())) 
				{
					elegida.setNombre(caixa.get(0).getNombre());
				}
		LocalDateTime fechaHora = LocalDateTime.now();
		/*pais*/if(caixa.get(0).getPais().equals("ES")||
				sabadell.get(0).getPais().equals("ES")||
				santander.get(0).getPais().equals("ES"))
				{
					elegida.setPais("ES");
					System.out.println(elegida.getNombre()+"\nBienvenido a nuestro sistema de gestion de cuentas");
			        System.out.println(fechaHora.format(DateTimeFormatter.ofPattern("HH:mm EEEE dd/MM/yyyy")));

				}else if(caixa.get(0).getNombre()!="error") {
					elegida.setPais("EX");//extranjera
					System.out.println(elegida.getNombre()+"\nWelcome to our account management system");	
					System.out.println(fechaHora.format(DateTimeFormatter.ofPattern("HH:mm EEEE MM/dd/yyyy", new Locale("en", "US"))));
				}
	
		/*fecha*/if(caixa.get(0).getFecha().equals(sabadell.get(0).getFecha())&&
				caixa.get(0).getFecha().equals(santander.get(0).getFecha())) 
		{
			elegida.setFecha(caixa.get(0).getFecha());
		}else {
//			Scanner sc = new Scanner(System.in);
			if(elegida.getPais().equals("ES")) {
				System.out.println("Indique que fecha de nacimiento es correcta: "+
					"1) "+caixa.get(0).getFecha()+" "+
					"2) "+sabadell.get(0).getFecha()+" "+
					"3) "+santander.get(0).getFecha());
			}else {
				System.out.println("Point us which birthdate is correct: "+
					"1) "+caixa.get(0).getFecha()+" "+
					"2) "+sabadell.get(0).getFecha()+" "+
					"3) "+santander.get(0).getFecha());
			}
			
			int opcion;
			do {
				opcion = Integer.parseInt(sc.nextLine());
				switch(opcion) {
				case 1 -> elegida.setFecha(caixa.get(0).getFecha());
				case 2 -> elegida.setFecha(sabadell.get(0).getFecha());
				case 3 -> elegida.setFecha(santander.get(0).getFecha());
				default -> System.out.printf("Esa no es una opción, por favor indique que fecha es correcta: \n"+
						"1) "+caixa.get(0).getFecha()+" "+
						"2) "+sabadell.get(0).getFecha()+" "+
						"3) "+santander.get(0).getFecha()+"\n");
				}
			}while(opcion<1 || opcion>3);
//			sc.close();
		}
		
		/*saldo*/elegida.setSaldo(caixa.get(0).getSaldo()+sabadell.get(0).getSaldo()+santander.get(0).getSaldo());
				
					
		
		
	
		}
		return elegida;

	}

	
	public static void FiltroFicheroProducto(Cuenta elegida)   {
		List<Oferta> ofertas = deFicheroAOferta(productos);
		List<Oferta> tarjetas = new ArrayList<Oferta>();
		String tarjeta = "0";
		if(elegida.getNombre()!="error") {
			int edad = Period.between(LocalDate.parse(elegida.getFecha(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.now()).getYears();
			//if(edad>=edadMinima && edad<=edadMaxima && elegida.getSaldo()>=saldoMinimo && elegida.getSaldo()<=saldoMaximo)
			for (Oferta oferta : ofertas) {
					if(oferta.getEdadMinima()<=edad
					&& oferta.getEdadMaxima()>=edad
					&& oferta.getSaldoMinimo()<=elegida.getSaldo()
					&& oferta.getSaldoMaximo()>=elegida.getSaldo()) {
						tarjetas.add(oferta);
					}
				}
			
			Optional<Oferta>  oferta = tarjetas.stream().max(Comparator.comparing(Oferta::getSaldoMinimo));
			if(oferta.isPresent()) {
				tarjeta = oferta.get().getTarjeta();
			}
		
			
			
			if(tarjeta!="0") {
				if(elegida.getPais().equals("ES")) {
					System.out.println("Esta es la tarjeta que le ofrecemos: " + tarjeta);
				}else if(elegida.getPais().equals("EX")) {
					System.out.println("This is the card we offer you: " + tarjeta);
				}
			}else {				
				if(elegida.getPais().equals("ES")) {
					System.out.println("Lo sentimos, no podemos ofrecerle ningún producto");
				}else if(elegida.getPais().equals("EX")) {
					System.out.println("Sorry, we can't offer you any products");
				}
				
	
			}
			/*try (BufferedReader lector = new BufferedReader(new FileReader(productos.toFile()))) {
			String linea;
			String oferta= "0";
			while ((linea = lector.readLine()) != null) {
				Pattern patron = Pattern.compile(
						"^(?<edadMinima>[^;]+);(?<edadMaxima>[^;]+);(?<saldoMinimo>[^;]+);(?<saldoMaximo>[^;]+);(?<producto>[^;]+)$");
				Matcher matcher = patron.matcher(linea);
				if (matcher.matches()) {
						int edadMinima =Integer.parseInt(matcher.group("edadMinima"));
						int edadMaxima =Integer.parseInt(matcher.group("edadMaxima"));
						double saldoMinimo = Double.parseDouble(matcher.group("saldoMinimo"));
						double saldoMaximo = Double.parseDouble(matcher.group("saldoMaximo"));
						String producto = (matcher.group("producto"));
						
						
						
						if(elegida.getPais()=="ES") {
							edad = Period.between(LocalDate.parse(elegida.getFecha(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.now()).getYears();
						}else {
							edad = Period.between(LocalDate.parse(elegida.getFecha(), DateTimeFormatter.ofPattern("MM/dd/yyyy")), LocalDate.now()).getYears();							
						} //esto resulta que no hace falta por que el formato de la fecha que nos dan es el mismo siempre
						if(edad>=edadMinima && edad<=edadMaxima && elegida.getSaldo()>=saldoMinimo && elegida.getSaldo()<=saldoMaximo) {
							
							oferta = producto;
						}
						
						
						
						
				}
			}
			
			//fuera del while
			if(oferta!="0") {
				if(elegida.getPais().equals("ES")) {
					System.out.println("Esta es la tarjeta que le ofrecemos: " + oferta);
				}else if(elegida.getPais().equals("EX")) {
					System.out.println("This is the card we offer you: " + oferta);
				}
			}else {				
						System.out.println("Lo sentimos, no podemos ofrecerle ningún producto");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		}
	}
	
	
	
	public static List<String> devolverLineasJava8(Path ruta){
		try {
			List<String> lineas = Files.readAllLines(ruta,Charset.forName("UTF-8"));
			return lineas;
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("No se puede acceder al fichero. Error en devolverLineasJava8");
			return null;
		}
	}
	public static List<Oferta> deFicheroAOferta(Path archivo) {
		List<Oferta> ofertas = new ArrayList<Oferta>();
		List<String> lineas = devolverLineasJava8(archivo);
		lineas.forEach(e->{
			Oferta oferta = new Oferta();
			String[] parte = e.split(";");
			oferta.setEdadMinima(Integer.parseInt(parte[0]));   
			oferta.setEdadMaxima(Integer.parseInt(parte[1]));   
			oferta.setSaldoMinimo(Integer.parseInt(parte[2]));   
			oferta.setSaldoMaximo(Integer.parseInt(parte[3]));   
			oferta.setTarjeta(parte[4]);   
			ofertas.add(oferta);
		});
		return ofertas;
	}
	
	// mostrar opcion a la que puede acceder con filtros
	
	
	// para el scanner probar libreria de metodos
	
	// si no se ofrece producto enviar mensaje 
	// si no hay cuenta con dni enviar mensaje el problema 

	
	// estructurar todo MEJOR :)
	public static void main(String[] args) {
		
		String dni = pedirDNI_CIF();
		Cuenta cliente = leerFicherosEnCarpetaTodasLasLineasAClaseCuenta(dni);
		FiltroFicheroProducto(cliente);
		
		
		    
		sc.close();
	}
}
