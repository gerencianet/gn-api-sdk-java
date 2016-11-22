package br.com.gerencianet.gnsdk;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import java.io.IOException;

import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test for Config class
 * @author Filipe Mata
 */

public class ConfigTest {
	
	@Test(expected=Exception.class)
	public void shouldThrowsExceptionWhenEndpointsDoesntExist() throws Exception{
		JSONObject configurations = Mockito.mock(JSONObject.class);
		new Config(new JSONObject(), configurations);
	}
	
	@Test(expected=Exception.class)
	public void shouldThrowsExceptionWhenUrlsDoesntExist() throws Exception{
		JSONObject configurations = Mockito.mock(JSONObject.class);
		when(configurations.has("ENDPOINTS")).thenReturn(true);
		new Config(new JSONObject(), configurations);
	}
	
	@Test
	public void shouldGetPropertiesCorrectly() throws Exception{
		JSONObject configurations = Mockito.mock(JSONObject.class);
		when(configurations.has("ENDPOINTS")).thenReturn(true);
		when(configurations.has("URL")).thenReturn(true);
		
		JSONObject endpointSet = mock(JSONObject.class);
		when(endpointSet.has("authorize")).thenReturn(true);
		when(endpointSet.has("cancelParcel")).thenReturn(true);
		when(configurations.get("ENDPOINTS")).thenReturn(endpointSet);
		
		JSONObject urlSet = mock(JSONObject.class);
		when(urlSet.has("production")).thenReturn(true);
		when(urlSet.has("sandbox")).thenReturn(true);
		when(configurations.get("URL")).thenReturn(urlSet);
		try {
			Config config = new Config(new JSONObject(), configurations);
			JSONObject urls = config.getUrls();
			JSONObject endpoins = config.getEndpoints();
			
			assertNotNull(urls);
			assertNotNull(endpoins);
			assertTrue(urls.has("production") && urls.has("sandbox")); 
			assertTrue(endpoins.has("authorize") && endpoins.has("cancelParcel")); 
			
		} catch (IOException e) {
			fail("The file config.json doesn't exist or is not in the right fouder.");
		}
	}
	
	@Test
	public void shouldBuildOptionsSuccessfully() throws Exception
	{
		JSONObject options = new JSONObject();
		options.put("client_id", "123");
		options.put("client_secret", "456");
		options.put("partner_token", "ptteste");
		options.put("debug", true);
		options.put("url", "http://filipegnapi.gerencianet.com.br:4400");

		JSONObject configurations = new JSONObject("{\"ENDPOINTS\": {}, \"URL\": {}}");
		try {
			Config configuration = new Config(options, configurations);
			
			JSONObject config = configuration.getOptions();
			assertTrue(config.has("sandbox"));
			assertTrue(config.has("debug"));
			assertTrue(config.has("clientId"));
			assertTrue(config.has("clientSecret"));
			assertTrue(config.has("baseUri"));
			assertTrue(config.has("partnerToken"));
			
			assertTrue(config.get("sandbox").equals(false));
			assertTrue(config.get("debug").equals(true));
			assertTrue(config.get("clientId").equals("123"));
			assertTrue(config.get("clientSecret").equals("456"));
			assertTrue(config.get("baseUri").equals("http://filipegnapi.gerencianet.com.br:4400"));
			assertTrue(config.get("partnerToken").equals("ptteste"));
		} catch (IOException e) {
			fail("The file config.json doesn't exist or is not in the right fouder.");
		}
	}
	
	@Test
	public void shouldBuildOptionsWithoutUrl() throws Exception
	{
		JSONObject options = new JSONObject();
		options.put("client_id", "456");
		options.put("client_secret", "123");
		options.put("sandbox", true);
		JSONObject configurations = Mockito.mock(JSONObject.class);
		when(configurations.has("ENDPOINTS")).thenReturn(true);
		when(configurations.has("URL")).thenReturn(true);
		
		JSONObject endpointSet = mock(JSONObject.class);
		when(endpointSet.has("authorize")).thenReturn(true);
		when(endpointSet.has("cancelParcel")).thenReturn(true);
		when(configurations.get("ENDPOINTS")).thenReturn(endpointSet);
		
		JSONObject urlSet = mock(JSONObject.class);
		when(urlSet.has("production")).thenReturn(true);
		when(urlSet.has("sandbox")).thenReturn(true);
		when(urlSet.getString("production")).thenReturn("https://gerencianet.com.br");
		when(urlSet.getString("sandbox")).thenReturn("https://sandbox.gerencianet.com.br");
		when(configurations.get("URL")).thenReturn(urlSet);
		try {
			Config configuration = new Config(options, configurations);
			
			JSONObject config = configuration.getOptions();
			assertTrue(config.has("sandbox"));
			assertTrue(config.has("debug"));
			assertTrue(config.has("clientId"));
			assertTrue(config.has("clientSecret"));
			assertTrue(config.has("baseUri"));
			
			assertTrue(config.get("sandbox").equals(true));
			assertTrue(config.get("debug").equals(false));
			assertTrue(config.get("clientId").equals("456"));
			assertTrue(config.get("clientSecret").equals("123"));
			assertTrue(config.get("baseUri").equals("https://sandbox.gerencianet.com.br"));
		} catch (IOException e) {
			fail("The file config.json doesn't exist or is not in the right fouder.");
		}
	}
	
	public void shouldBuildOptionsWithoutCredentials() throws Exception
	{
		JSONObject options = new JSONObject();
		options.put("sandbox", false);
		JSONObject configurations = new JSONObject("{\"ENDPOINTS\": {}, \"URL\": {}}");
		try {
			Config configuration = new Config(options, configurations);
			
			JSONObject config = configuration.getOptions();
			assertTrue(config.has("sandbox"));
			assertTrue(config.has("debug"));
			assertFalse(config.has("clientId"));
			assertFalse(config.has("clientSecret"));
			assertTrue(config.has("baseUri"));
			
			assertTrue(config.get("sandbox").equals(false));
			assertTrue(config.get("debug").equals(false));
			assertTrue(config.get("baseUri").equals("https://sandbox.gerencianet.com.br"));
		} catch (IOException e) {
			fail("The file config.json doesn't exist or is not in the right fouder.");
		}
	}
	
	@Test
	public void shouldReturnVersionSuccessfully()
	{
		String version = Config.getVersion();
		assertTrue(version.length() > 0);
	}
}
