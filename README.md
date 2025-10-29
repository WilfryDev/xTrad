#  Autocorrector v1.0

Una simple utilidad de escritorio en Java para corregir automáticamente texto en español, creada por **xPlugins**.

![Imagen de la GUI de Autocorrector](https://i.postimg.cc/k51g2SLH/image.png) 
## 📝 Acerca del Proyecto

**Autocorrector** es una aplicación ligera desarrollada en **Java Swing** diseñada para solucionar un problema común: olvidar tildes o mayúsculas en frases cotidianas en español.

La aplicación se ejecuta en la **bandeja del sistema (system tray)**, permitiéndote escribir en su ventana y ver cómo tu texto se corrige al instante. Es perfecta para tomar notas rápidas o escribir un mensaje y asegurarte de que tenga la ortografía correcta antes de copiarlo.

## ✨ Características Principales

* **Corrección en Tiempo Real:** Detecta y corrige frases mientras escribes (p. ej., `hola como estas` se convierte en `Hola cómo estás`).
* **Integración con Bandeja de Sistema:** Se minimiza a la bandeja del sistema para un acceso rápido y para mantenerse ejecutándose en segundo plano.
* **Interfaz Moderna:** Utiliza la librería [FlatLaf Darcula](https://www.formdev.com/flatlaf/) para una apariencia oscura, limpia y moderna.
* **Controles Sencillos:**
    * **Activar/Desactivar:** Un *checkbox* para habilitar o deshabilitar la corrección fácilmente.
    * **Botón de Copiar:** Copia todo el texto corregido al portapapeles.
    * **Botón de Limpiar:** Borra todo el texto del área.

## ⚙️ ¿Cómo Funciona?

La lógica es simple pero efectiva. La aplicación utiliza un `HashMap` estático que almacena las reglas de corrección:

```java
static {
    REGLAS_CORRECCION.put("hola como estas", "Hola cómo estás");
    REGLAS_CORRECCION.put("que haces", "qué haces");
    REGLAS_CORRECCION.put("por que", "por qué");
    // ... y más reglas (Puedes añadirle mas)
}
```

Un DocumentListener monitorea el JTextArea. Cuando escribes, compara el texto (en minúsculas) con las claves del mapa. Si encuentra una coincidencia, reemplaza la frase incorrecta por la versión corregida.

🚀 Cómo Usar
Ejecuta el archivo .jar.

La ventana principal aparecerá. Empieza a escribir.

Si cierras la ventana con la 'X', la aplicación no se cerrará, sino que se minimizará a la bandeja del sistema.

Haz clic derecho en el ícono de la bandeja para Restaurar la ventana o Salir de la aplicación.

📄 Licencia
Este proyecto está distribuido bajo la Licencia MIT.

Copyright (c) 2025 xGuard / [https://xguard.es/terms](https://xguard.es/terms)