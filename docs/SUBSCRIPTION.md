## Creating subscriptions

If you ever have to recurrently charge your clients, you can create a different kind of charge, one that belongs to a subscription. This way, subsequent charges will be automatically created based on plan configuration and the charge value is charged in your customers' credit card, or the banking billet is generated and sent to costumer, accoding to plan’s configuration.

The plan configuration receive two params, that are repeats and interval:

The `repeats` parameter defines how many times the transaction will be repeated. If you don't pass it, the subscription will create charges indefinitely.

The `interval` parameter defines the interval, in months, that a charge has to be generated. The minimum value is 1, and the maximum is 24. So, define "1" if you want monthly creations for example.

It's worth to mention that this mechanics is triggered only if the customer commits the subscription. In other words, it takes effect when the customer pays the first charge.

At first, you need to to create a plan. Then, you create a charge passing a plan_id to generate a subscription. You can use the same plan_id whenever you want.

First instantiate the module:

```java
import java.util.HashMap;
import org.json.JSONObject;
import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

public class TesteGN 
{
	public static void main(String[] args)
	{		
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

### Creating and setting a plan to a subscription:

```java
JSONObject body = new JSONObject();
body.put("name", "My plan");
body.put("interval", 2);
body.put("repeats", null);

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject plan = gn.call("createPlan", new HashMap<String,String>(), body);
	System.out.println(plan);
}catch (GerencianetException e){
	System.out.println(e.getCode());
	System.out.println(e.getError());
	System.out.println(e.getErrorDescription());
}catch (Exception e) {
	System.out.println(e.getMessage());
}

```

### Creating the subscription:

```java
HashMap<String, String> params = new HashMap<String, String>();
params.put("id", "1000");

JSONArray items = new JSONArray();
		
JSONObject item1 = new JSONObject();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 2000);

JSONObject item2 = new JSONObject("{\"name\":\"Item 1\", \"amount\":1, \"value\":1000}");

items.put(item1);
items.put(item2);

JSONObject body = new JSONObject();
body.put("items", items);

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject subscription = gn.call("createSubscription", params, body);
	System.out.println(subscription);
}catch (GerencianetException e){
	System.out.println(e.getCode());
	System.out.println(e.getError());
	System.out.println(e.getErrorDescription());
}catch (Exception e) {
	System.out.println(e.getMessage());
}

```

### Deleting a plan:
*(works just for plans that hasn't a subscription associated):*

```java
HashMap<String, String> params = new HashMap<String, String>();
params.put("id", "1000");

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject plan = gn.call("deletePlan", params, new JSONObject());
	System.out.println(plan);
}catch (GerencianetException e){
	System.out.println(e.getCode());
	System.out.println(e.getError());
	System.out.println(e.getErrorDescription());
}catch (Exception e) {
	System.out.println(e.getMessage());
}

```

### Canceling subscriptions

You can cancel active subscriptions at any time:

```java
HashMap<String, String> params = new HashMap<String, String>();
params.put("id", "1000");

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject subscription = gn.call("cancelSubscription", params, new JSONObject());
	System.out.println(subscription);
}catch (GerencianetException e){
	System.out.println(e.getCode());
	System.out.println(e.getError());
	System.out.println(e.getErrorDescription());
}catch (Exception e) {
	System.out.println(e.getMessage());
}

```
If everything goes well, the return will be:

```json
{
	"code":200
}
```

