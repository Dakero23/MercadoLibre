package co.com.dakero.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import co.com.dakero.entidad.Cupon;
import co.com.dakero.servicio.ICuponService;
import co.com.dakero.servicio.IRateLimitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api")
@Tag(name = "Dakero", description = "API prueba Mercado Libre")
public class CuponController {

	@Autowired
	private ICuponService cuponService;

	@Autowired
	private IRateLimitService bucket;

	@Operation(summary = "Consumo Cupon", 
			   description = "consumo de cupon con los datos ingresados", 
			   tags = {"Consulta Cupon" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Operación Exitosa"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "404", description = "Not Found"),
			@ApiResponse(responseCode = "429", description = "TOO_MANY_REQUESTS"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })

	@PostMapping(value = "coupon", consumes = { "application/json" })
	public ResponseEntity<Object> consumirCupon(@Validated @RequestBody Cupon cupon) {
		boolean okConsum = bucket.getBucket().tryConsume(1);
		if (okConsum) {
			Map<String, Float> listaItems = cuponService.consultarPrecio(cupon.getItem_ids());

			Map<String, Float> listaItemsCupon = cuponService.validarLista(listaItems, cupon.getAmount());
			Cupon cuponRespuesta = new Cupon();

			List<String> list = cuponService.calculate(listaItemsCupon, cupon.getAmount());
			String[] array = list.stream().toArray(String[]::new);

			cuponRespuesta.setItem_ids(array);
			cuponRespuesta.setAmount(cuponService.amount(listaItemsCupon));

			return new ResponseEntity<>(cuponRespuesta, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.TOO_MANY_REQUESTS);
		}
	}

	
	
	
	@Operation(summary = "Consumo Cupon listaPruebas", description = "consumo de cupon con lista de pruebas generadas desde un arreglo fijo en codigo", tags = {
			"Consulta Cupon Test" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Operación Exitosa"),
			@ApiResponse(responseCode = "400", description = "Bad Request" , headers = { }),
			@ApiResponse(responseCode = "404", description = "TOO_MANY_REQUESTS"),
			@ApiResponse(responseCode = "429", description = "Not Found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })

	@PostMapping(value = "/test/coupon", consumes = { "application/json" })
	public ResponseEntity<Object> consumirCuponPrueba(@Validated @RequestBody Cupon cupon) {

		boolean okConsum = bucket.getBucket().tryConsume(1);

		if (okConsum) {
			Map<String, Float> listaItems = cuponService.listaPrueba(cupon.getItem_ids());
			List<String> x = cuponService.calculateTwo(listaItems, cupon.getAmount());

			Gson gson = new Gson();
			Cupon t = gson.fromJson(x.get(0), Cupon.class);
			return new ResponseEntity<>(t, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.TOO_MANY_REQUESTS);
		}
	}

}
