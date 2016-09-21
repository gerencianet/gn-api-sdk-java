package br.com.gerencianet.gnsdk.exceptions;

/** This class extends to Exception and is developed to deal with authenticate errors
 * @author Filipe Mata
 */

public class AuthorizationException extends Exception 
{
	private static final long serialVersionUID = 1L;

	public AuthorizationException() {
		super();
	}
	
	@Override
	public String getMessage() {
		return "Authorization Error: Client_id or Client_secret are wrong";
	}
}
