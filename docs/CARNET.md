## Creating carnet billets

Carnet is a payment method that generates a set of charges with the same payment information and customer in all of them.

To generate a carnet, you have as required: the items, a customer, the expiration date of the first charge and the number of repeats (or parcels).

If you want, you can also send some additional informations:

- The metadata information (like in the banking billet), with notification_url and/or custom_id;
- If the total value must be split among every charges or if each charge must have the value;
- The message to the carnet
- The configurations to the carnet

Instantiate the module:

```java
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

public class TesteGN
{
	public static void main(String[] args){
	
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

### Setting the required properties to a carnet:
`required`

```java
JSONArray items = new JSONArray();
		
JSONObject item1 = new JSONObject();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 1000);

JSONObject item2 = new JSONObject("{\"name\":\"Item 1\", \"amount\":1, \"value\":1000}");

items.put(item1);
items.put(item2);

JSONObject customer = new JSONObject();
customer.put("name", "Gorbadoc Oldbuck");
customer.put("cpf", "04267484171");
customer.put("phone_number", "5144916523");

JSONObject body = new JSONObject();
body.put("items", items);
body.put("customer", customer);
body.put("expire_at", "2020-12-02");
body.put("repeats", 5);
```

### Setting metadata to a carnet:
`optional`

```java
JSONArray items = new JSONArray();
		
JSONObject item1 = new JSONObject();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 1000);

JSONObject item2 = new JSONObject("{\"name\":\"Item 1\", \"amount\":1, \"value\":1000}");

items.put(item1);
items.put(item2);

JSONObject customer = new JSONObject();
customer.put("name", "Gorbadoc Oldbuck");
customer.put("cpf", "04267484171");
customer.put("phone_number", "5144916523");

JSONObject metadata = new JSONObject();
metadata.put("custom_id", "Product 0001");
metadata.put("notification_url", "http://domain.com/notification");

JSONObject body = new JSONObject();
body.put("items", items);
body.put("customer", customer);
body.put("expire_at", "2020-12-02");
body.put("repeats", 5);
body.put("metadata", metadata);

```

The `notification_url` property will be used for notifications once things happen with charges status, as when it's payment was approved, for example. More about notifications [here](/docs/NOTIFICATION.md). The `custom_id` property can be used to set your own reference to the carnet.

### Setting the split items information
`optional`

By default, each parcel has the total value of the carnet as its value. If you want to divide the total value of the carnet by all the parcels, set the `split_items` property to *true*.

```java
JSONArray items = new JSONArray();
		
JSONObject item1 = new JSONObject();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 2000);

JSONObject item2 = new JSONObject("{\"name\":\"Item 1\", \"amount\":1, \"value\":1000}");

items.put(item1);
items.put(item2);

JSONObject customer = new JSONObject();
customer.put("name", "Gorbadoc Oldbuck");
customer.put("cpf", "04267484171");
customer.put("phone_number", "5144916523");

JSONObject body = new JSONObject();
body.put("items", items);
body.put("customer", customer);
body.put("expire_at", "2020-12-02");
body.put("repeats", 5);
body.put("split_items", true);
```

### Setting message to customer
`optional`

If you want the carnet billet to have a message to customer, it's possible to send a message with a maximum of 80 caracters, just as follows:

```java
JSONArray items = new JSONArray();
		
JSONObject item1 = new JSONObject();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 2000);

JSONObject item2 = new JSONObject("{\"name\":\"Item 1\", \"amount\":1, \"value\":1000}");

items.put(item1);
items.put(item2);

JSONObject customer = new JSONObject();
customer.put("name", "Gorbadoc Oldbuck");
customer.put("cpf", "04267484171");
customer.put("phone_number", "5144916523");

JSONObject body = new JSONObject();
body.put("items", items);
body.put("customer", customer);
body.put("expire_at", "2020-12-02");
body.put("repeats", 5);
body.put("message", "The delivery time is counted in working days, not include weekends and holidays");

```

### Setting configurations
`optional`

If you want the carnet billet to have own configurations. It's possible to send:
- `fine`: it's the amount charged after expiration. Ex.: If you want 2%, you must send 200.
- `interest`: it's the amount charged after expiration by day. Ex.: If you want 0.033%, you must send 33.

```php
JSONArray items = new JSONArray();
		
JSONObject item1 = new JSONObject();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 2000);

JSONObject item2 = new JSONObject("{\"name\":\"Item 1\", \"amount\":1, \"value\":1000}");

items.put(item1);
items.put(item2);

JSONObject customer = new JSONObject();
customer.put("name", "Gorbadoc Oldbuck");
customer.put("cpf", "04267484171");
customer.put("phone_number", "5144916523");

JSONObject configurations = new JSONObject();
configurations.put("fine", 200);
configurations.put("interest", 33);

JSONObject body = new JSONObject();
body.put("items", items);
body.put("customer", customer);
body.put("expire_at", "2020-12-02");
body.put("repeats", 5);
body.put("configurations", configurations);
```

### Finally, create the carnet:

```java
try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("createCarnet", new HashMap<>(), body);
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
    "carnet_id": 1000,
    "cover": "https://visualizacao.gerencianet.com.br/emissao/28333_2385_ZEMAL5/A5CC-28333-61428-LEENA9/28333-61428-LEENA9",
    "charges": [{
        "charge_id": 357,
        "parcel": "1",
        "status": "waiting",
        "value": 2000,
        "expire_at": "2020-12-12",
        "url": "https://visualizacao.gerencianet.com.br/emissao/28333_2385_ZEMAL5/A5CL-28333-61428-LEENA9/28333-61428-LEENA9",
        "barcode": "00190.00009 01523.894002 00061.428181 1 64780000002000"
      }, {
        "charge_id": 358,
        "parcel": "2",
        "status": "waiting",
        "value": 2000,
        "expire_at": "2021-01-12",
        "url": "https://visualizacao.gerencianet.com.br/emissao/28333_2385_ZEMAL5/A5CL-28333-61428-LEENA9/28333-61429-CORZE4",
        "barcode": "00190.00009 01523.894002 00061.428181 8 65090000002000"
      }, {
        "charge_id": 359,
        "parcel": "3",
        "status": "waiting",
        "value": 2000,
        "expire_at": "2021-02-12",
        "url": "https://visualizacao.gerencianet.com.br/emissao/28333_2385_ZEMAL5/A5CL-28333-61428-LEENA9/28333-61430-HIRRA4",
        "barcode": "00190.00009 01523.894002 00061.428181 7 65400000002000"
      }, {
        "charge_id": 360,
        "parcel": "4",
        "status": "waiting",
        "value": 2000,
        "expire_at": "2021-03-12",
        "url": "https://visualizacao.gerencianet.com.br/emissao/28333_2385_ZEMAL5/A5CL-28333-61428-LEENA9/28333-61431-HIRRA4",
        "barcode": "00190.00009 01523.894002 00061.428181 5 65400000002000"
      }
    ]
  }
}

```

Notice that, as the `repeats` were set to 5, the output contains 5 charges with `waiting` status. The value of each charge is the sum of the items values, because the `split_items` property was set to *false*. Also notice that `expire_at` increases monthly.
