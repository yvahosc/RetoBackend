# RetoBackEndSophos

## Proyecto creado para dar solución a los requerimientos planteados dentro del reto Back End. Dichos requerimientos se pueden encontrar en el archivo ubicado en la dirección RetoBackend/demo/Reto Backend Bootcamp Full Stack.docx

### Es posible ejecutar el código a traves de un IDE (por ejemplo Eclipse) descargando el proyecto, extrayéndolo del archivo .ZIP, importándolo en el IDE y ejecutándolo como Spring Boot App.

#### Para importar el proyecto es necesario:
* Abrir el IDE de Eclipse
* Dar clic en File
* Dar clic en Import
* Seleccionar Projects from Folder or Archive dentro de la carpeta General
* Dar clic en Next
* En Directory ir a la carpeta Demo que se encuentra dentro del archivo .ZIP que se descargo y descomprimió desde el repositorio de GitHub
* Dar clic en Finish
* Esperar que el proyecto se cargue completamente al espacio de trabajo

#### Para ejecutar el proyecto es necesario:
* Dar clic derecho sobre la carpeta Demo que es la que contiene el proyecto (en el Package Explorer)
* Seleccionar Spring Boot App en la opción Run As

#### Para que el proyecto se conecte de la manera correcta a la base de datos es necesario que la información agregada en el archivo application.properties sea la correcta
* En este proyecto se trabajó con mySQL (es necesario tenerlo instalado en el equipo en el que se quiera ejecutar el proyecto)
* Es necesario ingresar datos de username y password correctos (con los que se cuente en el equipo) dentro del archivo application.properties
* Es necesario que se cuente con un esquema de base de datos llamado agenda_citas_laboratorio como se indica en el archivo application.properties

#### Para realizar las peticiones HTTP en Postman pueden importarse las colecciones que se encuentran en la ruta RetoBackend/CollectionsPostman/
* Estas colecciones se importan en Postman dando clic en File, import y seleccionando los archivos de la ubicación donde haya sido descargado y descomprimido el proyecto
