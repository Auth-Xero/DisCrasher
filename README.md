
# DisCrasher

A website that generates videos to crash discord written in java using Spring Boot.
<br>

DISCLAIMER: THIS PROJECT IS INTENDED FOR EDUCATIONAL PURPOSES ONLY!

## How to use

Command Syntax:
```bash
java -jar discrasher.jar [port (defaults to 8080)] [debug FFMPEG (true|false)] [SSL (true|false)] [path to keystore in PKCS12 format] [keystore alias] [keystore password]
```
    
# Example Usage
Runs website on port 8080 without any debug information or ssl:
```bash
java -jar discrasher.jar
```
Runs website on port 80 without any debug information debug info, and without ssl:
```bash
java -jar discrasher.jar 80 true
```
Runs website on port 443 without ssl and no debug information:
```bash
java -jar discrasher.jar 443 false true discrasher.p12 discrasher ThisIsAWonderfulP@ssw0rd
```
<br>

Check out the site here: https://www.discrasher.xyz/
