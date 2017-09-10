
public class Automato {
	
	public enum Estado { ANDANDO, LIMPANDO, CARREGANDO, JOGANDO_LIXO_NA_LIXEIRA };
	
	private Estado estadoAtual;
	
	//assumindo que as quantidades de lixo sao sempre iguais e unitarias,
	//a capacidade de lixo do robo sera um multiplo inteiro de 1
	private int capacidadeMaximaDeLixo;
	private int quantidadeLixoAtual = 0;
	
	//pode ser uma lixeira, um ponto de regarda, ou a ultima celula visitada
	private Ponto posicaoObjetivo;
	private Ponto ultimaPosicaoVisitada;
	private Ponto posicaoAtual;
	private AEstrela aEstrela;
	
	//para simplificar, assumimos que a bateria nao cai abaixo da capacidade minima
	private int capacidadeMinimaDeBateria = 0;
	private int capacidadeMaximaDeBateria;
	private int quantidadeBateriaAtual;
	
	public Automato(int t, int c) throws Exception {
		estadoAtual = Estado.ANDANDO;
		posicaoAtual = new Ponto(0, 0);
		
		if (t <= 0)
			throw new Exception("Reservatorio de lixo (t) precisa ser maior que 0.");
		if (c < capacidadeMinimaDeBateria)
			throw new Exception("Carga maxima (c) precisa ser maior que a carga minima.");
		capacidadeMaximaDeLixo = t;
		capacidadeMaximaDeBateria = c;
		quantidadeBateriaAtual = c;
		
		aEstrela = new AEstrela();
	}
	
	public Ponto getPosicaoAtual() {
		return posicaoAtual;
	}
	
	//logica ainda nao testada. Acredito que ajudaria MUITO fazer uma nova maquina de estados
	public void proximaAcao(Sala sala) {
		System.out.println("Bateria: " + quantidadeBateriaAtual);
		System.out.println("Lixo carregado: " + quantidadeLixoAtual);
		System.out.println("Posicao: x=" + getPosicaoAtual().getY() + " y=" + getPosicaoAtual().getX());
		if (estadoAtual == Estado.CARREGANDO) {
			quantidadeBateriaAtual++;
			if (quantidadeBateriaAtual == capacidadeMaximaDeBateria) {
				estadoAtual = Estado.ANDANDO;
				posicaoObjetivo = new Ponto(ultimaPosicaoVisitada.getX(), ultimaPosicaoVisitada.getY());
			}
			System.out.println("Estado: carregando");
		}
		else if (estadoAtual == Estado.JOGANDO_LIXO_NA_LIXEIRA) {
			quantidadeLixoAtual--;
			quantidadeBateriaAtual--;
			if (quantidadeLixoAtual == 0 || quantidadeBateriaAtual <= capacidadeMinimaDeBateria) {
				estadoAtual = Estado.ANDANDO;
				posicaoObjetivo = new Ponto(ultimaPosicaoVisitada.getX(), ultimaPosicaoVisitada.getY());
			}
			System.out.println("Estado: jogando lixo na lixeira");
		}
		else if (estadoAtual == Estado.ANDANDO) {
			if (quantidadeBateriaAtual <= capacidadeMinimaDeBateria) {
				ultimaPosicaoVisitada = new Ponto(posicaoAtual.getX(), posicaoAtual.getY());
				posicaoObjetivo = sala.buscaRecargaMaisProxima(posicaoAtual.getX(), posicaoAtual.getY());
			}
			else if (quantidadeLixoAtual == capacidadeMaximaDeLixo) {
				ultimaPosicaoVisitada = new Ponto(posicaoAtual.getX(), posicaoAtual.getY());
				posicaoObjetivo = sala.buscaLixeiraMaisProxima(posicaoAtual.getX(), posicaoAtual.getY());
			}
			
			//realiza o movimento efetivo com A*
			if (posicaoObjetivo == null)
				posicaoObjetivo = sala.proximaPosicaoParaLimpeza(posicaoAtual);
			
			aEstrela.setOrigem(posicaoAtual);
			aEstrela.setObjetivo(posicaoObjetivo);
			posicaoAtual = aEstrela.f(sala);
			if (posicaoAtual.equals(posicaoObjetivo)) {
				sala.limpaPosicaoAtual(posicaoAtual.getX(), posicaoAtual.getY());
				aEstrela.limpaCaminhoPercorrido();
				posicaoObjetivo = null;
			}
			
			quantidadeBateriaAtual--;
			System.out.println("Estado: andando");
		}
		else if (estadoAtual == Estado.LIMPANDO) {
			quantidadeLixoAtual++;
			sala.limpaPosicaoAtual(posicaoAtual.getX(), posicaoAtual.getY());
			if (quantidadeLixoAtual == capacidadeMaximaDeLixo) {
				ultimaPosicaoVisitada = new Ponto(posicaoAtual.getX(), posicaoAtual.getY());
				posicaoObjetivo = sala.buscaLixeiraMaisProxima(posicaoAtual.getX(), posicaoAtual.getY());
			}
			estadoAtual = Estado.ANDANDO;
			quantidadeBateriaAtual--;
			System.out.println("Estado: limpando");
		}
		System.out.println();
	}
}