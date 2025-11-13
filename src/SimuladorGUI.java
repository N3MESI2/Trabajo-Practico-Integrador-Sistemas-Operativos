import com.sun.management.OperatingSystemMXBean;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class SimuladorGUI extends JFrame {

    private JComboBox<String> comboAlgoritmo;
    private JComboBox<String> comboMetrica;
    private JComboBox<String> comboModo;
    private JTextArea textAreaResultados;
    private GraficoResultadosPanel panelGrafico;

    private List<TareaResolucion> loteBase;
    private List<Planificador> planificadores;

    // Para generar el informe .txt a partir de la última simulación
    private List<ResultadoPlanificador> ultimosResultados = new ArrayList<>();

    public SimuladorGUI() {
        super("Simulacion de Planificacion - Sistemas Operativos");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        loteBase = SimuladorPlanificacion.generarLoteHeterogeneo();
        planificadores = SimuladorPlanificacion.crearPlanificadores();

        initComponents();
    }

    private void initComponents() {
        Color bg = new Color(30, 30, 30);
        Color fg = new Color(230, 230, 230);
        Color controlBg = new Color(45, 45, 45);

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(bg);

        JLabel lblAlg = new JLabel("Algoritmo:");
        lblAlg.setForeground(fg);
        panelSuperior.add(lblAlg);

        comboAlgoritmo = new JComboBox<>(new String[]{
                "FCFS",
                "Round Robin",
                "SJF",
                "Todos"
        });
        comboAlgoritmo.setBackground(controlBg);
        comboAlgoritmo.setForeground(fg);
        panelSuperior.add(comboAlgoritmo);

        JLabel lblMet = new JLabel("  Metrica grafica:");
        lblMet.setForeground(fg);
        panelSuperior.add(lblMet);

        comboMetrica = new JComboBox<>(new String[]{
                "Turnaround promedio",
                "Tiempo total global",
                "Throughput"
        });
        comboMetrica.setBackground(controlBg);
        comboMetrica.setForeground(fg);
        panelSuperior.add(comboMetrica);

        // 👇 Nuevo combo para elegir modo
        JLabel lblModo = new JLabel("  Modo:");
        lblModo.setForeground(fg);
        panelSuperior.add(lblModo);

        comboModo = new JComboBox<>(new String[]{
                "Resultado final",
                "Ejecucion en vivo"
        });
        comboModo.setBackground(controlBg);
        comboModo.setForeground(fg);
        panelSuperior.add(comboModo);

        JButton btnEjecutar = new JButton("Ejecutar simulacion");
        btnEjecutar.setBackground(new Color(70, 70, 70));
        btnEjecutar.setForeground(fg);
        btnEjecutar.addActionListener(this::ejecutarSimulacion);
        panelSuperior.add(btnEjecutar);

        // Boton para guardar informe .txt
        JButton btnGuardar = new JButton("Guardar informe metricas");
        btnGuardar.setBackground(new Color(70, 70, 70));
        btnGuardar.setForeground(fg);
        btnGuardar.addActionListener(e -> guardarInformeMetricas());
        panelSuperior.add(btnGuardar);

        getContentPane().setBackground(bg);
        getContentPane().add(panelSuperior, BorderLayout.NORTH);

        textAreaResultados = new JTextArea();
        textAreaResultados.setEditable(false);
        textAreaResultados.setBackground(new Color(20, 20, 20));
        textAreaResultados.setForeground(fg);
        textAreaResultados.setCaretColor(fg);
        // Fuente monoespaciada para que las columnas alineen bien
        textAreaResultados.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane scrollText = new JScrollPane(textAreaResultados);
        scrollText.getViewport().setBackground(new Color(20, 20, 20));

        panelGrafico = new GraficoResultadosPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollText, panelGrafico);
        splitPane.setDividerLocation(420);
        splitPane.setBackground(bg);

        getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    private void ejecutarSimulacion(ActionEvent e) {
        String seleccion = (String) comboAlgoritmo.getSelectedItem();
        String modo = (String) comboModo.getSelectedItem();

        textAreaResultados.setText("");

        List<ResultadoPlanificador> resultadosList = new ArrayList<>();

        if ("Todos".equals(seleccion)) {
            for (Planificador p : planificadores) {
                ResultadoPlanificador res = SimuladorPlanificacion.ejecutarPlanificador(p, loteBase);
                resultadosList.add(res);
            }
        } else {
            Planificador elegido = null;
            for (Planificador p : planificadores) {
                if (p.getNombre().startsWith(seleccion)) {
                    elegido = p;
                    break;
                }
            }
            if (elegido != null) {
                ResultadoPlanificador res = SimuladorPlanificacion.ejecutarPlanificador(elegido, loteBase);
                resultadosList.add(res);
            }
        }

        ultimosResultados = resultadosList;

        // Modo de salida: resultado final vs ejecucion en vivo
        if ("Ejecucion en vivo".equals(modo)) {
            mostrarResultadosEnVivo(resultadosList);
        } else {
            for (ResultadoPlanificador res : resultadosList) {
                imprimirResultadoEnTexto(res);
            }
        }

        // Métricas de sistema al final del texto
        textAreaResultados.append("══════════════════════════════════════════════════════\n");
        textAreaResultados.append(" METRICAS DE SISTEMA (APROXIMADAS)\n");
        textAreaResultados.append("══════════════════════════════════════════════════════\n");
        textAreaResultados.append(capturarMetricasSistema());
        textAreaResultados.append("\n\n");

        String metrica = (String) comboMetrica.getSelectedItem();
        panelGrafico.setResultados(resultadosList.toArray(new ResultadoPlanificador[0]), metrica);
    }

    // Simula ejecucion en vivo escribiendo linea por linea en un hilo aparte
    private void mostrarResultadosEnVivo(List<ResultadoPlanificador> resultadosList) {
        new Thread(() -> {
            for (ResultadoPlanificador res : resultadosList) {
                // Cabecera
                appendTexto("\n══════════════════════════════════════════════════════\n");
                appendTexto(" ALGORITMO (EN VIVO): " + res.getNombrePlanificador() + "\n");
                appendTexto("══════════════════════════════════════════════════════\n\n");

                appendTexto("» DETALLE DE TAREAS (simulado en vivo)\n");
                appendTexto(String.format(
                        "  %-4s %-15s %-12s %-10s %-12s%n",
                        "ID", "Metodo", "Complejidad", "Espera", "Turnaround"
                ));
                appendTexto("  -------------------------------------------------------------\n");

                for (TareaResolucion t : res.getTareas()) {
                    long turnaround = t.getTiempoFin() - t.getTiempoLlegada();
                    String linea = String.format(
                            "  %-4s %-15s %-12s %-10d %-12d%n",
                            t.getId(),
                            t.getMetodo(),
                            t.getComplejidad(),
                            t.getTiempoEspera(),
                            turnaround
                    );
                    appendTexto(linea);
                    try {
                        Thread.sleep(250); // pequeño delay para efecto "en vivo"
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

                appendTexto("\n");
                appendTexto("» RESUMEN GLOBAL\n");
                appendTexto(String.format("  • Tiempo total global : %d ms%n", res.getTiempoTotalMs()));
                appendTexto(String.format("  • Espera promedio     : %.2f ms%n", res.getEsperaPromedio()));
                appendTexto(String.format("  • Turnaround promedio : %.2f ms%n", res.getTurnaroundPromedio()));
                appendTexto(String.format("  • Throughput          : %.2f tareas/seg%n\n", res.getThroughput()));
            }
        }).start();
    }

    // Helper para escribir en el JTextArea desde otro hilo
    private void appendTexto(String texto) {
        javax.swing.SwingUtilities.invokeLater(() -> textAreaResultados.append(texto));
    }

    private void imprimirResultadoEnTexto(ResultadoPlanificador res) {
        textAreaResultados.append("══════════════════════════════════════════════════════\n");
        textAreaResultados.append(" ALGORITMO: " + res.getNombrePlanificador() + "\n");
        textAreaResultados.append("══════════════════════════════════════════════════════\n\n");

        textAreaResultados.append("» RESUMEN GLOBAL\n");
        textAreaResultados.append(String.format("  • Tiempo total global : %d ms\n", res.getTiempoTotalMs()));
        textAreaResultados.append(String.format("  • Espera promedio     : %.2f ms\n", res.getEsperaPromedio()));
        textAreaResultados.append(String.format("  • Turnaround promedio : %.2f ms\n", res.getTurnaroundPromedio()));
        textAreaResultados.append(String.format("  • Throughput          : %.2f tareas/seg\n\n", res.getThroughput()));

        textAreaResultados.append("» DETALLE DE TAREAS\n");
        textAreaResultados.append(String.format(
                "  %-4s %-15s %-12s %-10s %-12s%n",
                "ID", "Metodo", "Complejidad", "Espera", "Turnaround"
        ));
        textAreaResultados.append("  -------------------------------------------------------------\n");

        for (TareaResolucion t : res.getTareas()) {
            long turnaround = t.getTiempoFin() - t.getTiempoLlegada();
            textAreaResultados.append(String.format(
                    "  %-4s %-15s %-12s %-10d %-12d%n",
                    t.getId(),
                    t.getMetodo(),
                    t.getComplejidad(),
                    t.getTiempoEspera(),
                    turnaround
            ));
        }
        textAreaResultados.append("\n");

        textAreaResultados.append("» TABLA COMPARATIVA POR METODO\n");
        textAreaResultados.append(String.format(
                "  %-18s %-8s %-15s %-15s%n",
                "Metodo", "Count", "Espera prom.", "Turnaround prom."
        ));
        textAreaResultados.append("  -------------------------------------------------------------\n");

        Map<MetodoResolucion, long[]> stats = new EnumMap<>(MetodoResolucion.class);
        for (TareaResolucion t : res.getTareas()) {
            long turnaround = t.getTiempoFin() - t.getTiempoLlegada();
            MetodoResolucion m = t.getMetodo();
            long[] arr = stats.get(m);
            if (arr == null) {
                arr = new long[3]; // sumaEspera, sumaTurnaround, count
                stats.put(m, arr);
            }
            arr[0] += t.getTiempoEspera();
            arr[1] += turnaround;
            arr[2] += 1;
        }

        for (Map.Entry<MetodoResolucion, long[]> entry : stats.entrySet()) {
            MetodoResolucion m = entry.getKey();
            long[] arr = entry.getValue();
            double espProm = (double) arr[0] / arr[2];
            double turnProm = (double) arr[1] / arr[2];
            textAreaResultados.append(String.format(
                    "  %-18s %-8d %-15.1f %-15.1f%n",
                    m, arr[2], espProm, turnProm
            ));
        }

        textAreaResultados.append("\n\n");
    }

    // =========================
    //  GUARDAR INFORME EN .TXT
    // =========================

    private void guardarInformeMetricas() {
        if (ultimosResultados == null || ultimosResultados.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Primero ejecute una simulacion para generar metricas.",
                    "Sin datos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INFORME DE METRICAS - PLANIFICACION DE PROCESOS\n");
        sb.append("Fecha y hora: ").append(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        ).append("\n\n");

        for (ResultadoPlanificador res : ultimosResultados) {
            sb.append("============================================================\n");
            sb.append("ALGORITMO: ").append(res.getNombrePlanificador()).append("\n");
            sb.append("============================================================\n");
            sb.append(String.format("Tiempo total global     : %d ms%n", res.getTiempoTotalMs()));
            sb.append(String.format("Turnaround promedio     : %.2f ms%n", res.getTurnaroundPromedio()));
            sb.append(String.format("Espera promedio         : %.2f ms%n", res.getEsperaPromedio()));
            sb.append(String.format("Throughput              : %.2f tareas/seg (%.2f circuitos/minuto)%n",
                    res.getThroughput(), res.getThroughput() * 60.0));
            sb.append("\n");

            sb.append("Metricas de rendimiento por metodo (Cramer vs Gauss-Jordan vs Libreria numerica)\n");
            Map<MetodoResolucion, long[]> statsMetodo = new EnumMap<>(MetodoResolucion.class);
            // [0] sumaTiempoEjecucion, [1] sumaEspera, [2] count
            for (TareaResolucion t : res.getTareas()) {
                long tiempoEjecucion = t.getTiempoFin() - t.getTiempoInicio();
                MetodoResolucion m = t.getMetodo();
                long[] arr = statsMetodo.get(m);
                if (arr == null) {
                    arr = new long[3];
                    statsMetodo.put(m, arr);
                }
                arr[0] += tiempoEjecucion;
                arr[1] += t.getTiempoEspera();
                arr[2] += 1;
            }
            sb.append(String.format("%-18s %-10s %-18s %-18s%n",
                    "Metodo", "Count", "Ejecucion prom.", "Espera prom."));
            for (Map.Entry<MetodoResolucion, long[]> entry : statsMetodo.entrySet()) {
                MetodoResolucion m = entry.getKey();
                long[] arr = entry.getValue();
                double ejecProm = (double) arr[0] / arr[2];
                double espProm = (double) arr[1] / arr[2];
                sb.append(String.format("%-18s %-10d %-18.1f %-18.1f%n",
                        m, arr[2], ejecProm, espProm));
            }
            sb.append("\n");

            sb.append("Tiempo total por tipo de circuito (simple/medio/complejo)\n");
            Map<Complejidad, long[]> statsComp = new EnumMap<>(Complejidad.class);
            // [0] sumaTurnaround, [1] count
            for (TareaResolucion t : res.getTareas()) {
                long turnaround = t.getTiempoFin() - t.getTiempoLlegada();
                Complejidad c = t.getComplejidad();
                long[] arr = statsComp.get(c);
                if (arr == null) {
                    arr = new long[2];
                    statsComp.put(c, arr);
                }
                arr[0] += turnaround;
                arr[1] += 1;
            }
            sb.append(String.format("%-12s %-15s %-15s%n",
                    "Complejidad", "Turnaround total", "Turnaround prom."));
            for (Map.Entry<Complejidad, long[]> entry : statsComp.entrySet()) {
                Complejidad c = entry.getKey();
                long[] arr = entry.getValue();
                double turnProm = (double) arr[0] / arr[1];
                sb.append(String.format("%-12s %-15d %-15.1f%n",
                        c, arr[0], turnProm));
            }

            sb.append("\nTiempos de espera en cola de planificacion (detalle por tarea):\n");
            for (TareaResolucion t : res.getTareas()) {
                sb.append("  ").append(t.getId())
                        .append(" [").append(t.getMetodo())
                        .append(", ").append(t.getComplejidad())
                        .append("] Espera=").append(t.getTiempoEspera()).append(" ms\n");
            }

            sb.append("\n------------------------------------------------------------\n\n");
        }

        sb.append("METRICAS DE SISTEMA (aproximadas)\n");
        sb.append(capturarMetricasSistema()).append("\n");

        sb.append("METRICAS DE EFICIENCIA\n");
        sb.append(generarAnalisisEficiencia(ultimosResultados)).append("\n");

        File archivo = new File("metricas_planificacion.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            bw.write(sb.toString());
            JOptionPane.showMessageDialog(
                    this,
                    "Informe guardado en:\n" + archivo.getAbsolutePath(),
                    "Informe guardado",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar el informe: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String capturarMetricasSistema() {
        StringBuilder sb = new StringBuilder();
        try {
            OperatingSystemMXBean osBean =
                    (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            double cpuLoad = osBean.getSystemCpuLoad(); // 0..1
            long totalMem = osBean.getTotalPhysicalMemorySize();
            long freeMem = osBean.getFreePhysicalMemorySize();
            long usedMem = totalMem - freeMem;

            sb.append(String.format("Uso aproximado de CPU del sistema: %.2f %%\n", cpuLoad * 100.0));
            sb.append(String.format("Memoria fisica usada por el proceso Java: %.2f MB de %.2f MB\n",
                    usedMem / (1024.0 * 1024.0),
                    totalMem / (1024.0 * 1024.0)));
            sb.append("I/O de disco durante los calculos intensivos: en esta simulacion la carga se\n");
            sb.append("realiza principalmente en memoria y CPU, por lo que el acceso a disco es bajo\n");
            sb.append("y no se mide de forma directa. En un entorno real podria medirse con herramientas\n");
            sb.append("del sistema operativo (iostat, sar, perf, etc.).\n");
        } catch (Exception ex) {
            sb.append("No se pudieron obtener metricas de sistema desde la JVM.\n");
        }
        return sb.toString();
    }

    private String generarAnalisisEficiencia(List<ResultadoPlanificador> resultados) {
        StringBuilder sb = new StringBuilder();

        if (resultados.isEmpty()) return "";

        double minTotal = Double.MAX_VALUE;
        double maxTotal = 0;
        String mejor = "";
        String peor = "";

        for (ResultadoPlanificador r : resultados) {
            double total = r.getTiempoTotalMs();
            if (total < minTotal) {
                minTotal = total;
                mejor = r.getNombrePlanificador();
            }
            if (total > maxTotal) {
                maxTotal = total;
                peor = r.getNombrePlanificador();
            }
        }

        if (minTotal > 0) {
            double speedup = maxTotal / minTotal;
            sb.append(String.format("Speed-up entre el peor algoritmo (%s) y el mejor (%s): %.2f%n",
                    peor, mejor, speedup));
        }

        sb.append("Eficiencia de CPU: se infiere a partir del throughput y de los tiempos de turnaround.\n");
        sb.append("Algoritmos con menor turnaround promedio y mayor throughput aprovechan mejor el procesador.\n");
        sb.append("En esta simulacion, SJF suele mostrar mayor eficiencia que FCFS y Round Robin.\n\n");

        sb.append("Overhead de comunicacion entre hilos: la simulacion se implementa en un unico hilo,\n");
        sb.append("por lo que el overhead de sincronizacion entre hilos es despreciable. En un sistema real\n");
        sb.append("con hilos concurrentes, este overhead seria relevante.\n\n");

        sb.append("Escalabilidad con circuitos mas complejos: al incrementar la complejidad (circuitos complejos),\n");
        sb.append("aumentan los tiempos de servicio y, con ello, los tiempos de espera y turnaround. Los algoritmos\n");
        sb.append("que mejor gestionan este crecimiento son los que priorizan trabajos cortos (SJF) o reparten la CPU\n");
        sb.append("en forma equitativa (Round Robin).\n");

        return sb.toString();
    }
}
