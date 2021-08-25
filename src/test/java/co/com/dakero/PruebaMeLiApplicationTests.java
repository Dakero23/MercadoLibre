package co.com.dakero;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.com.dakero.exception.ModeloNotFoundException;
import co.com.dakero.servicio.ICuponService;

@SpringBootTest
class PruebaMeLiApplicationTests {

	@Autowired
	private ICuponService cuponService;
	HashMap<String, Float> items;

	@BeforeEach
	public void crearLista() {

		System.out.println("Llena lista items Prueba");
		items = new HashMap<>();
		items.put("MLA1", 100f);
		items.put("MLA2", 210f);
		items.put("MLA3", 260f);
		items.put("MLA4", 80f);
		items.put("MLA5", 90f);
	}

	@Test
	void TestAmount() {
//		System.out.println("Prueba Monto");
		Float resultadoReal = cuponService.amount(this.items);
		Float resultadoEsperado = 740f;

		assertEquals(resultadoEsperado, resultadoReal);// true

	}

	@Test
	void TestItems() {
//		System.out.println("Buscar items que se ajusten al cupon");
		HashMap<String, Float> temp = cuponService.validarLista(this.items, 80f);
		Float resultadoReal = cuponService.amount(temp);
		Float resultadoEsperado = 80f;

		assertEquals(resultadoEsperado, resultadoReal);// true
	}

	@Test
	void TestItemsException() {
//		System.out.println("Valida si el monto minimo es inferior al cupon");
		try {
			cuponService.validarLista(this.items, 60f);
		} catch (ModeloNotFoundException e) {
			assertTrue(e.getMessage()
					.contains("El monto minimo de los items no es suficiente para poder utilizar el cupon"));
		}
	}

	@Test
	void TestValidarLista() {
//		System.out.println("Valida si el monto minimo es inferior al cupon");
		try {
			cuponService.validarLista(this.items, 60f);
		} catch (ModeloNotFoundException e) {
			assertTrue(e.getMessage()
					.contains("El monto minimo de los items no es suficiente para poder utilizar el cupon"));
		}
	}

	@Test
	void TestconsultarPrecio() {
		String[] items_ids = { "MCO583857237", "MCO503785357" };
		HashMap<String, Float> listaPrecio = cuponService.consultarPrecio(items_ids);
		String key = "MCO583857237";
		assertTrue(listaPrecio.get(key) == 151900.0f);
		String key2 = "MCO503785357";
		assertTrue(listaPrecio.get(key2) == 109900.0f);
	}

	@Test
	void Testcalculate() {
		try {
			String[] items_ids = { "MCO583857237", "MCO503785357" };
			HashMap<String, Float> listaPrecio = cuponService.consultarPrecio(items_ids);
			Float amount = 200f;
			HashMap<String, Float> ListaFinal = cuponService.validarLista(listaPrecio, amount);
			List<String> x = cuponService.calculate(ListaFinal, amount);
			Float mountAct = cuponService.amount(ListaFinal);

			assertTrue(x.contains("MCO583857237") && mountAct == 151900.0);
		} catch (ModeloNotFoundException e) {
			assertTrue(e.getMessage()
					.contains("El monto minimo de los items no es suficiente para poder utilizar el cupon"));
		}

	}

}
