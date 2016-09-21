## Creating charges with marketplace

What if your web store contains products from many different sellers from many different segments? The user can complete a single purchase with products from more than one seller, right? Here enters marketplace.

With some extra attributes, you can tell Gerencianet to pass on a percentage amount of the purchase total value to someone else.

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

Create the charge object including a marketplace object:

```java
JSONObject marketplace = new JSONObject();
JSONArray repasses = new JSONArray();

JSONObject repasse1 = new JSONObject();
repasse1.put("payee_code", "GEZTAMJYHA3DAMBQGAYDAMRYGMZTGM");
repasse1.put("percentage", 2500);

JSONObject repasse2 = new JSONObject();
repasse1.put("payee_code", "AKSLJI3DAMBQGSKLJDYDAMRTGOPWKS");
repasse1.put("percentage", 2500);

repasses.put(repasse1);
repasses.put(repasse2);

marketplace.put("repasses", repasses);

JSONArray items = new JSONArray();
		
JSONObject item = new JSONObject();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 2000);
item1.put("marketplace", marketplace);
items.put(item);

JSONObject body = new JSONObject();
body.put("items", items);

```

Create the charge:

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

The attribute `payee_code` identifies a Gerencianet account, just like in [creating charges with shippings](/docs/CHARGE.md). In order to get someone else's `payee_code` you need to ask the account owner. There is no other way. To visualize yours, log in your Gerencianet account and search for *Identificador de Conta* under *Dados Cadastrais*.

In the example above, there are two repasses, both of 25%, but each one for a different account, whereas the `payee_code` differs. The integrator account will receive, at the end, 50% of the total value. Disregarding the rates, the integrator account would receive R$5,00. The other two accounts would receive R$ 2,50 each.

The response is the same as usual:

```json
{
  "code": 200,
  "data": {
    "charge_id": 1039,
    "total": 5000,
    "status": "new",
    "custom_id": None,
    "created_at": "2016-05-18 14:56:39"
  }
}

```
