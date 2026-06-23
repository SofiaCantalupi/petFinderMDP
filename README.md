
#  Pet Finder MDP

**Autores:** 
Benavides Leonel, Cantalupi Sofía, Cancino Franco, Herrera Daniel.

---

##  Descripción del proyecto

**PetFinder** es un sistema web orientado a facilitar la **publicación, búsqueda y visualización de reportes** relacionados con mascotas perdidas o encontradas dentro del ámbito urbano.

A través de una interfaz gráfica accesible y sencilla, los usuarios podrán:

- Registrarse en el sistema
- Realizar publicaciones asociadas a eventos de pérdida o hallazgo de mascotas
- Añadir información relevante como imágenes, descripciones y ubicación aproximada
- Visualizar y filtrar publicaciones existentes mediante un muro interactivo y un **mapa georreferenciado**

>  **Limitaciones del sistema:**  
> PetFinder **no incluye** seguimiento en tiempo real de animales, ni coordinación de encuentros entre usuarios. La exactitud de la ubicación provista en las publicaciones no está garantizada, y los acuerdos entre usuarios quedan fuera de responsabilidad del sistema.

---

##  Objetivo

Proporcionar una aplicación web que facilite la **búsqueda y recuperación de mascotas perdidas** mediante la colaboración entre miembros de la comunidad.

Los usuarios, denominados **miembros**, podrán publicar reportes sobre mascotas encontradas o perdidas, permitiendo que otros usuarios los visualicen y puedan contactar al publicador si tienen información útil.

---

##  Funcionalidades principales

-  Visualización de publicaciones de mascotas perdidas o encontradas  
-  Filtros personalizados para facilitar la búsqueda  
-  Creación y gestión de publicaciones con información relevante  
-  Sistema de comentarios para intercambiar datos útiles  
-  Gestión del perfil de usuario (modificación de datos y publicaciones activas)  
-  Visualización en **mapa interactivo** de las ubicaciones recientes  
-  Moderación por parte de administradores (comentarios y publicaciones)  
-  Acceso a normas comunitarias  
-  Información del sistema actualizada en tiempo real

---

##  Herramientas y tecnologías utilizadas

- **Lenguaje de programación:** Java
- **Framework backend:** Spring Boot
- **Gestor de dependencias:** Maven
- **Seguridad:** Spring Security
- **Base de datos:** MySQL
- **Frontend:** HTML, CSS, JavaScript
- **Diseño UI:** Bootstrap (previsto) | Futuro: Angular (evaluación)
- **API de mapas:** OpenStreetMap (vía Nominatim)
- **IDE:** IntelliJ IDEA
- **Control de versiones:** Git y GitHub
- **Pruebas de API:** Postman
- **Servidor local:** Spring Boot Embedded Tomcat

---

##  Instalación y ejecución del proyecto

### Requisitos previos

- Java 21 o superior
- MySQL corriendo localmente
- IntelliJ IDEA (u otro IDE compatible)
- Git

### Pasos para la configuración

1. **Clonar el repositorio:**

```bash
git clone https://github.com/SofiaCantalupi/petFinderMDP.git
cd petfinder-mdp
```

2. **Configurar la base de datos:**

Editar el archivo `application.properties` y colocar tu **usuario y contraseña de MySQL**:

```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

3. **Configurar JWT Secret Key:**

Crear una variable de entorno para definir una JWT Secret Key


4. **Ejecutar la aplicación:**


La aplicación estará disponible en `http://localhost:8080`.

---

##  Uso del sistema

### Registro de usuarios

Para registrarse, el usuario debe completar:

- Nombre
- Apellido
- Email
- Contraseña segura (mínimo 6 caracteres, con al menos una mayúscula, una minúscula, un número y un carácter especial)

### Inicio de sesión

Al iniciar sesión correctamente, el sistema devolverá:

- Un **token JWT** para autenticación
- Nombre del usuario
- Rol asignado en el sistema

Este token deberá ser incluido en Postman para acceder a las rutas protegidas:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI...
```

---

## Contacto

Proyecto desarrollado por:

- Benavides Leonel  
- Cantalupi Sofía  
- Cancino Franco  
- Herrera Daniel

---

## Licencia

Este proyecto está protegido bajo licencia académica. Para fines educativos y sin fines de lucro.
