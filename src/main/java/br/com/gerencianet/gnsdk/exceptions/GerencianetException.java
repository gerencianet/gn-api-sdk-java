package br.com.gerencianet.gnsdk.exceptions;

import org.json.JSONObject;

/** This class extends Exception and is developed to deal with Gerencianet API errors
 * @author Filipe Mata
 */

public class GerencianetException extends Exception {
	private static final long serialVersionUID = 1L;
	private int code = 0;
	private String error;
	private String errorDescription;
	
	public GerencianetException(JSONObject response) {
		String message = "";
		if(response.has("error_description"))
		{
			if(response.get("error_description").getClass().getSimpleName().equals("JSONObject")){
				JSONObject errorDescription = response.getJSONObject("error_description");
				if(errorDescription.has("message"))
					message = errorDescription.getString("message");
				else message = response.get("error_description").toString();
				
				if(errorDescription.has("property"))
					message += ":" + errorDescription.get("property");	
			}
			else
				message = response.get("error_description").toString();
			
			if(response.has("code"))
				this.code = Integer.parseInt(response.get("code").toString());
			this.error = response.get("error").toString();
			this.errorDescription = message;
		}
	}
	
	public String getError() {
		return error;
	}
	
	public String getErrorDescription() {
		return errorDescription;
	}
	public int getCode() {
		return code;
	}
	
	@Override
	public String getMessage() {
		if(this.code != 0)
			return "Error " + this.code + " - " + this.error + ": " + this.errorDescription;
		else return "Error: " + this.errorDescription;
	}
}

