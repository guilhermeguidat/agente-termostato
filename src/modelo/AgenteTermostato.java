package modelo;

import bd.HistoricoDAO;

public class AgenteTermostato {
    private final HistoricoDAO dao;

    public AgenteTermostato() {
        this.dao = new HistoricoDAO();
    }

    public void atuar(double temperaturaAtual) {
        System.out.println("\n[Percepção] Temperatura atual: " + temperaturaAtual + "°C");

        String ultimaAcao = dao.buscarUltimaAcao();
        System.out.println("[Histórico] Última ação realizada: " + ultimaAcao);

        String novaAcao = getString(temperaturaAtual, ultimaAcao);

        System.out.println("[Ação] Decisão tomada: " + novaAcao);

        dao.registrarAcao(temperaturaAtual, novaAcao);
    }

    private static String getString(double temperaturaAtual, String ultimaAcao) {
        String novaAcao = "Nenhuma Acao";

        if (temperaturaAtual > 24.0) {
            if (ultimaAcao.equals("Ligar Ar") || ultimaAcao.equals("Aumentar Potencia")) {
                novaAcao = "Aumentar Potencia";
            } else {
                novaAcao = "Ligar Ar";
            }
        } else if (temperaturaAtual < 20.0) {
            if (!ultimaAcao.equals("Desligar Ar")) {
                novaAcao = "Desligar Ar";
            }
        } else {
            novaAcao = "Nenhuma Acao";
        }
        return novaAcao;
    }
}