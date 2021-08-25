# Prueba_Mercado_Libre
 
 Aplicación desarrollada en IDE:
 Spring Tool Suite 4 
 
 Version: 4.10.0.RELEASE
 Build Id: 202103111225
 
 JDK 
 JRE AdoptOpenJDK\jdk-11.0.11.9-hotspot
 
 Url pruebas locales :
 URL de consumo de servicio expuesto por mercado libre:
 * http://localhost:8080/v1/api/coupon
 
 URL con las lista de pruebas proporcionadas en la prueba:  
 * http://localhost:8080/v1/api/test/coupon
 
 Documentación Generica Servicio Expuesto Swagger
 * http://localhost:8080/v3/api-docs
 * http://localhost:8080/swagger-ui/index.html?url=/v3/api-docs 
 
  Se anexa documento de despliege del aplicativo con imagenes y paso a paso de como realizarlo, con el nombre de  Documentación Pruebas Servicio.pdf
 
 URL Publicas del servicio
 
 * https://my-project-1543950798665.ey.r.appspot.com/v1/api/coupon
 * https://my-project-1543950798665.ey.r.appspot.com/v1/api/test/coupon
 
 Documentación Generica Servicio Expuesto Swagger
 * https://my-project-1543950798665.ey.r.appspot.com/v3/api-docs
 * https://my-project-1543950798665.ey.r.appspot.com/swagger-ui/index.html?url=/v3/api-docs 
 
 Pruebas de consumo realizadas desde PostMan y pruebas unitarias desde JUnit
 * Documento Pruebas Servicio.postman_collection.json
 
 
 Nota: bucket configurado  para realizar 20 consumos como maximo en un minuto por temas de consumo en la URL publica expuesta en App Engine para realizar envios de mas peticiones modificar el archivo /PruebaMeLi/src/main/java/co/com/dakero/serviciomimpl/RateLimitService.java
 
 
