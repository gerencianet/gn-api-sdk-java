## Creating charges

Charges have one or more items. That's it.

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

### Adding items:
```java
JSONArray items = new JSONArray();
		
JSONObject item1 = new JSONObject();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 2000);

JSONObject item2 = new JSONObject("{\"name\":\"Item 1\", \"amount\":1, \"value\":1000}");

items.put(item1);
items.put(item2);

```

### Adding shipping costs to a charge **(optional)**:

In order to be the most agnostic as possible about how the user handles shippings, the API just receives an array with the values. You can add as many as you want. Sometimes you'll want a shipping cost to be received by another person/account. In this case, you must provide the `payee_code`. The *Additional Shipping* in the example below will be passed on to the referenced account after the payment.

```java
JSONArray items = new JSONArray();
		
JSONObject item1 = new JSONObject();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 2000);

JSONObject item2 = new JSONObject("{\"name\":\"Item 1\", \"amount\":1, \"value\":1000}");

items.put(item1);
items.put(item2);

JSONArray shippings = new JSONArray();

JSONObject shipping1 = new JSONObject();
shipping1.put("name", "My Shipping");
shipping1.put("value", 2000);

JSONObject shipping2 = new JSONObject();
shipping1.put("name", "Shipping to someone else");
shipping1.put("value", 1000);
shipping1.put("payee_code", GEZTAMJYHA3DAMBQGAYDAMRYGMZTGMBRGI);

shippings.put(shipping1);
shippings.put(shipping2);

JSONObject body = new JSONObject();
body.put("items", items);
body.put("shippings", shippings);

```

### Charge `metadata` attribute:

```java
JSONArray items = new JSONArray();
		
JSONObject item1 = new JSONObject();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 2000);

JSONObject item2 = new JSONObject("{\"name\":\"Item 1\", \"amount\":1, \"value\":1000}");

items.put(item1);
items.put(item2);

JSONObject metadata = new JSONObject();
metadata.put("custom_id", "Product 0001");
metadata.put("notification_url", "http://my_domain.com/notification");

JSONObject body = new JSONObject();
body.put("items", items);
body.put("metadata", metadata);

```

The `notification_url` property will be used for sending notifications once things happen with charges statuses, as when it's payment was approved, for example. More about notifications [here](/docs/NOTIFICATION.md). The `custom_id` property can be used to set your own reference to the charge.


### Finally, create the charge:

```java
try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("createCharge", new HashMap<String,String>(), body);
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

Check out the response:

```json
{
  "code": 200,
  "data": {
    "charge_id": 1253,
    "total": 3000,
    "status": "new",
    "custom_id": "Product 0001",
    "created_at": "2016-05-18 14:56:39"
  }
}
```
