package co.com.dakero.entidad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Dakero
 *
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Item {

	private String id;
	private String title;
	private float price;
	private String site_id;

}
