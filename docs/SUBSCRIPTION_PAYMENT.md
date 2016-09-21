## Paying subscriptions

There is two ways of giving sequence to a subscription *banking billet* or *credit card*.

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

To submit the payment with banking billet, you just need define the customer and the expire at to first charge:

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
	JSONObject response = gn.call("paySubscription", params, body);
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

If everything went well, the response will come with barcode, link to banking billet and the value oh each installment:

```json
{
  "code": 200,
  "data": {
    "subscription_id": 11,
    "status": "active",
    "plan": {
      "id": 1000,
      "interval": 2,
      "repeats": None
    },
    "charge": {
      "id": 1053,
      "status": "waiting"
    },
    "total": 1150,
    "payment": "banking_billet"
  }
}

```

### 2. Credit card

As we know, the credit card information is confidential, so, you need to prepare your system to send this information in a securely way. See how to send it and receive the payment token in our official documentation. Here we show how to do the backend part.


Then pay the subscription:

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
	JSONObject response = gn.call("paySubscription", new HashMap<>(), body);
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


If everything went well, the response will come with total value:

```json
{
  "code": 200,
  "data": {
    "subscription_id": 11,
    "status": "active",
    "plan": {
      "id": 1000,
      "interval": 2,
      "repeats": None
    },
    "charge": {
      "id": 1053,
      "status": "waiting"
    },
    "total": 1150,
    "payment": "credit_card"
  }
}

```

To know every installment value including interests for each brand, you can see [Getting the Payment Data](/docs/PAYMENT_DATA.md).


##### Payment tokens

A `payment_token` represents a credit card number at Gerencianet.

For testing purposes, you can go to your application playground in your Gerencianet's account. At the payment endpoint you'll see a button that generates one token for you. This payment token will point to a random test credit card number.

When in production, it will depend if your project is a web app or a mobile app.

For web apps you should follow this [guide](https://api.gerencianet.com.br/checkout/card). It basically consists of copying/pasting a script tag in your checkout page.

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

The discount may be applied as percentage or with a previously calculated value.

The `type` property is used to specify how the discount will work. It may be set as *currency* or *percentage*.

The first will discount the amount specified in `value` property as *cents*. So, in the example above, the amount paid by the customer will be equal `(Charge's value) - R$ 10,00`.

However, if the discount type is set to *percentage*, the amount will be `(Charge's value) - 10%`.
