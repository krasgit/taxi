https://medium.com/@tayeblagha/create-free-ssl-certificate-and-configure-it-for-spring-boot-web-applications-a2106d97b733

To set the **initial (minimum)**, **maximum**, and **idle** connections in a connection pool, you need to configure the respective properties of the connection pool library you're using. Below, I'll explain 
how to set these values for **HikariCP**, one of the most popular connection pooling libraries for Java.

---

### Key Properties for Connection Pool Configuration:
1. **Minimum (Initial) Connections**: The number of connections the pool will start with.
2. **Maximum Connections**: The maximum number of connections the pool will allow.
3. **Idle Connections**: The number of connections that can remain idle in the pool.

---

### Example: Configuring HikariCP

Here’s how you can configure these properties in HikariCP:

```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPExample {

    private static HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/yourdatabase");
        config.setUsername("yourusername");
        config.setPassword("yourpassword");

        // Set minimum (initial) connections
        config.setMinimumIdle(5); // Default is same as maximumPoolSize

        // Set maximum connections
        config.setMaximumPoolSize(20); // Default is 10

        // Set idle connections
        config.setIdleTimeout(30000); // 30 seconds (time before idle connections are closed)

        // Other optional settings
        config.setConnectionTimeout(30000); // 30 seconds (time to wait for a connection)
        config.setMaxLifetime(1800000); // 30 minutes (maximum lifetime of a connection)

        ds = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            System.out.println("Connection successful!");
            // Use the connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

### Explanation of Key Properties:
1. **`setMinimumIdle(int)`**:
   - Sets the minimum number of idle connections that the pool will maintain.
   - Default: Same as `maximumPoolSize`.
   - Example: `config.setMinimumIdle(5);`

2. **`setMaximumPoolSize(int)`**:
   - Sets the maximum number of connections that the pool will create.
   - Default: 10.
   - Example: `config.setMaximumPoolSize(20);`

3. **`setIdleTimeout(long)`**:
   - Sets the maximum time (in milliseconds) that a connection can remain idle in the pool before being closed.
   - Default: 600000 (10 minutes).
   - Example: `config.setIdleTimeout(30000);` (30 seconds)

4. **`setConnectionTimeout(long)`**:
   - Sets the maximum time (in milliseconds) that a connection request will wait before timing out.
   - Default: 30000 (30 seconds).
   - Example: `config.setConnectionTimeout(30000);`

5. **`setMaxLifetime(long)`**:
   - Sets the maximum lifetime (in milliseconds) of a connection in the pool.
   - Default: 1800000 (30 minutes).
   - Example: `config.setMaxLifetime(1800000);`

---

### Best Practices:
- **Minimum Idle Connections**: Set this to a value that matches your application's average load. This ensures that connections are readily available when needed.
- **Maximum Connections**: Set this based on your database's capacity and the expected peak load of your application.
- **Idle Timeout**: Set this to a reasonable value to avoid holding onto idle connections for too long, which can waste resources.
- **Connection Timeout**: Ensure this is set high enough to avoid timeouts during peak load but low enough to fail fast in case of issues.

---

### Other Connection Pool Libraries:
If you're using a different connection pool library (e.g., Apache DBCP, C3P0), the configuration properties will be similar but with different names. For example:
- **Apache DBCP**:
  - `setInitialSize(int)` for initial connections.
  - `setMaxTotal(int)` for maximum connections.
  - `setMaxIdle(int)` for idle connections.
- **C3P0**:
  - `setMinPoolSize(int)` for initial connections.
  - `setMaxPoolSize(int)` for maximum connections.
  - `setMaxIdleTime(int)` for idle connections.

Let me know if you need help with a specific library!
If the `county` field is missing in the OpenStreetMap (OSM) JSON data, you can handle it similarly to other missing fields by providing a fallback value or skipping it in the final address formatting. The `county` field is typically used to represent the county or administrative division within a country (e.g., "Cook County" in the USA or "Oxfordshire" in the UK). If it's missing, you can either skip it or provide a default value like "Unknown County".

Below is an updated version of the Python code that includes handling for a missing `county` field.

### Updated Python Code with `county`

```python
import json

# Sample OpenStreetMap JSON data with missing county, country, street, district, name, and housenumber
osm_json = '''
{
  "address": {
    "road": "Main Street",
    "city": "Springfield",
    "postcode": "62701"
  }
}
'''

