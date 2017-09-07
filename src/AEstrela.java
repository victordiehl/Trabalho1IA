
public class AEstrela {

	Ponto origem, objetivo;
	Sala sala;
	Ponto[] solucoesPossiveis = new Ponto[8];
	Ponto[] solucoesValidas = new Ponto[8];
	double[] solucoesCusto = new double[8];
	int quantidadeSolucoes = 0;
	
	public AEstrela(Ponto origem, Ponto objetivo, Sala sala) {
		this.origem = origem;
		this.objetivo = objetivo;
		this.sala = sala;
		
		solucoesPossiveis[0] = origem.superiorEsquerdo();
		solucoesPossiveis[1] = origem.superior();
		solucoesPossiveis[2] = origem.superiorDireito();
		solucoesPossiveis[3] = origem.esquerdo();
		solucoesPossiveis[4] = origem.direito();
		solucoesPossiveis[5] = origem.inferiorEsquerdo();
		solucoesPossiveis[6] = origem.inferior();
		solucoesPossiveis[7] = origem.inferiorDireito();
	}
	
	public Ponto f() {
		for (int i = 0; i < solucoesPossiveis.length; i++) {
			Ponto aux = solucoesPossiveis[i];
			if (sala.isValido(aux) && !sala.isParede(aux) && !sala.isLixeira(aux) && !sala.isRecarga(aux)) {
				solucoesValidas[quantidadeSolucoes] = solucoesPossiveis[i];
				solucoesCusto[quantidadeSolucoes] = g() + h(aux);
				quantidadeSolucoes++;
			}
		}
		
		//Todas solucoes validas foram calculadas. Escolheremos aquela que tem menor custo
		double custoAtual = Integer.MAX_VALUE;
		int solucaoAtual = -1;
		for (int i = 0; i < quantidadeSolucoes; i++) {
			if (solucoesCusto[i] < custoAtual) {
				custoAtual = solucoesCusto[i];
				solucaoAtual = i;
			}
		}
		return solucoesValidas[solucaoAtual];
	}
	
	private double g() {
		return 1.0;
	}
	
	private int h(Ponto posicaoN) {
		return Math.abs(posicaoN.getX() - objetivo.getX()) + Math.abs(posicaoN.getX() - objetivo.getX());
	}
}
