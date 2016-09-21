## Listing plans

Listing plans is pretty simple. There are no required parameters for this, although you can use special query parameters can be used to filter your search.
By default, the search will bring back 20 registers and always start from offset 0.
The example below shows how to use it:

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
		params.put("name", "My plan");
		params.put("limit", "20");
		params.put("offset", "0");
		
		JSONObject options = new JSONObject();
		options.put("client_id", "client_id");
		options.put("client_secret", "client_secret");
		options.put("sandbox", true); 
		
		try {
			Gerencianet gn = new Gerencianet(options);
			JSONObject plans = gn.call("getPlans", params, new JSONObject());
			System.out.println(plans);
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

If the filters are correct, the response will be an array like this:
```java

Array
(
    [code] => 200
    [data] => Array
        (
            [0] => Array
                (
                    [id] => 1
                    [name] => My plan
                    [interval] => 12
                    [repeats] => 2
                    [created_at] => 2015-07-22T12:33:06.000Z
                    [updated_at] => 2015-07-22T12:33:06.000Z
                )
            [1] => Array
                (
                    [id] => 2
                    [name] => My other plan
                    [interval] => 1
                    [repeats] => 12
                    [created_at] => 2014-12-22T12:33:06.000Z
                    [updated_at] => 2014-12-22T12:33:06.000Z
                )

        )

)
```
