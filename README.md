# FileEncryption
Proyecto final para la clase de cyberseguridad - G3 - 2021-1
Universidad Icesi
Proyecto escrito en Java 11
Laura Hincapié, Jhon Mora, Lizeth Varela

#Descripción del proyecto
El programa es un sistema de encriptado/desencriptado de archivos utilizando el algoritmo de generación de claves PBKDF2 y el algoritmo AES para la encriptación.

El algoritmo PBKDF2 es un algoritmo para derivación de claves utilizado para reducir la vulnerabilidad a ataques de fuerza bruta. El algoritmo aplica funciones pseudoaleatorias a una clave de entrada, junto a un valor de salt predefinido, y repite el proceso un número dado de veces para producir una clave derivada. Esta nueva clave derivada puede entonces ser usada cómo una llave criptógrafica para operaciones subsecuentes.

El algoritmo AES es un algoritmo de encriptación que utiliza una clave de 128, 192 o 256 bits, basandose en bloques de 128 bits.
El programa tiene actualmente 2 opciones: cifrado y descifrado. Para el cifrado, el programa recibe un archivo dado por el usuario, y una contraseña; esta contraseña es entonces pasada por el algoritmo PBKDF2, y el resultado es utilizado cómo la clave de encriptación para el algoritmo AES. Esto produce un archivo .cif con el resultado del cifrado. Adicional al resultado, también produce un archivo .hash, que corresponde al hash SHA-1 del archivo previo a su cifrado.

En la opción de decifrado, el programa pide inicialmente el archivo ya cifrado, y la contraseña que se utilizó para su encriptación. Con esto, procede a calcular la clave de cifrado por el algoritmo PBKDF2, y descifra el archivo al contenido correspondiente.

