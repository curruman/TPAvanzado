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
//		File ruta = new File("C:\\tp");
	//	if (!ruta.exists()) {
		//	ruta.mkdirs();
	//	}
		//System.out.println(args[1]);
		File fileIn = new File("c:\\tp\\" + args[1]);// "C:\\tp\\salida.json"
		File fileIn2 = new File("c:\\tp\\" + args[0]); // "c:\tp\prestamos.json"
		File fileOut = new File("c:\\tp\\elegible_collateral.json");// "C:\\tp\\salida.json");
		
		try {
			DetallesCuenta[] cuentas = null;
			double precioCartera = 0;
			List<Double> precioCuenta = new ArrayList<>();
			List<ElegibleCollateral> elegible_collateral = new ArrayList<>();
			
			try {
				cuentas = mapper.readValue(fileIn, DetallesCuenta[].class);
				//------- Lectura de valores----
				List<ValoresSimplificados> valoresS = mapper.readValue(fileIn2, new TypeReference<List<ValoresSimplificados>>(){});				
				Map<String, ValoresSimplificados> mapaAcciones = new HashMap<>();

				ElegibleCollateral elegible = null;
				for (ValoresSimplificados valor : valoresS) {
					mapaAcciones.put(valor.getIsin(), valor);
				}
				for (DetallesCuenta cuenta : cuentas) {
					for (Positions pos : cuenta.getPositions()) {
						precioCartera += pos.getQuantity()
								* Double.parseDouble(String.valueOf((mapaAcciones.get(pos.getId())).getPrice()));
					}
					if (precioCartera < cuenta.getAmount()) {
						
						elegible = new ElegibleCollateral();
						elegible.setId(cuenta.getId());
						elegible.setCreditpolicy(cuenta.getCreditpolicy());
						elegible.setAmount(cuenta.getAmount());
						elegible.setValorCartera(precioCartera);
						elegible_collateral.add(elegible);
					} 
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

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
