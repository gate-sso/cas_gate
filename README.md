## gate-mfa

CAS Authentication Handler plugin for MFA enablement via Gate Service (based Google Authenticator).

> Although it can be used to authenticate against any HTTP-Based Service.
> Which responds with body "0" for successful login and "1" for authentication failure with "username" and"token" passed as GET Request.

---

* './doc/deployerConfigContext.xml' is a sample cas-maven-overlay config to enable this plug-in

---

> It includes maven-wrapper, so just run './mvnw package' to generate target jar.

---
---
