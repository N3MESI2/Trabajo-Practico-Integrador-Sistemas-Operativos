[README.txt](https://github.com/user-attachments/files/23536400/README.txt)

Proyecto: Simulación de Algoritmos de Planificación de Procesos
Este proyecto implementa tres algoritmos de planificación de procesos (FCFS, Round Robin y SJF) aplicados a la resolución simulada de circuitos eléctricos mediante distintos métodos (Cramer, Gauss-Jordan y Librería Numérica).
Incluye una interfaz gráfica en Java (modo oscuro), gráficos comparativos, métricas detalladas y scripts Bash para compilación, ejecución y monitoreo del sistema.

1. Contenido del proyecto

Estructura recomendada del directorio:

TP_INTEGRADOR/
 ├─ src/                     Código fuente Java
 ├─ bin/                     Archivos .class compilados
 ├─ compilar.sh              Script Bash de compilación
 ├─ ejecutar.sh              Script Bash de ejecución
 ├─ monitorear.sh            Script Bash de monitoreo del sistema
 ├─ metricas_planificacion.txt (generado por la app)
 ├─ logs/                    Archivos generados por monitoreo
 └─ README.txt


2. Requerimientos

1. WSL (Windows Subsystem for Linux) con Ubuntu.
2. Java JDK 8 o superior instalado dentro de Ubuntu.
3. Permisos de ejecución para los scripts Bash.
4. Visual Studio Code opcional, pero recomendado.


3. Compilación del proyecto (Linux / WSL)

1. Abrir Ubuntu o terminal WSL dentro de Visual Studio Code.
2. Posicionarse en la carpeta del proyecto:

   cd ruta/al/TP_INTEGRADOR

3. Dar permisos de ejecución (solo la primera vez):

   chmod +x compilar.sh ejecutar.sh monitorear.sh

4. Ejecutar la compilación:

   ./compilar.sh

Esto compila todos los archivos .java dentro de src/ y genera los .class dentro de bin/.


4. Ejecución del programa (GUI)

Para iniciar la interfaz gráfica:

   ./ejecutar.sh

Aparecerá la ventana del simulador en modo oscuro, con:

- Selección de algoritmo
- Selección de métrica para gráfico
- Modo de ejecución (resultado final o ejecución en vivo)
- Botón para generar archivo de métricas
- Visualización del gráfico comparativo por algoritmo


5. Monitoreo del sistema operativo

El siguiente script inicia el programa y captura métricas del sistema en archivos .txt:

   ./monitorear.sh

Este script genera la carpeta:

logs/
 ├── cpu_usage.txt
 ├── memoria_inicial.txt
 ├── procesos_cpu.txt
 └── disk_io.txt

Descripción de cada archivo:

- cpu_usage.txt: Registro periódico de uso del CPU.
- memoria_inicial.txt: Estado de RAM total, usada y libre.
- procesos_cpu.txt: Procesos ordenados por uso de CPU.
- disk_io.txt: Actividad de disco durante la ejecución.


6. Generación del archivo de métricas de planificación

Dentro de la aplicación Java, el botón "Guardar informe métricas" produce un archivo:

metricas_planificacion.txt

El archivo incluye:

- Métricas de rendimiento por algoritmo
- Métricas por método (Cramer, Gauss-Jordan, Librería numérica)
- Tiempos por complejidad (simple, medio, complejo)
- Tiempos de espera y turnaround
- Throughput
- Métricas aproximadas del sistema (CPU y RAM)
- Análisis de eficiencia, speed-up, overhead y escalabilidad

Este archivo es el entregable principal del análisis comparativo solicitado.


7. Nota sobre I/O de disco y context switches

La simulación se ejecuta dentro de un único proceso Java; por lo tanto:

- La actividad de disco es simulada y medida mediante iostat.
- No se accede directamente al contador de cambios de contexto.
- Se incluyen explicaciones dentro del informe.


8. Ejecución completa recomendada

./compilar.sh
./monitorear.sh   (opcional)
./ejecutar.sh


9. Soporte

Si se abre en Windows y no reconoce los scripts Bash, usar WSL (Ubuntu).
