## Updating charges

Instantiate the module:
```java
import java.util.HashMap;
import org.json.JSONObject;
import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

public class TesteGN 
{
	public static void main(String[] args)
	{
		HashMap<String, String> params = new HashMap<>();
		
		JSONObject options = new JSONObject();
		options.put("client_id", "client_id");
		options.put("client_secret", "client_secret");
		options.put("sandbox", true); 
		
		try {
			Gerencianet gn = new Gerencianet(options);
		}catch (GerencianetException e){
			System.out.println(e.getCode());
			System.out.println(e.getError());
			System.out.println(e.getErrorDescription());
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

```

### Changing the metadata

You can update the `custom_id` or the `notification_url` of a charge at any time you want:

```java
HashMap<String, String> params = new HashMap<String, String>();
params.put("id", "1000");

JSONObject body = new JSONObject();
body.put("custom_id", "Product 0001");
body.put("notification_url", "http://my_domain.com/notification");

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("updateChargeMetadata", params, body);
	System.out.println(response);
}catch (GerencianetException e){
	System.out.println(e.getCode());
	System.out.println(e.getError());
	System.out.println(e.getErrorDescription());
}
catch (Exception e) {
	System.out.println(e.getMessage());
}

```

If everything goes well, the return will be:

```json
{
	"code":200
}
```

### Updating the expiration date of a billet

To update or set a expiration date to a charge, the charge must have a `waiting` or `unpaid` status, and the payment method chosen must be `banking_billet`.

If the charge contemplates these requirements, you just have to provide the charge id and a new expiration date:

```java
HashMap<String, String> params = new HashMap<String, String>();
params.put("id", "1000");

JSONObject body = new JSONObject();
body.put("expire_at", "2016-01-01");

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("updateBillet", params, body);
	System.out.println(response);
}catch (GerencianetException e){
	System.out.println(e.getCode());
	System.out.println(e.getError());
	System.out.println(e.getErrorDescription());
}
catch (Exception e) {
	e.printStackTrace();
	System.out.println(e.getMessage());
}

```

If everything goes well, the return will be:


```json
{
	"code":200
}
```
