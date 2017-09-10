
public class App {
	
	private static final int TAMANHO_SALA = 12;
	private static final int QUANTIDADE_LIXEIRAS = 1;
	private static final int QUANTIDADE_PONTOS_RECARGA = 5;
	
	private static final int CAPACIDADE_MAXIMA_LIXO = 5;
	private static final int CARGA_MAXIMA_BATERIA = Integer.MAX_VALUE;
	
	public static void main(String[] args) throws Exception {
		Sala sala = new Sala(TAMANHO_SALA, QUANTIDADE_LIXEIRAS, QUANTIDADE_PONTOS_RECARGA);
		Automato robo = new Automato(CAPACIDADE_MAXIMA_LIXO, CARGA_MAXIMA_BATERIA);
		
		while(true) {
			sala.printSala(robo);
			robo.proximaAcao(sala);
			Thread.sleep(500);
		}
	}
}
