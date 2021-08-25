package co.com.dakero.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICuponService {
	/**
	 * Metodo que calcula los item que pueden ser usados
	 * dentro de un cupon
	 * @param items
	 * @param amount
	 * @return
	 */
	List<String> calculate(Map<String, Float> items, Float amount);

	/**
	 * Segun documentación metodo encargado de consultar el precio de un producto
	 * mediante la api expuesta en la URL https://api.mercadolibre.com/items/
	 * 
	 * @param items
	 * @return
	 */
	HashMap<String, Float> consultarPrecio(String[] items);

	/**
	 * Listas de variables encontradas en el documento entregrado
	 * 
	 * @return
	 */
	HashMap<String, Float> listaPrueba(String[] items);
	
	
	/**
	 * Metodo que devuelve la suma de los items del arreglo
	 * @param items
	 * @return
	 */
	Float amount(Map<String, Float> items);
	
	/**
	 *  Metodo encargado de crear un Map con los posibles items 
	 *  necesarios para consumir el cupon
	 * @param items
	 * @param amount
	 * @return
	 */
	HashMap<String, Float> validarLista(Map<String, Float> items, Float amount);
	
	
	/**
	 * Segunda posible solucion manteniendo firma inicial en el cual se devuelve
	 * los parametros en una lista String para ser casteados posteriormente
	 * a la clase cupon y de hay extraer los campos de amount y items_ids
	 * @param items
	 * @param amount
	 * @return
	 */
	List<String> calculateTwo(Map<String, Float> items, Float amount);
	
	
	
}
