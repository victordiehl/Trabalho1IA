import java.util.ArrayDeque;
import java.util.Deque;

public class AEstrela {

	Ponto origem, objetivo;
	Ponto[] solucoesPossiveis = new Ponto[8];
	Ponto[] solucoesValidas = new Ponto[8];
	double[] solucoesCusto = new double[8];
	int quantidadeSolucoes = 0;
	Deque<Ponto> caminhoPercorrido = new ArrayDeque<Ponto>();
	
	public void setOrigem(Ponto origem) {
		this.origem = origem;
	}
	
	public void setObjetivo(Ponto objetivo) {
		this.objetivo = objetivo;
	}
	
	private void geraSolucoesPossiveis() {
		solucoesPossiveis[0] = origem.superiorEsquerdo();
		solucoesPossiveis[1] = origem.superior();
		solucoesPossiveis[2] = origem.superiorDireito();
		solucoesPossiveis[3] = origem.esquerdo();
		solucoesPossiveis[4] = origem.direito();
		solucoesPossiveis[5] = origem.inferiorEsquerdo();
		solucoesPossiveis[6] = origem.inferior();
		solucoesPossiveis[7] = origem.inferiorDireito();
	}
	
	public Ponto f(Sala sala) {
		geraSolucoesPossiveis();
		
		quantidadeSolucoes = 0;
		for (int i = 0; i < solucoesPossiveis.length; i++) {
			Ponto aux = solucoesPossiveis[i];
			if (sala.isValido(aux) && !caminhoPercorrido.contains(aux)) {
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
		caminhoPercorrido.push(solucoesValidas[solucaoAtual]);
		return solucoesValidas[solucaoAtual];
	}
	
	//custo fixo para ir para qualquer celula vizinha
	private double g() {
		return 1.0;
	}
	
	private int h(Ponto posicaoN) {
		return Math.abs(posicaoN.getX() - objetivo.getX()) + Math.abs(posicaoN.getY() - objetivo.getY());
	}
}
