package com.tp2.avanzado;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class App {
	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		File ruta = new File("C:\\tp");
		if (!ruta.exists()) {
			ruta.mkdirs();
		}
		//System.out.println(args[1]);
		File fileIn = new File(ruta + "\\" + args[1]);// "C:\\tp\\salida.json");
		File fileOut = new File(ruta + "\\elegible_collateral.json");// "C:\\tp\\salida.json");
		//File filePrestamos = new File(ruta + "\\prestamos.json");
		URL url;
		try {
			//System.out.println(args[0]);
			url = new URL(args[0]);
			DetallesCuenta[] cuentas = null;
			//List<ValoresSimplificados> valoresS = new ArrayList<>();
			//ValoresSimplificados[] valoresS = null;
			double precioCartera = 0;
			List<Double> precioCuenta = new ArrayList<>();
			List<ElegibleCollateral> elegible_collateral = new ArrayList<>();
			// Set<String> claves=null;
			try {
				//cuentas = mapper.readValue(filePrestamos, DetallesCuenta[].class);
				cuentas = mapper.readValue(url, DetallesCuenta[].class);
				//valoresS = mapper.readValue(fileIn, ValoresSimplificados[].class);
				List<ValoresSimplificados> valoresS = mapper.readValue(fileIn, new TypeReference<List<ValoresSimplificados>>(){});
				
				Map<String, ValoresSimplificados> mapaAcciones = new HashMap<>();
				/*JavaType setType = mapper.getTypeFactory().constructCollectionType(List.class, ValoresSimplificados.class);
				JavaType stringType = mapper.getTypeFactory().constructType(String.class);
				JavaType mapType = mapper.getTypeFactory().constructMapType(ArrayList.class, stringType, setType);

				String outputJson = mapper.readValue(fileIn, mapType);
				System.out.println(outputJson);
				*/
				ElegibleCollateral elegible = null;
				for (ValoresSimplificados valor : valoresS) {
					//System.out.println(valor.getIsin());
					mapaAcciones.put(valor.getIsin(), valor);
				}
				for (DetallesCuenta cuenta : cuentas) {
					for (Positions pos : cuenta.getPositions()) {
						precioCartera += pos.getQuantity()
								* Double.parseDouble(String.valueOf((mapaAcciones.get(pos.getId())).getPrice()));
					}
					if (precioCartera < cuenta.getAmount()) {
						//System.out.println("Elegible Collateral: " + cuenta.getId());
						elegible = new ElegibleCollateral();
						elegible.setId(cuenta.getId());
						elegible.setCreditpolicy(cuenta.getCreditpolicy());
						elegible.setAmount(cuenta.getAmount());
						elegible.setValorCartera(precioCartera);
						elegible_collateral.add(elegible);
					} /*else {
						System.out.println("Prestamo garantizado para la cuenta: " + cuenta.getId());
					}*/
					precioCuenta.add(precioCartera);
					precioCartera = 0;
				}

				mapper.enable(SerializationFeature.INDENT_OUTPUT);
				mapper.writeValue(fileOut, elegible_collateral);

			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