# Load the JSON data
data = json.loads(osm_json)

# Extract address components with fallback for missing fields
address = data.get('address', {})
name = address.get('name', 'N/A')  # Fallback to 'N/A' if missing
housenumber = address.get('housenumber', 'N/A')  # Fallback to 'N/A' if missing
street = address.get('street', 'N/A')  # Fallback to 'N/A' if missing
road = address.get('road', 'N/A')  # Fallback to 'N/A' if missing
district = address.get('district', 'N/A')  # Fallback to 'N/A' if missing
county = address.get('county', 'Unknown County')  # Fallback to 'Unknown County' if missing
city = address.get('city', 'N/A')  # Fallback to 'N/A' if missing
state = address.get('state', 'N/A')  # Fallback to 'N/A' if missing
postcode = address.get('postcode', 'N/A')  # Fallback to 'N/A' if missing
country = address.get('country', 'Unknown Country')  # Fallback to 'Unknown Country' if missing

# If street is missing, fall back to combining housenumber and road
if street == 'N/A' and (housenumber != 'N/A' or road != 'N/A'):
    street = f"{housenumber} {road}".strip()  # Combine housenumber and road, remove extra spaces

# Format the address, skipping undefined or 'N/A' fields
address_parts = []
if name != 'N/A':
    address_parts.append(name)
if street != 'N/A':
    address_parts.append(street)
if district != 'N/A':
    address_parts.append(district)
if county != 'Unknown County':  # Include county only if it's not the fallback value
    address_parts.append(county)
if city != 'N/A':
    address_parts.append(city)
if state != 'N/A':
    address_parts.append(state)
if postcode != 'N/A':
    address_parts.append(postcode)
if country != 'Unknown Country':  # Include country only if it's not the fallback value
    address_parts.append(country)

formatted_address = ', '.join(address_parts)

print(formatted_address)
```

### Output
```
Main Street, Springfield, 62701
```

### Explanation of Changes
1. **Added `county`**: The `county` field is extracted from the JSON data. If it's missing, it defaults to `'Unknown County'`.
2. **Conditional Inclusion**: The `county` is included in the address only if it is not `'Unknown County'`.
3. **Dynamic Construction**: The address is built dynamically by appending valid fields to a list and joining them with `', '`.

### Handling `county` in JavaScript
If you're working in JavaScript, here's how you can handle a missing `county`:

```javascript
const osmJson = `{
  "address": {
    "road": "Main Street",
    "city": "Springfield",
    "postcode": "62701"
  }
}`;

const data = JSON.parse(osmJson);
const address = data.address || {};

const name = address.name || 'N/A';
const housenumber = address.housenumber || 'N/A';
const street = address.street || 'N/A';
const road = address.road || 'N/A';
const district = address.district || 'N/A';
const county = address.county || 'Unknown County';
const city = address.city || 'N/A';
const state = address.state || 'N/A';
const postcode = address.postcode || 'N/A';
const country = address.country || 'Unknown Country';

// If street is missing, fall back to combining housenumber and road
let finalStreet = street;
if (street === 'N/A' && (housenumber !== 'N/A' || road !== 'N/A')) {
    finalStreet = `${housenumber} ${road}`.trim(); // Combine housenumber and road, remove extra spaces
}

const addressParts = [name, finalStreet, district, county, city, state, postcode, country]
  .filter(part => part !== 'N/A' && part !== 'Unknown County' && part !== 'Unknown Country'); // Remove 'N/A' and fallback values

const formattedAddress = addressParts.join(', ');

console.log(formattedAddress);
```

### Output (JavaScript)
```
Main Street, Springfield, 62701
```

### Key Points
- Use fallback values (`'N/A'`, `'Unknown County'`, or `'Unknown Country'`) to handle missing fields like `county`, `country`, `street`, `district`, `name`, and `housenumber`.
- If `street` is missing, construct it dynamically using `housenumber` and `road`.
- Dynamically construct the address by filtering out invalid or placeholder values.
- This approach ensures your code doesn't break and produces clean, readable output even with incomplete data.

Let me know if you need further assistance! 😊
If the `street` field is missing in the OpenStreetMap (OSM) JSON data, you can handle it similarly to other missing fields by providing a fallback value or skipping it in the final address formatting. The `street` field is often used to represent the full street name (e.g., "Main Street"), and if it's missing, you can fall back to combining other fields like `road` and `housenumber`.

Below is an updated version of the Python code that includes handling for a missing `street` field.

### Updated Python Code with `street`

```python
import json

