```sh
docker build -t lifelong-ditsignature:0.0.1-beta.2-alpha.17 .
docker image tag lifelong-ditsignature:0.0.1-beta.2-alpha.17 hubcpa.ar.co.th:5000/lifelong-ditsignature:0.0.1-beta.2-alpha.17
docker push hubcpa.ar.co.th:5000/lifelong-ditsignature:0.0.1-beta.2-alpha.17

for uat
docker image tag lifelong-ditsignature:0.0.1-beta.2-alpha.17 hubcpa.swu.ac.th/lifelong-ditsignature:0.0.1-beta.2-alpha.17
docker push hubcpa.swu.ac.th/lifelong-ditsignature:0.0.1-beta.2-alpha.17
```

``` sh
kubectl apply -f deployment.yml
```

``` JKS
Convert Certificate and Key (Private) into a keystore file
When you have Certificate (.crt file) and Private key (.key file), which you want to convert into Keystore (.keystore file), please follow the below steps.

Basic flow: <.crt file> ➕ <.key file> ➡️ <.p12 file> ➡️ <.keystore file>
Step 1 - Convert Certificate, Key pair to PKCS#12 file
Consider you have the following Certificate, Key pair.
Certificate file name - abc_certificate.crt
Private key file name - xyz_private.key

Execute the below command with the details above to generate PKCS#12 (.p12 file).

openssl pkcs12 -export -in abc_certificate.crt -inkey xyz_private.key -out output_pkcs.p12
Output PKCS#12 file name - output_pkcs.p12
Step 2 - Convert PKCS#12 file to Keystore file
Once you have generated the PKCS#12 (.p12 file), execute the below command to generate the Keystore (.keystore file).

keytool -importkeystore -destkeystore final_keystore.keystore -srckeystore output_pkcs.p12 -srcstoretype pkcs12
Output Keystore file name - final_keystore.keystore

The final output file named "final_keystore.keystore" is the keystore file which is ready to use.
```


``` FOR REGISTER FILE JKS OR RENEW TO DB DIT....
@Test
void loadFileJks()
{
    jksg.registerCertificate();
}
```