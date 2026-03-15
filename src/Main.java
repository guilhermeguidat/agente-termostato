import modelo.AgenteTermostato;

void main() {
    AgenteTermostato agente = new AgenteTermostato();

    double[] temperaturasSimuladas = {25.5, 26.0, 22.0, 19.0, 18.5};

    IO.println("--- INICIANDO SIMULAÇÃO DO AGENTE ---");
    for (int i = 0; i < temperaturasSimuladas.length; i++) {
        IO.println("\n--- Ciclo " + (i + 1) + " ---");
        agente.atuar(temperaturasSimuladas[i]);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException _) {
        }
    }
}