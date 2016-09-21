package br.com.gerencianet.gnsdk;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.gerencianet.gnsdk.exceptions.AuthorizationException;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

/**
 * Test for Request Class
 * @author Filipe Mata
 */

public class RequestTest {
	private Request request;
	
	@Mock
	private HttpURLConnection client;

    @Mock
    private JSONObject options;
    
    @Test
    public void createHeadderCorrectly(){
    	MockitoAnnotations.initMocks(this);
		try {
			request = new Request("POST", client);
			request.addHeader("test1", "hello");
			request.addHeader("test2", "hello again");
			verify(client, times(1)).setRequestProperty("test1", "hello");
			verify(client, times(1)).setRequestProperty("test2", "hello again");
		} catch (IOException e) {
			Assert.fail("Teste falhou ao carregar as configuracoes do arquivo config.json");
		}
    }
    
	@Test(expected=AuthorizationException.class) 
	public void shouldThrowExceptionForUnauthorized() throws AuthorizationException{
		MockitoAnnotations.initMocks(this);
		when(client.getRequestMethod()).thenReturn("POST");
		try {
			request = new Request("post", client);
			JSONObject json = new JSONObject();
			JSONObject body = new JSONObject();
			body.put("grant_type", "client_credentials");
			json.put("json", body);
			
			Mockito.when(client.getOutputStream()).thenReturn(new ByteArrayOutputStream());
			Mockito.when(client.getResponseCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
			try {
				request.send(json);
			} catch (GerencianetException e) {
				Assert.fail("Throwed wrong exception. Should throw Authorize Exception.");
			}
		} catch (IOException e) {
			Assert.fail("Teste falhou ao carregar as configuracoes do arquivo config.json");
		}
	}
	
	@Test(expected=GerencianetException.class) 
	public void shouldThrowExceptionForServerError() throws GerencianetException {
		MockitoAnnotations.initMocks(this);
		when(client.getRequestMethod()).thenReturn("POST");
		try {
			request = new Request("post", client);
			JSONObject body = new JSONObject();
			Mockito.when(client.getOutputStream()).thenReturn(new ByteArrayOutputStream());
			Mockito.when(client.getResponseCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);
			JSONObject error = new JSONObject();
			error.put("code", 500);
			error.put("error_description", "internal error happenned");
			error.put("error", "server internal error");
			InputStream stream = new ByteArrayInputStream(error.toString().getBytes(StandardCharsets.UTF_8));
			Mockito.when(client.getErrorStream()).thenReturn(stream);
			
			try {
				request.send(body);
			} catch (AuthorizationException e) {
				Assert.fail("Throwed wrong exception. Should throw Authorize Exception.");
			}
		} catch (IOException e) {
			Assert.fail("Teste falhou ao carregar as configuracoes do arquivo config.json");
		}
	}

    @Test
    public void shouldSendHttpRequestSuccessfully()
    {
    	MockitoAnnotations.initMocks(this);
    	when(client.getRequestMethod()).thenReturn("GET");
    	try {
			request = new Request("get", client);
		} catch (IOException e) {
			Assert.fail("Teste falhou ao carregar as configuracoes do arquivo config.json");
		}
    	
		try{
	    	Mockito.when(client.getOutputStream()).thenReturn(new ByteArrayOutputStream());
			Mockito.when(client.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			JSONObject success = new JSONObject();
			success.put("code", 200);
			success.put("access_token", "token");
			success.put("expires_in", 500);
			success.put("token_type", "bearer");
			InputStream stream = new ByteArrayInputStream(success.toString().getBytes(StandardCharsets.UTF_8));
			Mockito.when(client.getInputStream()).thenReturn(stream);

			JSONObject response = request.send(new JSONObject());
			
			Assert.assertTrue(response.length() == 4);
			Assert.assertTrue(response.has("code"));
			Assert.assertTrue(response.getInt("code") == 200);
			Assert.assertTrue(response.has("access_token"));
			Assert.assertTrue(response.get("access_token").equals("token"));
			Assert.assertTrue(response.has("expires_in"));
			Assert.assertTrue(response.getInt("expires_in") == 500);
			Assert.assertTrue(response.has("token_type"));
			Assert.assertTrue(response.get("token_type").equals("bearer"));
		}
		catch(Exception e){
			Assert.fail("Souldn't throw any exception.");
		}

    }
}
