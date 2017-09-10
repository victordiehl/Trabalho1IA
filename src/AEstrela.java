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
	
	public Ponto f(Sala sala) {
		solucoesPossiveis = sala.getVizinhos(origem);
		
		quantidadeSolucoes = 0;
		for (int i = 0; i < solucoesPossiveis.length; i++) {
			Ponto aux = solucoesPossiveis[i];
			if (sala.isPontoValido(aux) && !caminhoPercorrido.contains(aux)) {
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
	
	public void limpaCaminhoPercorrido() {
		caminhoPercorrido.clear();
	}
	
	//custo fixo para ir para qualquer celula vizinha
	private double g() {
		return 1.0;
	}
	
	private int h(Ponto posicaoN) {
		return Math.abs(posicaoN.getX() - objetivo.getX()) + Math.abs(posicaoN.getY() - objetivo.getY());
	}
}
