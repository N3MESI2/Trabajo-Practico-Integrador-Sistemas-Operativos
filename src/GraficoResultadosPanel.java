import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class GraficoResultadosPanel extends JPanel {

    private ResultadoPlanificador[] resultados;
    private String metrica;

    public GraficoResultadosPanel() {
        this.resultados = null;
        this.metrica = "Turnaround promedio";
        setBackground(new Color(20, 20, 20));
    }

    public void setResultados(ResultadoPlanificador[] resultados, String metrica) {
        this.resultados = resultados;
        this.metrica = metrica;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (resultados == null || resultados.length == 0) {
            g.setColor(Color.WHITE);
            g.drawString("Ejecute una simulacion para ver los resultados.", 20, 20);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int margin = 50;

        int n = resultados.length;
        double maxValor = 0;
        for (ResultadoPlanificador r : resultados) {
            double val = getValorMetrica(r);
            if (val > maxValor) {
                maxValor = val;
            }
        }
        if (maxValor == 0) maxValor = 1;

        int chartWidth = width - 2 * margin;
        int chartHeight = height - 2 * margin;
        int barWidth = chartWidth / (n * 2);

        g2.setColor(Color.WHITE);
        g2.drawString(getTituloMetrica(), margin, margin - 10);

        // Ejes
        g2.drawLine(margin, margin, margin, margin + chartHeight);
        g2.drawLine(margin, margin + chartHeight, margin + chartWidth, margin + chartHeight);

        for (int i = 0; i < n; i++) {
            ResultadoPlanificador r = resultados[i];
            int x = margin + (2 * i + 1) * barWidth;
            int barHeight = (int) ((getValorMetrica(r) / maxValor) * (chartHeight - 20));
            int y = margin + chartHeight - barHeight;

            // 🎨 Color distinto por algoritmo
            g2.setColor(getColorForPlanificador(r.getNombrePlanificador()));
            g2.fillRect(x, y, barWidth, barHeight);
            g2.setColor(Color.WHITE);
            g2.drawRect(x, y, barWidth, barHeight);

            String label = r.getNombrePlanificador();
            FontMetrics fm = g2.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2.drawString(label, x + (barWidth - labelWidth) / 2, margin + chartHeight + 15);

            String valStr;
            if ("Throughput".equals(metrica)) {
                valStr = String.format("%.2f", getValorMetrica(r));
            } else {
                valStr = String.format("%.1f", getValorMetrica(r));
            }
            int valWidth = fm.stringWidth(valStr);
            g2.drawString(valStr, x + (barWidth - valWidth) / 2, y - 5);
        }
    }

    private double getValorMetrica(ResultadoPlanificador r) {
        switch (metrica) {
            case "Tiempo total global":
                return r.getTiempoTotalMs();
            case "Throughput":
                return r.getThroughput();
            case "Turnaround promedio":
            default:
                return r.getTurnaroundPromedio();
        }
    }

    private String getTituloMetrica() {
        switch (metrica) {
            case "Tiempo total global":
                return "Tiempo total global por planificador (ms)";
            case "Throughput":
                return "Throughput por planificador (tareas/seg)";
            case "Turnaround promedio":
            default:
                return "Turnaround promedio por planificador (ms)";
        }
    }

    // 🎨 Color propio para cada planificador
    private Color getColorForPlanificador(String nombre) {
        if (nombre == null) return new Color(100, 149, 237);

        String n = nombre.toLowerCase();
        if (n.contains("fcfs")) {
            // Azul
            return new Color(52, 152, 219);
        } else if (n.contains("round")) {
            // Verde
            return new Color(46, 204, 113);
        } else if (n.contains("sjf")) {
            // Violeta
            return new Color(155, 89, 182);
        }
        // Default
        return new Color(100, 149, 237);
    }
}
