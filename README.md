# testng-api-java

Steps to setup
<details>
  <summary>Windows Setup</summary>

### Install Java 21
- For windows using powershell run command :

```text
winget install --id EclipseAdoptium.Temurin.21.JDK -e
```

- Restart powershell to check java version command :

```text
java -version
```

- Add Java 21 to IntelliJ
- Go to File → Project Structure
- Under Project settings:
  Change Project SDK to Java 21.
  Change Project language level to SDK default
- Under Modules:
  Select your module and set SDK to Java 21 (eclipse temurin).
- Restart Intellij and check java version in terminal command :

```text
java -version

   Output should show :  
   version "21.0.6" 2025-01-21 LTS  
   OpenJDK Runtime Environment Temurin-21.0.6+7 (build 21.0.6+7-LTS)
```

### Setup Apache Maven:

- Visit the Apache Maven download page.
- Download the Binary zip archive (e.g., apache-maven-3.x.x-bin.zip).
- Extract the archive to a directory of your choice, e.g., C:\Program Files\Apache\Maven.
- Set Up Maven Environment Variables:
    - After extracting Maven, you need to set up the MAVEN_HOME environment variable and update the
      Path to include Maven's bin directory.
    - Add Maven’s bin directory to the system Path
- Open a new PowerShell window (to make sure the environment variables are loaded) and run:

```text
mvn -version
```

- Optional: Set Proxy Settings (if behind a proxy):
    - If you are working behind a proxy, you may need to configure Maven's settings.xml file with
      your proxy settings.
    - Edit the settings.xml file located at: C:\Users\<YourUser>\.m2\settings.xml
    - Add the proxy configuration (if applicable):

```xml

<proxies>
  <proxy>
    <id>example-proxy</id>
    <active>true</active>
    <protocol>http</protocol>
    <host>proxy.example.com</host>
    <port>8080</port>
    <username>proxyuser</username>
    <password>somepassword</password>
    <nonProxyHosts>www.google.com|*.example.com</nonProxyHosts>
  </proxy>
</proxies>
```
### Setup Allure:

- Install Scoop (if not already installed):
```text
iwr -useb get.scoop.sh | iex
```
- Install Allure:
```text
scoop install allure
```
- Check allure version
```text
allure --version
```
- Restart IDEA and check allure version inside IDEA terminal again
- Run any test case and check allure report with terminal command :
```text
allure serve target/allure-results
```

</details>

<details>
  <summary>Mac Setup</summary>
</details>

### Setup Google Java Style Guide configuration for IntelliJ IDEA
- Go to File → Settings
  Navigate to Editor → Code Style.
  In the Code Style section, select Java from the language list.
  Click the gear icon ⚙ next to the Scheme dropdown.
  Select Import Scheme → IntelliJ IDEA Code Style XML.
  Go to this project folder -> lib -> intellij-java-google-style.xml -> Click OK
  Ensure the new Google Style scheme is selected.
  Click Apply, then OK.
- Now you can reformat you code using (Menu Bar -> Code -> Reformat Code)

### How to run the tests locally

- Compile the project

```text
mvn clean package -DskipTests
```

- To execute tests in suite file

```text
mvn clean test -DsuiteXmlFile="testng_sample.xml"
```

### How to execute single test class method

- To execute tests with custom profile configuration - Single Test Execution

```text
mvn clean test -Dtest="SampleTest#test01"
```