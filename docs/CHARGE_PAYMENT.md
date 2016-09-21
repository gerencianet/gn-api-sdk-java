## Paying charges

There are two ways of giving sequence to a charge. You can generate a banking billet so it is payable until its due date, or can use the customer's credit card to submit the payment.

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

### 1. Banking billets

Setting banking billet as a charge's payment method is simple. You have to use `banking_billet` as the payment method and inform the `charge_id`.

```java
HashMap<String, String> params = new HashMap<String, String>();
params.put("id", "1000");

JSONObject customer = new JSONObject();
customer.put("name", "Gorbadoc Oldbuck");
customer.put("cpf", "04267484171");
customer.put("phone_number", "5144916523");

JSONObject bankingBillet = new JSONObject();
bankingBillet.put("expire_at", "2018-12-12");
bankingBillet.put("customer", customer);

JSONObject payment = new JSONObject();
payment.put("banking_billet", bankingBillet);

JSONObject body = new JSONObject();
body.put("payment", payment);

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("payCharge", params, body);
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

You'll receive the payment info, such as the barcode and the billet link:

```json

{
  "code": 200,
  "data": {
    "charge_id": 1000,
    "total": 5000,
    "payment": "banking_billet",
    "barcode": "00190.00009 01523.894002 00059.161182 9 64350000001150",
    "link": "https://visualizacao.gerencianet.com.br/emissao/28333_2139_RRABRA7/A4XB-28333-59161-BRANAE4",
    "expire_at": "2018-12-21"
  }
}

```

If you want the banking billet to have a message to customer, it's possible to send a message with a maximum of 80 caracters, just as follows:

```java
HashMap<String, String> params = new HashMap<String, String>();
params.put("id", "1000");

JSONObject customer = new JSONObject();
customer.put("name", "Gorbadoc Oldbuck");
customer.put("cpf", "04267484171");
customer.put("phone_number", "5144916523");

JSONObject bankingBillet = new JSONObject();
bankingBillet.put("expire_at", "2018-12-12");
bankingBillet.put("customer", customer);
bankingBillet.put("message", "The delivery time is counted in working days, not include weekends and holidays");

JSONObject payment = new JSONObject();
payment.put("banking_billet", bankingBillet);

JSONObject body = new JSONObject();
body.put("payment", payment);

```

If you want the banking billet to have own configurations. It's possible to send:
- `fine`: it's the amount charged after expiration. Ex.: If you want 2%, you must send 200.
- `interest`: it's the amount charged after expiration by day. Ex.: If you want 0.033%, you must send 33.

```java
HashMap<String, String> params = new HashMap<String, String>();
params.put("id", "1000");

JSONObject customer = new JSONObject();
customer.put("name", "Gorbadoc Oldbuck");
customer.put("cpf", "04267484171");
customer.put("phone_number", "5144916523");

JSONObject configurations = new JSONObject();
configurations.put("fine", 200);
configurations.put("interest", 33);

JSONObject bankingBillet = new JSONObject();
bankingBillet.put("expire_at", "2018-12-12");
bankingBillet.put("customer", customer);
bankingBillet.put("configurations", configurations);

JSONObject payment = new JSONObject();
payment.put("banking_billet", bankingBillet);

JSONObject body = new JSONObject();
body.put("payment", payment);

```

### 2. Credit card

The most common payment method is to use a credit card in order to make things happen faster. Paying a charge with a credit card in Gerencianet is as simples as generating a banking billet, as seen above.

The difference here is that we need to provide some extra information, as a `billing_address` and a `payment_token`. The former is used to make an anti-fraud analyze before accepting/appoving the payment, the latter identifies a credit card at Gerencianet, so that you don't need to bother about keeping track of credit card numbers. The `installments` attribute is self-explanatory.

To credit card is also possible to send `message` and `configurations` like in banking billet. For more information, see the previous topic.

We'll talk about getting payment tokens later. For now, let's take a look at the snipet that does the work we're aiming for:

```java

