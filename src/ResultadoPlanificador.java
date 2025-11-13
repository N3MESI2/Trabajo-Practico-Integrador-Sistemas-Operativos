import java.util.List;

public class ResultadoPlanificador {
    private final String nombrePlanificador;
    private final List<TareaResolucion> tareas;
    private final long tiempoTotalMs;
    private final double esperaPromedio;
    private final double turnaroundPromedio;
    private final double throughput;

    public ResultadoPlanificador(String nombrePlanificador,
                                 List<TareaResolucion> tareas,
                                 long tiempoTotalMs,
                                 double esperaPromedio,
                                 double turnaroundPromedio,
                                 double throughput) {
        this.nombrePlanificador = nombrePlanificador;
        this.tareas = tareas;
        this.tiempoTotalMs = tiempoTotalMs;
        this.esperaPromedio = esperaPromedio;
        this.turnaroundPromedio = turnaroundPromedio;
        this.throughput = throughput;
    }

    public String getNombrePlanificador() { return nombrePlanificador; }
    public List<TareaResolucion> getTareas() { return tareas; }
    public long getTiempoTotalMs() { return tiempoTotalMs; }
    public double getEsperaPromedio() { return esperaPromedio; }
    public double getTurnaroundPromedio() { return turnaroundPromedio; }
    public double getThroughput() { return throughput; }
}