# Sample OpenStreetMap JSON data with missing street, district, name, and housenumber
osm_json = '''
{
  "address": {
    "road": "Main Street",
    "city": "Springfield",
    "postcode": "62701"
  }
}
'''

# Load the JSON data
data = json.loads(osm_json)

# Extract address components with fallback for missing fields
address = data.get('address', {})
name = address.get('name', 'N/A')  # Fallback to 'N/A' if missing
housenumber = address.get('housenumber', 'N/A')  # Fallback to 'N/A' if missing
street = address.get('street', 'N/A')  # Fallback to 'N/A' if missing
road = address.get('road', 'N/A')  # Fallback to 'N/A' if missing
district = address.get('district', 'N/A')  # Fallback to 'N/A' if missing
city = address.get('city', 'N/A')  # Fallback to 'N/A' if missing
state = address.get('state', 'N/A')  # Fallback to 'N/A' if missing
postcode = address.get('postcode', 'N/A')  # Fallback to 'N/A' if missing
country = address.get('country', 'N/A')  # Fallback to 'N/A' if missing

# If street is missing, fall back to combining housenumber and road
if street == 'N/A' and (housenumber != 'N/A' or road != 'N/A'):
    street = f"{housenumber} {road}".strip()  # Combine housenumber and road, remove extra spaces

# Format the address, skipping undefined or 'N/A' fields
address_parts = []
if name != 'N/A':
    address_parts.append(name)
if street != 'N/A':
    address_parts.append(street)
if district != 'N/A':
    address_parts.append(district)
if city != 'N/A':
    address_parts.append(city)
if state != 'N/A':
    address_parts.append(state)
if postcode != 'N/A':
    address_parts.append(postcode)
if country != 'N/A':
    address_parts.append(country)

formatted_address = ', '.join(address_parts)

print(formatted_address)
```

### Output
```
Main Street, Springfield, 62701
```

### Explanation of Changes
1. **Added `street`**: The `street` field is extracted from the JSON data. If it's missing, it defaults to `'N/A'`.
2. **Fallback Logic**: If `street` is missing, the code attempts to construct it by combining `housenumber` and `road`. This ensures that the street information is still included if possible.
3. **Conditional Inclusion**: The `street` (or its fallback value) is included in the address only if it is not `'N/A'`.
4. **Dynamic Construction**: The address is built dynamically by appending valid fields to a list and joining them with `', '`.

### Handling `street` in JavaScript
If you're working in JavaScript, here's how you can handle a missing `street`:

```javascript
const osmJson = `{
  "address": {
    "road": "Main Street",
    "city": "Springfield",
    "postcode": "62701"
  }
}`;

const data = JSON.parse(osmJson);
const address = data.address || {};

const name = address.name || 'N/A';
const housenumber = address.housenumber || 'N/A';
const street = address.street || 'N/A';
const road = address.road || 'N/A';
const district = address.district || 'N/A';
const city = address.city || 'N/A';
const state = address.state || 'N/A';
const postcode = address.postcode || 'N/A';
const country = address.country || 'N/A';

// If street is missing, fall back to combining housenumber and road
let finalStreet = street;
if (street === 'N/A' && (housenumber !== 'N/A' || road !== 'N/A')) {
    finalStreet = `${housenumber} ${road}`.trim(); // Combine housenumber and road, remove extra spaces
}

const addressParts = [name, finalStreet, district, city, state, postcode, country]
  .filter(part => part !== 'N/A'); // Remove 'N/A' values

const formattedAddress = addressParts.join(', ');

