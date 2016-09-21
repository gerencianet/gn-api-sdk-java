### Resending carnet parcel

To resend the carnet parcel, the parcel must have a `waiting` status.

If the parcel contemplates this requirement, you just have to provide the carnet id, the parcel number and a email to resend it:

```java
HashMap<String, String> params = new HashMap<>();
params.put("id", "0");
params.put("parcel", "1");

JSONObject body = new JSONObject();
body.put("email", "oldbuck@gerencianet.com.br");

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("resendParcel", params, body);
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
