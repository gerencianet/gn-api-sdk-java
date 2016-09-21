## Payment data - listing installments

If you ever need to get the total value for a charge, including rates and interests, as well as each installment value, even before the payment itself, you can.

Why would I need this?

Sometimes you need to check the total for making a discount, or simple to show a combobox with the installments for your users.

Stop bragging about. Here is the code:

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
		
		HashMap<String, String> params = new HashMap<>();
		params.put("total", "2000");
		params.put("brand", "visa");
		
		try {
			Gerencianet gn = new Gerencianet(options);
			JSONObject installments = gn.call("getInstallments", params, new JSONObject());
			System.out.println(installments);
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

And the response:

```json
{
  "code": 200,
  "data": {
    "rate": 150,
    "interest_percentage": 0,
    "name": "visa",
    "installments": [
      {
        "installment": 1,
        "has_interest": False,
        "value": 5150,
        "currency": "51,50"
      },
      {
        "installment": 2,
        "has_interest": True,
        "value": 2679,
        "currency": "26,79"
      },
      {
        "installment": 3,
        "has_interest": True,
        "value": 1821,
        "currency": "18,21"
      },
      {
        "installment": 4,
        "has_interest": True,
        "value": 1393,
        "currency": "13,93"
      },
      {
        "installment": 5,
        "has_interest": True,
        "value": 1137,
        "currency": "11,37"
      },
      {
        "installment": 6,
        "has_interest": True,
        "value": 966,
        "currency": "9,66"
      },
      {
        "installment": 7,
        "has_interest": True,
        "value": 845,
        "currency": "8,45"
      },
      {
        "installment": 8,
        "has_interest": True,
        "value": 754,
        "currency": "7,54"
      },
      {
        "installment": 9,
        "has_interest": True,
        "value": 683,
        "currency": "6,83"
      },
      {
        "installment": 10,
        "has_interest": True,
        "value": 627,
        "currency": "6,27"
      },
      {
        "installment": 11,
        "has_interest": True,
        "value": 581,
        "currency": "5,81"
      },
      {
        "installment": 12,
        "has_interest": True,
        "value": 544,
        "currency": "5,44"
      }
    ]
  }
}

```
