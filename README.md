Spring boot client for Configuration Server
=============

This library is intended for Spring boot clients. It will automatically connect to the config service to get your configuration files.

### Add Maven Dependency

The best way to get the client is via Maven:

```xml
<dependency>
    <groupId>com.intuit.common-config.config-client</groupId>
    <artifactId>spring-config-client-fallback</artifactId>
    <version>{latest_jar}</version>
</dependency>
```

### Edit the bootstrap.yml file

After adding the dependency, just include the following properties in your bootstrap.yml or application.yml.
There is no need to instantiate the client. It will bootstrap automatically using the below properties:

```
spring:
    application:
        name: foo (Your application name/Prefix you selected while onboarding)
    profiles:
        active: dev (This is the profile that will be fetched)
    cloud:
        config:
            uri: {servicePortalURL}/configserver/v1
            username: {servicePortalappID}
            password: {servicePortalappSecret}
```

Thats all that is needed to use the Spring boot client.


### Fallback Mechanism

The client comes with a fallback mechanism which can be enabled to handle cases when there is a network outage, or if the Config service is down for maintenance.
When you enable the fallback, the client adapter will "cache" the computed properties on your local filesystem. To enable the fallback, just specify the location to store the cache.

```
spring:
    application:
        name: foo (Your application name/Prefix you selected while onboarding)
    profiles:
        active: dev (This is the profile that will be fetched)
    cloud:
        config:
            uri: {servicePortalURL}/configserver/v1
            username: {servicePortalappID}
            password: {servicePortalappSecret}
            fallbackLocation: /path/to/fallback/location
```

### Encryption when using fallback

If you have any sensitive properties e.g. passwords, storing it locally in clear text is a security risk. So if you do enable the fallback mechanism, and you also have some sensitive properties, the client can encrypt them for you.
            Just add the "Spring RSA" dependency jar to your pom.xml and specify the keystore in your bootstrap.yml.
            e.g.
```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-rsa</artifactId>
    <version>{latest_jar}</version>
</dependency>
```

bootstrap.yml
```
encrypt:
    keyStore:
        location: file:///path/to/keystore.jks
        password: ConfigServiceIstheBest!
        alias: myAlias
```
You can read more about about Spring RSA Encypytion here - http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html#_key_management


## Issues & Contributions
Please [open an issue here on GitHub](https://github.com/intuit/spring-config-client-fallback/issues/new) if you have a problem, suggestion, or other comment.

Pull requests are welcome and encouraged!  Any contributions should include new or updated unit tests as necessary to maintain thorough test coverage.

## License
spring-config-client-fallback is provided under the [Eclipse Public License - Version 1.0](http://www.eclipse.org/legal/epl-v10.html)

