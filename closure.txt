(Due to technical issues, the search service is temporarily unavailable.)

Integrating Google Closure Compiler with Thymeleaf, a server-side Java template engine, involves optimizing JavaScript files referenced in Thymeleaf templates during your build process. Below is a step-by-step guide and key considerations for combining these tools effectively:

---

### **Key Concepts**
1. **Google Closure Compiler**  
   Optimizes JavaScript (minifies, dead-code removal, type-checking). Runs during build time.
2. **Thymeleaf**  
   Processes HTML templates on the server to generate dynamic content. References static assets like JavaScript files.

---

### **Integration Workflow**
#### 1. **Project Structure**
Organize your project to separate static resources (JavaScript) from Thymeleaf templates:
```
src/
  main/
    resources/
      templates/       # Thymeleaf HTML files
      static/
        js/
          app.js       # Raw JavaScript
          min/         # Output for optimized JS (e.g., app.min.js)
```

#### 2. **Build Process Integration**
Use Closure Compiler during the build phase (e.g., with Maven/Gradle) to process JavaScript **before** packaging the application.

**Example with Maven:**
```xml
<plugin>
  <groupId>com.google.code.maven-closure-compiler</groupId>
  <artifactId>closure-compiler-maven-plugin</artifactId>
  <version>2.21.0</version>
  <executions>
    <execution>
      <phase>prepare-package</phase>
      <goals>
        <goal>compile</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <jsSourceDir>${project.basedir}/src/main/resources/static/js/</jsSourceDir>
    <jsOutputFile>${project.build.directory}/classes/static/js/min/app.min.js</jsOutputFile>
    <compilationLevel>ADVANCED</compilationLevel>
  </configuration>
</plugin>
```

#### 3. **Thymeleaf Template Updates**
Reference the minified JavaScript file in your Thymeleaf template:
```html
<script th:src="@{/js/min/app.min.js}"></script>
```

---

### **Critical Considerations**
#### **1. Avoiding Variable Renaming Conflicts**
If your JavaScript interacts with server-side data (e.g., Thymeleaf-injected values), use **externs** or quote properties to prevent Closure Compiler from breaking references:
```javascript
// Thymeleaf-injected variable (e.g., [[${data}]])
const serverData = JSON.parse(document.getElementById("data").textContent);

// Use quoted properties to prevent renaming
console.log(serverData['userRole']); // Safe with ADVANCED mode
```

#### **2. Handling Inline JavaScript**
Avoid inline JavaScript in Thymeleaf templates. Instead:
- Use `data-*` attributes to pass server-side values to external scripts.
- Process inline scripts separately with Closure Compiler if needed.

#### **3. Cache Busting**
Append versioning to JS file references to avoid stale caches:
```html
<script th:src="@{/js/min/app.min.js(v=${@environment.getProperty('app.version')})}"></script>
```

#### **4. Source Maps**
Generate source maps for debugging minified code:
```xml
<configuration>
  ...
  <createSourceMap>true</createSourceMap>
</configuration>
```

---

### **Example Workflow**
1. Write raw JavaScript in `src/main/resources/static/js/app.js`.
2. Closure Compiler minifies it to `static/js/min/app.min.js` during the build.
3. Thymeleaf templates reference the minified file.
4. Server-side data is safely passed via `data-*` attributes or JSON embedded in HTML.

---

### **Troubleshooting**
- **Broken References**: Use externs to preserve variable names or disable `ADVANCED` optimizations.
- **Missing Files**: Ensure Closure Compiler outputs to a directory included in the final build artifact (e.g., `target/classes/static` for Maven).
- **Caching**: Force browser cache refresh by updating file versions.

By combining Closure Compiler’s optimizations with Thymeleaf’s templating, you achieve both efficient client-side code and dynamic server-rendered content.


(Due to technical issues, the search service is temporarily unavailable.)