console.log(formattedAddress);
```

### Output (JavaScript)
```
Main Street, Springfield, 62701
```

### Key Points
- Use fallback values (`'N/A'` or similar) to handle missing fields like `street`, `district`, `name`, and `housenumber`.
- If `street` is missing, construct it dynamically using `housenumber` and `road`.
- Dynamically construct the address by filtering out invalid or placeholder values.
- This approach ensures your code doesn't break and produces clean, readable output even with incomplete data.

Let me know if you need further assistance!

keytool -genkeypair -alias baeldung -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore baeldung.p12 -validity 3650

-Dmaven.test.skip=true skips

CONTAINER ID   IMAGE                             COMMAND                  CREATED        STATUS        PORTS                                                 NAMES
7d23bb530297   osrm/osrm-backend                 "osrm-routed --algor…"   21 hours ago   Up 21 hours   0.0.0.0:5000->5000/tcp, :::5000->5000/tcp             practical_thompson
ba3e688888da   overv/openstreetmap-tile-server   "/run.sh run"            46 hours ago   Up 46 hours   5432/tcp, 0.0.0.0:8080->80/tcp, :::8080->80/tcp       adoring_shockley
acd42120918f   mediagis/nominatim:4.2            "/app/start.sh"          5 days ago     Up 45 hours   5432/tcp, 0.0.0.0:8180->8080/tcp, :::8180->8080/tcp   nominatim



bulgaria-latest.osm.pbf

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)


docker volume rm $(docker volume ls  -q)

docker system prune -a
mkdir dokers
cd dokers

1) copy file  bulgaria-latest.osm.pbf
2) Run osrm/osrm-backend
	1)	docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-extract -p /opt/car.lua /data/bulgaria-latest.osm.pbf
	2)  docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-partition /data/bulgaria-latest.osm.pbf
	3)  docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-customize /data/bulgaria-latest.osm.pbf
	4)  docker run -t -i -p 5000:5000 -v "${PWD}:/data" osrm/osrm-backend osrm-routed --algorithm mld /data/bulgaria-latest.osm.pbf
    	
		Test Make requests against the HTTP server
			curl "http://127.0.0.1:5000/route/v1/driving/13.388860,52.517037;13.385983,52.496891?steps=true"

3) Run overv/openstreetmap-tile-server
	1)	docker volume create osm-data
	2)  docker run  -v "${PWD}/bulgaria-latest.osm.pbf:/data/region.osm.pbf"  -v osm-data:/data/database/  overv/openstreetmap-tile-server  import
	3) 	docker run -p 8080:80   -p 5432:5432 -v osm-data:/data/database/ -e ALLOW_CORS=enabled -d overv/openstreetmap-tile-server run
	      
	      -e PGPASSWORD=secret
	      
		Test tile server
	4)	curl -k -L --output tile-3005.png "http://localhost:8080/tile/13/4731/3005.png"
	 
	 psql localhost:5432  DBname:gis DBuser:renderer DBpassword:renderer
	
	
		
	 
	
	

4)  Run	mediagis/nominatim:4.2
	1)docker run -it -e PBF_PATH=/nominatim/data/bulgaria-latest.osm.pbf -p 5433:5432 -p 8181:8080 -v "${PWD}:/nominatim/data"  -e NOMINATIM_PASSWORD=very_secure_password --name nominatim mediagis/nominatim:4.2
	
	Note!!! where the /osm-maps/data/ directory contains monaco-latest.osm.pbf file that is mounted and available in container: /nominatim/data/monaco-latest.osm.pbf
	
	 
	Test
	curl 'http://localhost:8181/http://127.0.0.1:8181/search?q=varna&limit=5&format=json&addressdetails=1'
	
	 psql localhost:5433  DBname:nominatim DBuser:nominatim DBpassword:very_secure_password
	
	


git clone https://github.com/krasgit/taxi.git


add hiips 
keytool -genkey -alias https-example -storetype JKS -keyalg RSA -keysize 2048 -validity 365 -keystore https-example.jks

cp https-example.jks to /src/main/resources

add  in app.prop
server.port=8443

server.ssl.key-alias=https-example
server.ssl.key-store-type=JKS
server.ssl.key-password=qazqaz
server.ssl.key-store=classpath:https-example.jks

----------------
security

docker run --name myzerotier --rm --cap-add NET_ADMIN --device /dev/net/tun zerotier/zerotier:latest abcdefdeadbeef00



https://javatechonline.com/how-to-implement-security-in-spring-boot-project/

get server ip
sudo zerotier-cli listnetworks

   serviceUrl: 'http://localhost:8282/http://127.0.0.1:8181/',
# Spring PetClinic Sample Application [![Build Status](https://github.com/spring-projects/spring-petclinic/actions/workflows/maven-build.yml/badge.svg)](https://github.com/spring-projects/spring-petclinic/actions/workflows/maven-build.yml)

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/spring-projects/spring-petclinic) [![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://github.com/codespaces/new?hide_repo_select=true&ref=main&repo=7517918)

## Understanding the Spring Petclinic application with a few diagrams

[See the presentation here](https://speakerdeck.com/michaelisvy/spring-petclinic-sample-application)

## Run Petclinic locally

Spring Petclinic is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/) or [Gradle](https://spring.io/guides/gs/gradle/). You can build a jar file and run it from the command line (it should work just as well with Java 17 or newer):

```bash
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
./mvnw package
java -jar target/*.jar
```

You can then access the Petclinic at <http://localhost:8080/>.

<img width="1042" alt="petclinic-screenshot" src="https://cloud.githubusercontent.com/assets/838318/19727082/2aee6d6c-9b8e-11e6-81fe-e889a5ddfded.png">

Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this, it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```bash
./mvnw spring-boot:run
```

> NOTE: If you prefer to use Gradle, you can build the app using `./gradlew build` and look for the jar file in `build/libs`.

## Building a Container

There is no `Dockerfile` in this project. You can build a container image (if you have a docker daemon) using the Spring Boot build plugin:

```bash
./mvnw spring-boot:build-image
```

## In case you find a bug/suggested improvement for Spring Petclinic

Our issue tracker is available [here](https://github.com/spring-projects/spring-petclinic/issues).

## Database configuration

In its default configuration, Petclinic uses an in-memory database (H2) which
gets populated at startup with data. The h2 console is exposed at `http://localhost:8080/h2-console`,
and it is possible to inspect the content of the database using the `jdbc:h2:mem:<uuid>` URL. The UUID is printed at startup to the console.

A similar setup is provided for MySQL and PostgreSQL if a persistent database configuration is needed. Note that whenever the database type changes, the app needs to run with a different profile: `spring.profiles.active=mysql` for MySQL or `spring.profiles.active=postgres` for PostgreSQL.

You can start MySQL or PostgreSQL locally with whatever installer works for your OS or use docker:

```bash
docker run -e MYSQL_USER=petclinic -e MYSQL_PASSWORD=petclinic -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:8.4
```

or

```bash
docker run -e POSTGRES_USER=petclinic -e POSTGRES_PASSWORD=petclinic -e POSTGRES_DB=petclinic -p 5432:5432 postgres:16.3
```

Further documentation is provided for [MySQL](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources/db/mysql/petclinic_db_setup_mysql.txt)
and [PostgreSQL](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources/db/postgres/petclinic_db_setup_postgres.txt).

Instead of vanilla `docker` you can also use the provided `docker-compose.yml` file to start the database containers. Each one has a profile just like the Spring profile:

```bash
docker-compose --profile mysql up
```

or

```bash
docker-compose --profile postgres up
```

## Test Applications

At development time we recommend you use the test applications set up as `main()` methods in `PetClinicIntegrationTests` (using the default H2 database and also adding Spring Boot Devtools), `MySqlTestApplication` and `PostgresIntegrationTests`. These are set up so that you can run the apps in your IDE to get fast feedback and also run the same classes as integration tests against the respective database. The MySql integration tests use Testcontainers to start the database in a Docker container, and the Postgres tests use Docker Compose to do the same thing.

## Compiling the CSS

There is a `petclinic.css` in `src/main/resources/static/resources/css`. It was generated from the `petclinic.scss` source, combined with the [Bootstrap](https://getbootstrap.com/) library. If you make changes to the `scss`, or upgrade Bootstrap, you will need to re-compile the CSS resources using the Maven profile "css", i.e. `./mvnw package -P css`. There is no build profile for Gradle to compile the CSS.

## Working with Petclinic in your IDE

### Prerequisites

The following items should be installed in your system:

- Java 17 or newer (full JDK, not a JRE)
- [Git command line tool](https://help.github.com/articles/set-up-git)
- Your preferred IDE
  - Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is
  not there, follow the install process [here](https://www.eclipse.org/m2e/)
  - [Spring Tools Suite](https://spring.io/tools) (STS)
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
  - [VS Code](https://code.visualstudio.com)

### Steps

1. On the command line run:

    ```bash
    git clone https://github.com/spring-projects/spring-petclinic.git
    ```

1. Inside Eclipse or STS:

    Open the project via `File -> Import -> Maven -> Existing Maven project`, then select the root directory of the cloned repo.

    Then either build on the command line `./mvnw generate-resources` or use the Eclipse launcher (right-click on project and `Run As -> Maven install`) to generate the CSS. Run the application's main method by right-clicking on it and choosing `Run As -> Java Application`.

1. Inside IntelliJ IDEA:

    In the main menu, choose `File -> Open` and select the Petclinic [pom.xml](pom.xml). Click on the `Open` button.

    - CSS files are generated from the Maven build. You can build them on the command line `./mvnw generate-resources` or right-click on the `spring-petclinic` project then `Maven -> Generates sources and Update Folders`.

    - A run configuration named `PetClinicApplication` should have been created for you if you're using a recent Ultimate version. Otherwise, run the application by right-clicking on the `PetClinicApplication` main class and choosing `Run 'PetClinicApplication'`.

1. Navigate to the Petclinic

    Visit [http://localhost:8080](http://localhost:8080) in your browser.

## Looking for something in particular?

|Spring Boot Configuration | Class or Java property files  |
|--------------------------|---|
|The Main Class | [PetClinicApplication](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java) |
|Properties Files | [application.properties](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources) |
|Caching | [CacheConfiguration](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/system/CacheConfiguration.java) |

## Interesting Spring Petclinic branches and forks

The Spring Petclinic "main" branch in the [spring-projects](https://github.com/spring-projects/spring-petclinic)
GitHub org is the "canonical" implementation based on Spring Boot and Thymeleaf. There are
[quite a few forks](https://spring-petclinic.github.io/docs/forks.html) in the GitHub org
[spring-petclinic](https://github.com/spring-petclinic). If you are interested in using a different technology stack to implement the Pet Clinic, please join the community there.

## Interaction with other open-source projects

One of the best parts about working on the Spring Petclinic application is that we have the opportunity to work in direct contact with many Open Source projects. We found bugs/suggested improvements on various topics such as Spring, Spring Data, Bean Validation and even Eclipse! In many cases, they've been fixed/implemented in just a few days.
Here is a list of them:

| Name | Issue |
|------|-------|
| Spring JDBC: simplify usage of NamedParameterJdbcTemplate | [SPR-10256](https://jira.springsource.org/browse/SPR-10256) and [SPR-10257](https://jira.springsource.org/browse/SPR-10257) |
| Bean Validation / Hibernate Validator: simplify Maven dependencies and backward compatibility |[HV-790](https://hibernate.atlassian.net/browse/HV-790) and [HV-792](https://hibernate.atlassian.net/browse/HV-792) |
| Spring Data: provide more flexibility when working with JPQL queries | [DATAJPA-292](https://jira.springsource.org/browse/DATAJPA-292) |

## Contributing

The [issue tracker](https://github.com/spring-projects/spring-petclinic/issues) is the preferred channel for bug reports, feature requests and submitting pull requests.

For pull requests, editor preferences are available in the [editor config](.editorconfig) for easy use in common text editors. Read more and download plugins at <https://editorconfig.org>. If you have not previously done so, please fill out and submit the [Contributor License Agreement](https://cla.pivotal.io/sign/spring).

## License

The Spring PetClinic sample application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).
-- public.people definition

-- Drop table

-- DROP TABLE public.people;

drop  TABLE Person 

create  TABLE Person (
	id int4 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	age int4 NULL,
	firstname text NULL,
	lastname text NULL,
	token text NULL,
	CONSTRAINT people_pkey PRIMARY KEY (id)
);
commit;


SELECT count(*) FROM Person WHERE firstname = '' AND token = '?'

update Person set token =null


drop  TABLE orders 

create  TABLE orders  (
	id int4 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	clientId int4  NOT NULL,
	taxiId int4   NULL,
	state int NULL,
	route text NULL,
	createTime date NULL,
	CONSTRAINT order_pkey PRIMARY KEY (id)
);

https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/jdbc.html
https://worlds-slowest.dev/posts/rpc-using-websockets/
