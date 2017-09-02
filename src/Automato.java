
public class Automato {
	
	public enum Estado { ANDANDO, LIMPANDO, CARREGANDO, JOGANDO_LIXO_NA_LIXEIRA };
	
	private Estado estadoAtual;
	
	//assumindo que as quantidades de lixo sao sempre iguais e unitarias,
	//a capacidade de lixo do robo sera um multiplo inteiro de 1
	private int capacidadeMaximaDeLixo;
	private int quantidadeLixoAtual;
	
	private Ponto ultimaPosicaoVisitada;
	
	//pode ser uma lixeira, um ponto de regarda, ou a ultima celula visitada
	private Ponto posicaoObjetivo;
	
	//para simplificar, assumimos que a bateria nao cai abaixo da capacidade minima
	private int capacidadeMinimaDeBateria = 0;
	private int capacidadeMaximaDeBateria;
	private int quantidadeBateriaAtual;
	
	public Automato(int t, int c) throws Exception {
		this.estadoAtual = Estado.ANDANDO;
		if (t <= 0)
			throw new Exception("Reservatorio de lixo (t) precisa ser maior que 0.");
		if (c <= 0)
			throw new Exception("Carga maxima (c) precisa ser maior que 0.");
		capacidadeMaximaDeLixo = t;
		capacidadeMaximaDeBateria = c;
	}
}
