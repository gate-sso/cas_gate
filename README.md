## gate-mfa

CAS Gate SSO Authentication Handler plugin for Gate SSO. 
Gate SSO is MFA enabled HTTP/Rest best authentication and VPN management server.

> Although it can be used to authenticate against any HTTP-Based Service.
> Which responds with body "0" for successful login and "1" for authentication failure with "username" and"token" passed as GET Request.


Installation
----


* `./doc/deployerConfigContext.xml` is a sample cas-maven-overlay config to enable this plug-in
* It includes maven-wrapper, so just run './mvnw package' to generate target jar.
* Then put this jar in cas configuration with package name


Todo
----
1. Better documentation and all steps for writng custom MFA
2. Example code for CAS configuration