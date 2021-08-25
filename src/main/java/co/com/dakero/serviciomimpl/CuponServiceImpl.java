package co.com.dakero.serviciomimpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

//import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.com.dakero.entidad.Cupon;
import co.com.dakero.entidad.Item;
import co.com.dakero.exception.ModeloNotFoundException;
import co.com.dakero.servicio.ICuponService;

@Service
public class CuponServiceImpl implements ICuponService {

	/**
	 * 
	 * @param items
	 * @param amount
	 * @return
	 */
	public Float amount(Map<String, Float> items) {

		float amountSave;
		String sum;

		DecimalFormat df = new DecimalFormat("#.##");

		sum = df.format(items.entrySet().stream().mapToDouble(w -> w.getValue()).sum());
		amountSave = Float.valueOf(sum);

		return amountSave;
	}

	/**
	 * 
	 * @param items
	 * @param amount
	 * @return
	 */
	@Override
	public List<String> calculate(Map<String, Float> items, Float amount) {
//		validarLista(items,amount);
		List<String> r = items.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getKey)
				.collect(Collectors.toList());
		return r;
	}

	/**
	 * Metodo que calcula los item que pueden ser usados dentro de un cupon
	 * 
	 * @param items
	 * @param amount
	 * @return
	 */
	public List<String> calculateTwo(Map<String, Float> items, Float amount) {
		List<String> lista = new ArrayList<>();
		validarCupon(items, amount);

		Cupon cupon = comprobarlista(items, amount);
		String t = new Gson().toJson(cupon);
		lista.add(t);
		return lista;
	}

	/**
	 * Metodo encargado de recorrer la lista de item y buscar una combinacion con la
	 * suma de los mismo si pasar el monto ingresado mediante forma clasica 1.7
	 * 
	 * @param items
	 * @param amount
	 */
	private Cupon comprobarlista(Map<String, Float> items, Float amount) {
		Float sumTotal = 0f;
//		Float temp = 0f;
//		float monto = 500f;
		List<String> list = new ArrayList<>();
		HashMap<String, Float> listItems;
		Cupon cupon = new Cupon();
//		
//		for (Map.Entry<String, Float> e : items.entrySet()) {
//			temp = e.getValue() + sumTotal;
//			if (sumTotal < monto && temp < monto) {
//				sumTotal = temp;
//				list.add(e.getKey());
//			}
//			temp = 0f;
//		}
		listItems = validarLista(items, amount);
		list = calculate(listItems, amount);
		sumTotal = amount(listItems);
		cupon.setItem_ids(list.toArray(new String[list.size()]));
		cupon.setAmount(sumTotal);
		return cupon;
	}

	/**
	 * Metodo para verificar si el cupon puede consumirse con los item ingresados
	 * 
	 * @param items
	 * @param amount
	 */

	private void validarCupon(Map<String, Float> items, Float amount) {

		Float minContCount = items.entrySet().stream().map(m -> m.getValue()).min((a, b) -> a.compareTo(b)).get();
		if (minContCount > amount) {
			throw new ModeloNotFoundException(
					"El monto minimo de los items no es suficiente para poder utilizar el cupon");
		}
	}

	/**
	 * Metodo encargado de consultar precios en el servicio Expuesto
	 * 
	 * @return
	 */
	public HashMap<String, Float> consultarPrecio(String[] items) {

		HashMap<String, Float> listPrecios = new HashMap<>();
		for (int i = 0; i < items.length; i++) {
			ResponseEntity<String> response = consumoWebService(items[i]);
			Item item = procesarRespuesta(response);
			listPrecios.put(item.getId(), item.getPrice());
		}
		return listPrecios;
	}

	/**
	 * Metodo encargado de procesar la respuesta obtenida por el consumo del
	 * servicio
	 * 
	 * @return
	 */
	private Item procesarRespuesta(ResponseEntity<String> response) {
		Item item = new Item();
		if (response.getStatusCode() == HttpStatus.OK) {
			Gson gson = new GsonBuilder().create();

			item = gson.fromJson(response.getBody(), Item.class);

		}
		return item;
	}

	/**
	 * Metodo encargado de consumir servicio de items de mercadolibre retornando la
	 * respuesta y la trama capturada
	 * 
	 * @param item
	 * @return
	 */
	private ResponseEntity<String> consumoWebService(String item) {
		RestTemplate restTemplate = new RestTemplate();
		String endpoint = "https://api.mercadolibre.com/items/" + item;
		ResponseEntity<String> response;
//		try {
		response = restTemplate.getForEntity(endpoint, String.class);
//		} catch (HttpStatusCodeException ex) {
//			response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(),
//					ex.getStatusCode());
//		}
		return response;
	}

	/**
	 * 
	 * @param items
	 * @param amount
	 * @return
	 */
	public HashMap<String, Float> validarLista(Map<String, Float> items, Float amount) {
		// Validamos cupon para saber si recorremos o no la lista
		validarCupon(items, amount);

		HashMap<String, Float> t = new HashMap<>();
		Consumer<Map.Entry<String, Float>> consumer;
		consumer = entry -> {
			double c = t.entrySet().stream().map(Map.Entry::getValue).mapToDouble(w -> w).sum();
			c = c + entry.getValue();
			if (c <= amount) {
				t.put(entry.getKey(), entry.getValue());
			}
		};

		items.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(consumer);

		return t;

	}

	/**
	 * Metodo con lista de pruebas
	 * 
	 * @return
	 */
	public HashMap<String, Float> listaPrueba(String[] items) {
		HashMap<String, Float> listaPrueba = new HashMap<>();

		listaPrueba.put("MLA1", 100f);
		listaPrueba.put("MLA2", 210f);
		listaPrueba.put("MLA3", 260f);
		listaPrueba.put("MLA4", 80f);
		listaPrueba.put("MLA5", 90f);

		HashMap<String, Float> temp = new HashMap<>();

		Consumer<String> consumer;
		consumer = x -> {
			if (listaPrueba.containsKey(x)) {
				temp.put(x, listaPrueba.get(x));
			}
		};
		Arrays.stream(items).map(s -> s).forEach(consumer);
		
		if (temp.isEmpty()) {
			throw new ModeloNotFoundException(
					"Los elementos ingresados no Existen");
		}

		return temp;
	}

}
