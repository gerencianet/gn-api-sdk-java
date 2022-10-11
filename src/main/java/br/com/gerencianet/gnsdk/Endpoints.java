package br.com.gerencianet.gnsdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * This is the mains class of Gerencianet SDK JAVA. It's responsible to instance
 * an APIRequester,
 * send the right data to a given endpoint, and return a response to SDK client.
 * 
 * @author Filipe Mata
 */

public class Endpoints {
	private APIRequest requester;
	private Config config;

	/**
	 * Constructor JSONObject
	 * 
	 * @param options Objeto JSON com as definições da API
	 * @throws Exception
	 * @author Jessica Gava
	 */
	public Endpoints(JSONObject options) throws Exception {
		JSONObject config = readJSONFile();
		this.config = new Config(options, config);
	}

	/**
	 * Constructor Map<String, Object>
	 * 
	 * @param options Map com as definiçõess da API
	 * @throws Exception
	 * @author Jessica Gava
	 */
	public Endpoints(Map<String, Object> options) throws Exception {
		JSONObject credentials = (JSONObject) JSONObject.wrap(options);
		JSONObject config = readJSONFile();
		this.config = new Config(credentials, config);
	}

	public Endpoints(Config config, APIRequest request) throws Exception {
		this.config = config;
		this.requester = request;
	}

	public Endpoints(Config config) {
		this.config = config;
	}

	/**
	 * Read the JSON settings file
	 * 
	 * @return JSONObject 
	 * @throws IOException
	 * @author Jessica Gava
	 */
	protected JSONObject readJSONFile() throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream configFile = classLoader.getResourceAsStream("config.json");
		JSONTokener tokener = new JSONTokener(configFile);
		JSONObject config = new JSONObject(tokener);
		configFile.close();
		return config;
	}

	public APIRequest getRequester() {
		return requester;
	}

	public JSONObject call(String endpoint, Map<String, String> params, JSONObject body) throws Exception {
		return kernelCall(endpoint, params, body);
	}

	public String callString(String endpoint, Map<String, String> params, JSONObject body) throws Exception {
		return kernelCallString(endpoint, params, body);
	}

	public Map<String, Object> call(String endpoint, Map<String, String> params, Map<String, Object> mapBody)
			throws Exception {
		JSONObject body = (JSONObject) JSONObject.wrap(mapBody);
		JSONObject response = kernelCall(endpoint, params, body);
		return response.toMap();
	}

	protected HashMap<String, JSONObject> getRoute(JSONObject parEndpointsObj, String parEndpointName) {
		HashMap<String, JSONObject> hash = new HashMap<String, JSONObject>();
		for (String ApiName : parEndpointsObj.keySet()) {
			JSONObject urls = (JSONObject) ((JSONObject) parEndpointsObj.get(ApiName)).get("URL");
			JSONObject endpoints = (JSONObject) ((JSONObject) parEndpointsObj.get(ApiName)).get("ENDPOINTS");
			if (endpoints.has(parEndpointName)) {
				hash.put("URL", urls);
				hash.put("ENDPOINTS", endpoints);
			}
		}
		return hash;
	}

	private JSONObject kernelCall(String endpointName, Map<String, String> params, JSONObject body) throws Exception {
		JSONObject endpoints = this.config.getEndpoints();
		HashMap<String, JSONObject> api = getRoute(endpoints, endpointName);
		if (!api.containsKey("ENDPOINTS"))
			throw new Exception("nonexistent endpoint");

		JSONObject api_url = (JSONObject) api.get("URL");
		this.config.setURLs(api_url);

		JSONObject api_endpoints = (JSONObject) api.get("ENDPOINTS");
		JSONObject api_auth = (JSONObject) api_endpoints.get("authorize");
		JSONObject endpoint = (JSONObject) api_endpoints.get(endpointName);
		String routeName = getRoute(endpoint, params);
		routeName += getQueryString(params);

		if (this.requester == null) {
			requester = new APIRequest(endpoint.get("method").toString(), routeName, body, api_auth, this.config);
		}
		JSONObject response = this.requester.send();
		this.requester = null;

		return response;
	}

	private String kernelCallString(String endpointName, Map<String, String> params, JSONObject body) throws Exception {
		JSONObject endpoints = this.config.getEndpoints();
		HashMap<String, JSONObject> api = getRoute(endpoints, endpointName);
		if (!api.containsKey("ENDPOINTS"))
			throw new Exception("nonexistent endpoint");

		JSONObject api_url = (JSONObject) api.get("URL");
		this.config.setURLs(api_url);

		JSONObject api_endpoints = (JSONObject) api.get("ENDPOINTS");
		JSONObject api_auth = (JSONObject) api_endpoints.get("authorize");
		JSONObject endpoint = (JSONObject) api_endpoints.get(endpointName);
		String routeName = getRoute(endpoint, params);
		routeName += getQueryString(params);
		if (this.requester == null)
			requester = new APIRequest(endpoint.get("method").toString(), routeName, body, api_auth, this.config);
		String response = this.requester.sendString();
		this.requester = null;

		return response;
	}

	private String getQueryString(Map<String, String> params) throws UnsupportedEncodingException {
		Set<Entry<String, String>> set = params.entrySet();
		String query = "";
		for (Entry<String, String> entry : set) {
			if (!query.isEmpty())
				query += "&";
			else
				query += "?";
			query += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
		}
		return query;
	}

	private String getRoute(JSONObject endpoint, Map<String, String> params) {
		Pattern pattern = Pattern.compile("/:(\\w+)/");
		String route = endpoint.get("route").toString();
		route += "/";
		Matcher matcher = pattern.matcher(route);
		while (matcher.find()) {
			String value = route.substring(matcher.start() + 2, matcher.end() - 1);
			if (params.containsKey(value)) {
				route = route.replace(":" + value, params.get(value));
				params.remove(value);
				matcher = pattern.matcher(route);
			}
		}
		route = route.substring(0, route.length() - 1);
		return route;
	}
}
