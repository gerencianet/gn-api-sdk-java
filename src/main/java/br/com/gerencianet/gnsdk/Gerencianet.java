package br.com.gerencianet.gnsdk;

/**
 * This class extends Endpoins class.
 * @author Filipe Mata
 */

import java.util.Map;

import org.json.JSONObject;

public class Gerencianet extends Endpoints{
	public Gerencianet(JSONObject options) throws Exception {
		super(options);
	}
	
	public Gerencianet(Map<String, Object> options) throws Exception {
		super(options);
	}	
}
