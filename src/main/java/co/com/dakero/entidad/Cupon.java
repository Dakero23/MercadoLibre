package co.com.dakero.entidad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Cupon {

	@Schema(description = "Arreglo de items enviados para validar presio", example = "[\"MCO583857237\",\"MCO503785357\"]", required = true)
	private String[] item_ids;

	@Schema(description = "Monto designado del cupon", example = "500", required = true)
	private float amount;

}
