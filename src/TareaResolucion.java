public class TareaResolucion {
    private final String id;
    private final MetodoResolucion metodo;
    private final Complejidad complejidad;
    private final long tiempoServicioEstimadoMs;

    private long tiempoLlegada;
    private long tiempoInicio;
    private long tiempoFin;
    private long tiempoEspera;

    public TareaResolucion(String id,
                           MetodoResolucion metodo,
                           Complejidad complejidad,
                           long tiempoServicioEstimadoMs,
                           long tiempoLlegada) {
        this.id = id;
        this.metodo = metodo;
        this.complejidad = complejidad;
        this.tiempoServicioEstimadoMs = tiempoServicioEstimadoMs;
        this.tiempoLlegada = tiempoLlegada;
    }

    public void ejecutarTrabajoRealCompleto() {
        try {
            Thread.sleep(tiempoServicioEstimadoMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getId() { return id; }
    public MetodoResolucion getMetodo() { return metodo; }
    public Complejidad getComplejidad() { return complejidad; }
    public long getTiempoServicioEstimadoMs() { return tiempoServicioEstimadoMs; }
    public long getTiempoLlegada() { return tiempoLlegada; }
    public void setTiempoLlegada(long v) { tiempoLlegada = v; }
    public long getTiempoInicio() { return tiempoInicio; }
    public void setTiempoInicio(long v) { tiempoInicio = v; }
    public long getTiempoFin() { return tiempoFin; }
    public void setTiempoFin(long v) { tiempoFin = v; }
    public long getTiempoEspera() { return tiempoEspera; }
    public void setTiempoEspera(long v) { tiempoEspera = v; }

    public long getTurnaround() {
        if (tiempoFin == 0 || tiempoLlegada == 0) return 0;
        return tiempoFin - tiempoLlegada;
    }
}
