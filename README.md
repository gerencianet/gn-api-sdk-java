# SDK GERENCIANET FOR JAVA

Sdk for Gerencianet Pagamentos' API.
For more informations about parameters and values, please refer to [Gerencianet](http://gerencianet.com.br) documentation.

**Em caso de dúvidas, você pode verificar a [Documentação](https://docs.gerencianet.com.br) da API na Gerencianet e, necessitando de mais detalhes ou informações, entre em contato com nossa consultoria técnica, via nossos [Canais de Comunicação](https://gerencianet.com.br/central-de-ajuda).**


[![Build Status](https://travis-ci.org/gerencianet/gn-api-sdk-java.svg?branch=master)](https://travis-ci.org/gerencianet/gn-api-sdk-java)
[![Coverage Status](https://coveralls.io/repos/github/gerencianet/gn-api-sdk-java/badge.svg?branch=master)](https://coveralls.io/github/gerencianet/gn-api-sdk-java?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/br.com.gerencianet.gnsdk/gn-api-sdk-java.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22br.com.gerencianet.gnsdk%22%20AND%20a%3A%22gn-api-sdk-java%22)

## Requirements
* Java >= 7.0

## Tested with
```
java  7.0 and 8.0
```
## Installation

**Via gradle:**

```gradle
compile 'br.com.gerencianet.gnsdk:gn-api-sdk-java:0.2.5'
```

**Via maven:**

```xml
<dependency>
    <groupId>br.com.gerencianet.gnsdk</groupId>
    <artifactId>gn-api-sdk-java</artifactId>
    <version>0.2.5</version>
</dependency>
```

## Getting started
Require the module and packages:
```java
import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

```
Although the web services responses are in json format, the sdk will convert any server response to a JSONObject or a Map<String, Object>. The code must be within a try-catch and exceptions can be handled as follow:
```
```java
try {
  /* code */
} catch(GerencianetException e) {
  /* Gerencianet's api errors will come here */
} catch(Exception ex) {
  /* Other errors will come here */
}
```

### For development environment
Instantiate the module passing using your client_id, client_secret and sandbox equals true:
```java
JSONObject options = new JSONObject();
options.put("client_id", "client_id");
options.put("client_secret", "client_secret");
options.put("sandbox", true);

Gerencianet gn = new Gerencianet($options);
```
Or

```java
Map<String, Object> options = new HashMap<String, Object>();
options.put("client_id", "client_id");
options.put("client_secret", "client_secret");
options.put("sandbox", true);

Gerencianet gn = new Gerencianet($options);
```

### For production environment
To change the environment to production, just set the third sandbox to false:
```java
JSONObject options = new JSONObject();
options.put("client_id", "client_id");
options.put("client_secret", "client_secret");
options.put("sandbox", false);

Gerencianet gn = new Gerencianet($options);
```
Or

```java
Map<String, Object> options = new HashMap<String, Object>();
options.put("client_id", "client_id");
options.put("client_secret", "client_secret");
options.put("sandbox", false);

Gerencianet gn = new Gerencianet($options);
```

## Running tests

To run the test suite with coverage:

```
mvn clean test jacoco:report
```
## Running examples
To run some existing examples follow the steps described at [gn-api-sdk-java-examples](https://github.com/gerencianet/gn-api-sdk-java-examples).

## Additional Documentation

The full documentation with all available endpoints is in https://dev.gerencianet.com.br/.

## Changelog

[CHANGELOG](CHANGELOG.md)

## License ##
[MIT](LICENSE)
