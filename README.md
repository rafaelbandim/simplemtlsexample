For dev purposes<br>

Server:<br>

keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore server-keystore.p12 -validity 365 -storepass somepasswordhere -dname "CN=localhost, OU=Dev, O=Example, L=City, S=State, C=US"<br>
keytool -export -alias server -file mtlsserver.crt -keystore server-keystore.p12<br>
keytool -importcert -keystore cacerts -file mtlsserver.crt -alias server<br>


Client:<br>
keytool -genkeypair -alias client -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore client-keystore.p12 -validity 365 -storepass password -dname "CN=localhost, OU=Dev, O=Example, L=City, S=State, C=US"<br>
keytool -export -alias client -file client.crt -keystore client-keystore.p12<br>
keytool -importcert -keystore cacerts -file client -alias client