HashMap<String, String> params = new HashMap<String, String>();
params.put("id", "1000");

String paymentToken = "payment_token";

JSONObject customer = new JSONObject();
customer.put("name", "Gorbadoc Oldbuck");
customer.put("cpf", "04267484171");
customer.put("phone_number", "5144916523");
customer.put("email", "ldbuck@gerencianet.com.br");
customer.put("birth", "1977-01-15");

JSONObject billingAddress = new JSONObject();
billingAddress.put("street", "Av. JK");
billingAddress.put("number", 909);
billingAddress.put("neighborhood", "Bauxita");
billingAddress.put("zipcode", "5400000");
billingAddress.put("city", "Ouro Preto");
billingAddress.put("state", "MG");

JSONObject creditCard = new JSONObject();
creditCard.put("installments", 1);
creditCard.put("billing_address", billingAddress);
creditCard.put("payment_token", paymentToken);
creditCard.put("customer", customer);

JSONObject payment = new JSONObject();
payment.put("credit_card", creditCard);

JSONObject body = new JSONObject();
body.put("payment", payment);

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("payCharge", new HashMap<>(), body);
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

If everything went well, the response will come with the total value, installments number and the value of each installment:

```json
{
  "code": 200,
  "data": {
    "charge_id": 1000,
    "total": 5000,
    "payment": "credit_card",
    "installments": 1,
    "installment_value": 5000
  }
}

```

##### Payment tokens

A `payment_token` represents a credit card number at Gerencianet.

For testing purposes, you can go to your application playground in your Gerencianet's account. At the payment endpoint you'll see a button that generates one token for you. This payment token will point to a random test credit card number.

When in production, it will depend if your project is a web app or a mobile app.

For web apps you should follow this [guide](https://docs.gerencianet.com.br/#!/charges/checkout/card). It basically consists of copying/pasting a script tag in your checkout page.

For mobile apps you should use this [SDK for Android](https://github.com/gerencianet/gn-api-sdk-android) or this [SDK for iOS](https://github.com/gerencianet/gn-api-sdk-ios).

### 3. Discount by payment method

It is possible to set discounts based on payment. You just need to add an `discount` attribute within `banking_billet` or `credit_card` tags.

The example below shows how to do this for credit card payments.

```java

HashMap<String, String> params = new HashMap<String, String>();
params.put("id", "1000");

String paymentToken = "payment_token";

JSONObject customer = new JSONObject();
customer.put("name", "Gorbadoc Oldbuck");
customer.put("cpf", "04267484171");
customer.put("phone_number", "5144916523");
customer.put("email", "ldbuck@gerencianet.com.br");
customer.put("birth", "1977-01-15");

JSONObject billingAddress = new JSONObject();
billingAddress.put("street", "Av. JK");
billingAddress.put("number", 909);
billingAddress.put("neighborhood", "Bauxita");
billingAddress.put("zipcode", "5400000");
billingAddress.put("city", "Ouro Preto");
billingAddress.put("state", "MG");

JSONObject discount = new JSONObject();
discount.put("type", "currency");
discount.put("value", 1000);

JSONObject creditCard = new JSONObject();
creditCard.put("installments", 1);
creditCard.put("billing_address", billingAddress);
creditCard.put("payment_token", paymentToken);
creditCard.put("customer", customer);
creditCard.put("discount", discount);

JSONObject payment = new JSONObject();
payment.put("credit_card", creditCard);

JSONObject body = new JSONObject();
body.put("payment", payment);

```
Discounts for banking billets works similar to credit cards. You just need to add the `discount` attribute.

The discount may be applied as percentage or with a previous calculated value.

The `type` property is used to specify how the discount will work. It may be set as *currency* or *percentage*.

The first will discount the amount specified in `value` property as *cents*, so, in the example above the amount paid by the customer will be equal `(Charge's value) - R$ 10,00`.

However, if the discount type is set to *percentage*, the amount will be `(Charge's value) - 10%`.
