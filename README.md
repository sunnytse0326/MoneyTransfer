# Android Secure Money Transfer Application


Network security has long been regarded as an important issue between server and mobile or website platforms, especially in payment gateway and banking system. Here is an example showing how to build a simple money transfer application in Android platform.


As we need to demonstrate how to build a high-level security application in client-side, we already build a [GitHub page](https://sunnytse0326.github.io/MockJson/balance/result.json) which will return following results:
```
{
	"success": true,
	"data": {
		"balance": 2500,
		"currency": "HKD"
	}
}
```

# Project Structure
This project included a login page and balance-check page. We need to clarify the identification login by user, save the encrypted token and use it as an authorization communicating with server.

In this application, we build the architecture by using MVVM structure with data-binding features. We use anko to build the layout and use native Http libraries for making network connections.


# Implementation
In this application, we mainly handled several security matters in different aspects:
1. SSL Pinning
2. Login
3. Private/Public keys save location
4. Data encryption/decryption
5. Proguard the application

## SSL Pinning
SSL pinning develops a significant security protection in network layer during SSL handshake communication. It could ensure the connection between client and server side by implementing public key, or more specifically the SubjectPublicKeyInfo (SPKI).

In this example, you could generate the key pairs referencing from server domain-name and the key pairs are in SHA-256 hash by using Base-64 encoding standard. 

### Shell script command:

```
sh pinpairs.sh sunnytse0326.github.io
```

### Results:
```
/C=US/ST=California/L=San Francisco/O=GitHub, Inc./CN=www.github.com
tlWQhja7+4LV96qCw71ZE325VzDFNgQuI0N2soN4JSo=
/C=US/O=DigiCert Inc/OU=www.digicert.com/CN=DigiCert SHA2 High Assurance Server CA
k2v657xBsOVe1PQRwOsHsw3bsGT2VzIqz5K+59sNQws=
```
We will use the upper one as public key and make url connections with server. As some of third party network libraries may have  vulnerabilities on certificate pinners like OKHttp order version, we prefer to use native HttpUrlConnection which will be easily to control and monitor.
<br>

## Login
This application support login feature. After we type any characters to login and password field, it will encrypt the data (which will be descripted below) and return the token. The token will also be encrypted and save in device.

<p float="left">
  <img src="https://github.com/sunnytse0326/MoneyTransfer/blob/develop/screenshot/screenshot4.png" width="250" height="400">
  <img src="https://github.com/sunnytse0326/MoneyTransfer/blob/develop/screenshot/screenshot5.png" width="250" height="400">
  <img src="https://github.com/sunnytse0326/MoneyTransfer/blob/develop/screenshot/screenshot6.png" width="250" height="400">
</p>


## Private/Public keys save location
All thye cryptographic keys will be saved in build.gradle build type location. They will be easier to manager out of the application when generating the apk files to some deployment flows like CI/CD, as the keys may be needed to change from time to time, especially to the SSL pinner.

<br>

## Data encryption/decryption
All sensitive data must be encrypted and we will decrypt all encrypted data from server in order to make a secure communication. From the project, we use two sets of RSA key pairs (with SHA-256 hashed) to ensure the data will be safely delivered from client to server for the request and back from server to client for the response. However, in this example, we use one set of key to demonstrate how the flow works. Here are the key pair:


### Public Key (RSA with SHA-256 standard)
```
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtfObupl+mwD5EzmHXLE/F3BJc0s/qA5YaeTQJFA5EI2nsUGkiF9CrHnqKndXR3G3hkXA37BhGmimsE5ved62M+FIUv/NRTnqdqpFMZDqBGCAwzrc151f/rL82kqSly1CG8fzCnYKcsnYLSgE9JMmdc4pju/4KhmxPq404YqdGEN1agC0mH9N1N5d1GNuFBoqSBMsvqpLr3DF4481zFdE0W+UPmhr05ozu7gNA1tYoOd+2iLoOQMFbd+4skOse8gS2/gVtOUylHZJY2lPw3GUHMy62ClvYmx9APFfHwBMadlkacalLK9kGKGylkPzPJ+4OStMRsrKiRnBH0A9+D6oHwIDAQAB
```

### Private Key (RSA with SHA-256 standard)
```
MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC185u6mX6bAPkTOYdcsT8XcElzSz+oDlhp5NAkUDkQjaexQaSIX0Kseeoqd1dHcbeGRcDfsGEaaKawTm953rYz4UhS/81FOep2qkUxkOoEYIDDOtzXnV/+svzaSpKXLUIbx/MKdgpyydgtKAT0kyZ1zimO7/gqGbE+rjThip0YQ3VqALSYf03U3l3UY24UGipIEyy+qkuvcMXjjzXMV0TRb5Q+aGvTmjO7uA0DW1ig537aIug5AwVt37iyQ6x7yBLb+BW05TKUdkljaU/DcZQczLrYKW9ibH0A8V8fAExp2WRpxqUsr2QYobKWQ/M8n7g5K0xGysqJGcEfQD34PqgfAgMBAAECggEARt04nz3N8ee4mlSzFBZSSLoqWTWK9tSTebasnCAEgZ5yG9bBkn1rdcpLubteKkyvWiPLoTNMNmFg3lTGFPR5CQ6qQO0w0M/kMdBR/6J9ijPdomvH/E/mRcgHM6uMmUV+tFw1G7NqkVdskvW9rFWtSn9/bxSmFhuIjS2DGf6G/Z4LDI2c/08DIq8ZAax9sXSbPGnMSeQz7WnjY5HBqO93b3/OnCq8qNEAjWSuzwOgSK+SHfr73cMC2YR5SfhWcJZoQbIyb3yM2dhmXGvUF6mGoU6jPV07MLypx47BySpFaOIURo8CMCgehBFAIwUpatqI21AKUFZI0a3eSdnJDNJnpQKBgQDzEAM12Ivrfgb0d1KloUWvkfgoU3BpYcqlh42rICVvVZ3hqZ7Gjz6+ESnDjO60zCd2yie0VjE5m32ZplU4XdDFP6FULRaP8kTiU3CfjBsjalgqnmxKmm1TYIs/sPboe/9PT7ltLMgYQOYYoFVjk6h3OQgIwKZhQqeq9rAv8VO+8wKBgQC/ouTC/KEsF59njOv18sJhCRHjaNv1dCEXTZjZw5PN3I1PTwYBXPQED+60TPuZPeBp+cZWmx/R9vKo1Bm8tPZ/Uj7vNVgXoTFJAWqngWzLELXrsLXC6Gx/+ViLVr9XWbhZ8sxLeGu9edkoEnP4YjuSw2Es0sHYDqjEpVcIWWV1JQKBgCIBfoF9D25eDnkLbKuxmX9Ly81QhyynOuPXyDFF6Rbfgpw8z/L1vZB39EDgqdDvsyHCfLWVTYPCxlrF06x0K649DcQqHrr66TpE9D/OKUnWKgmkaoAxhNQicLjzeEfkeT7OZ6HnnwMNqWOOowOwd+RePUqEMYLwAWdA5jt+5vmTAoGARl/IiH6DpZCLPli28lwS29Wb1xpT5GCgIlGhtx6mcavzzNMlTmzXQ4KiU14N6ymY9vH9zyIqXk3EsREq3mzKNNl8ORYq4oqUcc3uX9Mco7ngRMxhA1GdPqXPyWOy4p4VIBsUwQZY8n0DrT6Rjf5tCCltzysaeoBvo79gJbXGl20CgYB+/zqdLxkf6nxU45h2NvDHLXhabXJHhBapZ/sg5ZZ6d5B9AmMyPaspllsVTqvxBpkYH0/8MdRCa1+kNzEv+9W+IduTMRCQ+yzCtbE3gsBGcY388muJQJorKw9XO1R2lWiED0WhvMITZCBKr6wz8HRnYtRtrSK7s10PXkgmPjSYKQ==
```

When the data is encrypted, in transfer money page, we will send the result with post body in JSON format. After the result is obtained, it will be shown on the page.

<p float="left">
  <img src="https://github.com/sunnytse0326/MoneyTransfer/blob/develop/screenshot/screenshot1.png" width="250" height="400">
  <img src="https://github.com/sunnytse0326/MoneyTransfer/blob/develop/screenshot/screenshot2.png" width="250" height="400">
  <img src="https://github.com/sunnytse0326/MoneyTransfer/blob/develop/screenshot/screenshot3.png" width="250" height="400">
</p>

## Proguard rules
Proguard is already enabled and it is neccessary for this feature used obfuscation which makes codes difficult to decomplile.



## Reference:
[SSL Pinning](https://medium.com/@appmattus/android-security-ssl-pinning-1db8acb6621e)
[Android Storing Secret Data](https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3)
[OSWAP Android Data Storage](https://github.com/OWASP/owasp-mstg/blob/master/Document/0x05d-Testing-Data-Storage.md)
[Proguard Rules](https://medium.com/@maheshwar.ligade/enabling-proguard-for-android-98e2b19e90a4)








