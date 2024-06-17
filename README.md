Este proyecto cuenta con algunas funcionalidades principales de una aplicación de ventas de productos de comida y sobre todo bebidas online (el cual también aplica para ventas física), tales como:
-	Vender alcohol (de forma física y online)
-	Vender dulces (de forma física y online)
-	Vender gaseosas (de forma física y online)
-	Comprar alcohol
-	Comprar dulces
-	Comprar gaseosas
-	Ver los detalles de un producto
-	Ver los detalles de una venta
-	Ver los detalles de una compra
-	Registrar un producto 
-	Agregar (cantidad) un producto
-	Ver el detalle de una agregación de producto
-	Ver todas las ventas realizadas por empleados
-	Modificar los datos de una cuenta
-	Etc.

Algunas características de este proyecto son:

- Se realizó con el framework Angular conocido para creación de aplicaciones web de una sola página (SPA, por sus siglas en inglés)
- Las interfaces graficas se realizaron con la ayuda de la herramienta de desarrollo Bootstrap
- Se utilizaron Guards como middleware que se ejecutan antes de cargar una ruta específica en la aplicación. La principal función es determinar si el usuario tiene permisos suficientes para acceder a esa ruta en particular
- Se utilizaron Interceptors principalmente para inspeccionar y modificar las peticiones HTTP que la aplicación envía al servidor, así como las respuestas que recibe; permite realizar diversas operaciones como modificar encabezados HTTP, manipular el cuerpo de la solicitud, establecer tokens de autenticación o autorización, y manejar errores.
- Para la parte del backend se uso Servlet Filter la cual su función es interceptar y procesar solicitudes y respuestas HTTP antes de que lleguen a un servlet o después de que se generen. Por ejemplo: Restringir métodos según el rol del usuario, como permitir solo a pacientes crear citas y solo a administradores cerrar PQRS.
- Se usaron tokens garantiza la validez y seguridad de los datos con la ayuda de JSON Web Tokens (JWT).

Dentro la carpeta “Imagenes de su Funcionamiento” se encuentran algunas imágenes del proyecto en ejecución mostrando algunas funcionalidades tanto del cliente como del empleado y el administrador.	

This project has some main functionalities of an application for sales of food products and especially beverages online (which also applies to physical sales), such as:
- Sell alcohol (physically and online)
- Sell sweets (physically and online)
- Sell soft drinks (physically and online)
- Buy alcohol
-	Buy candies
- Buy soft drinks
- View the details of a product
- View the details of a sale
- View details of a purchase
- Register a product
- Add (quantity) a product
- View the details of a product aggregation
- View all sales made by employees
- Modify account details
-	Etc.

Some features of this project are:

- It was made with the Angular framework known for creating single page web applications (SPA)
- The graphical interfaces were made with the help of the Bootstrap development tool
- Guards were used as middleware that are executed before loading a specific route in the application. The main function is to determine if the user has sufficient permissions to access that particular route
- Interceptors were mainly used to inspect and modify the HTTP requests that the application sends to the server, as well as the responses it receives; allows you to perform various operations such as modifying HTTP headers, manipulating the request body, setting authentication or authorization tokens, and handling errors.
- For the backend part, Servlet Filter was used, whose function is to intercept and process HTTP requests and responses before they reach a servlet or after they are generated. For example: Restrict methods based on the user's role, such as allowing only patients to create appointments and only allowing administrators to close PQRS.
- Tokens were used to ensure the validity and security of the data with the help of JSON Web Tokens (JWT).

Within the “Images of its Operation” folder there are some images of the project in execution showing some functionalities of both the client, the employee and the administrator.
