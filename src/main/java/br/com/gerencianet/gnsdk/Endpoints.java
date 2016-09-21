package br.com.gerencianet.gnsdk;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * This is the mains class of Gerencianet SDK JAVA. It's responsible to instance an APIRequester,
 * send the right data to a given endpoint, and return a response to SDK client.
 * @author Filipe Mata
 *
 */

public class Endpoints {
	private APIRequest requester;
	private Config config;
	
	public Endpoints(JSONObject options) throws Exception 
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream configFile = classLoader.getResourceAsStream("config.json");
		JSONTokener tokener = new JSONTokener(configFile);
		JSONObject config = new JSONObject(tokener);
		configFile.close();
		this.config = new Config(options, config);
	}
	
	public Endpoints(Config config, APIRequest request) throws Exception 
	{
		this.config = config;
		this.requester = request;
	}
	
	public Endpoints(Config config){
		this.config = config;
	}
	
	public Endpoints(Map<String, Object> options) throws Exception{
		JSONObject credentials = (JSONObject) JSONObject.wrap(options);
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream configFile = classLoader.getResourceAsStream("config.json");
		JSONTokener tokener = new JSONTokener(configFile);
		JSONObject config = new JSONObject(tokener);
		configFile.close();
		this.config = new Config(credentials, config);
	}
		
	public APIRequest getRequester() {
		return requester;
	}
	
	public JSONObject call(String endpoint, Map<String, String> params, JSONObject body) throws Exception{
		return kernelCall(endpoint, params, body);
	}
	
	public Map<String, Object> call(String endpoint, Map<String, String> params, Map<String, Object> mapBody) throws Exception{
		JSONObject body = (JSONObject) JSONObject.wrap(mapBody);
		JSONObject response = kernelCall(endpoint, params, body);
		return response.toMap();
	}
	
	private JSONObject kernelCall(String endpointName, Map<String, String> params, JSONObject body) throws Exception{
		JSONObject endpoints = this.config.getEndpoints();
		if(!endpoints.has(endpointName))
			throw new Exception("nonexistent endpoint");
		
		JSONObject endpoint = (JSONObject)endpoints.get(endpointName);
		String route = getRoute(endpoint, params);
		route += getQueryString(params);
		if(this.requester == null)
			requester = new APIRequest(endpoint.get("method").toString(), route, body, this.config);
		JSONObject response = this.requester.send();
		this.requester = null;
		
		return response;
	}
	
	private String getQueryString(Map<String, String> params) throws UnsupportedEncodingException {
		Set<Entry<String, String>> set = params.entrySet();
    	String query="";
    	for(Entry<String, String> entry : set){
    		if(!query.isEmpty())query +="&";
    		else query +="?";
    		query += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),"UTF-8");
    	}
    	return query;
	}
	
	private String getRoute(JSONObject endpoint, Map<String, String> params) 
	{
		Pattern pattern = Pattern.compile("/:(\\w+)/");
    	String route = endpoint.get("route").toString();
    	route += "/";
    	Matcher matcher = pattern.matcher(route);
    	while(matcher.find()){
    		String value = route.substring(matcher.start()+2, matcher.end()-1); 
    		if(params.containsKey(value)){
    			route = route.replace(":"+value, params.get(value));
    			params.remove(value);
    			matcher = pattern.matcher(route);
    		}    		
    	}
    	route = route.substring(0, route.length()-1);
    	return route;
	}
}