Here’s how to add a **development target without optimization** while using Google Closure Compiler with Thymeleaf. This setup allows you to:  
- **Skip minification** during development for easier debugging.  
- **Preserve original JavaScript** files for readability.  
- Use **Thymeleaf templates** to dynamically reference unoptimized or optimized files based on the build profile.

---

### **Step-by-Step Configuration**

#### 1. **Maven Profiles for Dev and Prod**
Define profiles in your `pom.xml` to control when Closure Compiler runs.  

```xml
<profiles>
  <!-- Development Profile (Default) -->
  <profile>
    <id>dev</id>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
    <build>
      <plugins>
        <!-- Copy raw JS files to the "min" directory (no optimization) -->
        <plugin>
          <artifactId>maven-resources-plugin</artifactId>
          <executions>
            <execution>
              <id>copy-js-for-dev</id>
              <phase>prepare-package</phase>
              <goals>
                <goal>copy-resources</goal>
              </goals>
              <configuration>
                <outputDirectory>${project.build.directory}/classes/static/js/min</outputDirectory>
                <resources>
                  <resource>
                    <directory>src/main/resources/static/js</directory>
                    <includes>
                      <include>**/*.js</include>
                    </includes>
                  </resource>
                </resources>
              </configuration>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
  </profile>

  <!-- Production Profile (Uses Closure Compiler) -->
  <profile>
    <id>prod</id>
    <build>
      <plugins>
        <plugin>
          <groupId>com.google.code.maven-closure-compiler</groupId>
          <artifactId>closure-compiler-maven-plugin</artifactId>
          <version>2.21.0</version>
          <executions>
            <execution>
              <phase>prepare-package</phase>
              <goals>
                <goal>compile</goal>
              </goals>
            </execution>
          </executions>
          <configuration>
            <jsSourceDir>src/main/resources/static/js</jsSourceDir>
            <jsOutputFile>${project.build.directory}/classes/static/js/min/app.min.js</jsOutputFile>
            <compilationLevel>ADVANCED</compilationLevel>
            <createSourceMap>true</createSourceMap>
          </configuration>
        </plugin>
      </plugins>
    </build>
  </profile>
</profiles>
```

#### 2. **Thymeleaf Template**
Reference the `min` directory in your template. The build process ensures the correct files (raw or optimized) are placed here:  
```html
<script th:src="@{/js/min/app.min.js}"></script>
```

---

### **How It Works**
- **Development Mode (`mvn package -Pdev` or default):**  
  - The `maven-resources-plugin` copies raw JavaScript files (e.g., `app.js`) to `static/js/min/app.min.js`.  
  - No optimization is applied, so you can debug with readable code.  

- **Production Mode (`mvn package -Pprod`):**  
  - Closure Compiler minifies and optimizes `app.js` into `app.min.js` in the same directory.  
  - Advanced optimizations (e.g., dead code removal) are applied.

---

### **Key Benefits**
1. **No Template Changes**  
   Thymeleaf always points to `app.min.js`, but the build process determines whether it’s optimized or raw.  
2. **Fast Development Workflow**  
   Skip Closure Compiler during development to speed up builds.  
3. **Debugging-Friendly**  
   Use unminified JS with source maps (if needed) for easier troubleshooting.  

---

### **Optional: Add a Debug Mode in Thymeleaf**
If you want to **conditionally load raw JS** in development (e.g., for live reloading), use a Thymeleaf expression with a Spring property:  
```html
<script th:src="${@environment.getProperty('app.env') == 'dev' ? 
  @{/js/app.js} : 
  @{/js/min/app.min.js}}"></script>
```

---

### **Run Commands**
- **Development:**  
  ```bash
  mvn package -Pdev  # Builds with raw JS
  ```
- **Production:**  
  ```bash
  mvn package -Pprod  # Builds with optimized JS
  ```

---

This setup keeps your development workflow simple while retaining the power of Closure Compiler for production optimizations.