import java.util.HashMap;
import java.util.Random;

public class Sala {
    
    public enum Tile { LIMPO, LIXEIRA, LIXO, PAREDE, RECARGA, LIMPO_E_VISITADO };
    HashMap<Tile, String> tabelaParaImpressao = new HashMap<>();
    
    protected Tile[][] campo;
    protected int altura;
    protected int largura;
    protected Lixeira[] lixeiras;
    protected Recarga[] recargas;
    
    private int totalParedes = 0;
    private int totalLixeiras = 0;
    private int totalRecargas = 0;
    private boolean moverParaDireita = true;
    
    private int posicaoParede1, posicaoParede2;
    AEstrela aes;
    
    public Sala(int tamanhoSala, int quantidadeLixeiras, int quantidadeRecargas){
    	tabelaParaImpressao.put(Tile.LIMPO, " ");
    	tabelaParaImpressao.put(Tile.LIXEIRA, "T");
    	tabelaParaImpressao.put(Tile.LIXO, ".");
    	tabelaParaImpressao.put(Tile.PAREDE, "/");
    	tabelaParaImpressao.put(Tile.RECARGA, "+");
    	tabelaParaImpressao.put(Tile.LIMPO_E_VISITADO, " ");
    	
        criaSala(tamanhoSala);
        constroiParedes();
        geraPosicoesParaDescarregarLixo(quantidadeLixeiras);
        geraPosicoesParaRecarga(quantidadeRecargas);
        geraPosicoesComLixo();
    }
    
    public Ponto[] getVizinhos(Ponto pos) {
    	Ponto[] vizinhos = new Ponto[8];
    	vizinhos[0] = pos.superiorEsquerdo();
    	vizinhos[1] = pos.superior();
		vizinhos[2] = pos.superiorDireito();
		vizinhos[3] = pos.esquerdo();
		vizinhos[4] = pos.direito();
		vizinhos[5] = pos.inferiorEsquerdo();
		vizinhos[6] = pos.inferior();
		vizinhos[7] = pos.inferiorDireito();
		return vizinhos;
    }

    private void geraPosicoesComLixo() {
        double porcentagemLixo = 0.4 + new Random().nextDouble() * 0.45;
        int lixo = (int) (porcentagemLixo *(campo.length * campo[0].length));
        
        int espacoLivre = campo.length * campo[0].length - totalParedes - totalLixeiras - totalRecargas;
        if(lixo > espacoLivre) {
            lixo = espacoLivre;
            porcentagemLixo = lixo/(1.0 * campo.length * campo[0].length);          
        }
        System.out.println("Total de lixo :" + lixo + " (" + (int)(porcentagemLixo*100) + "%)");
        
        Random valor = new Random();
        while(lixo > 0) {
            int x = valor.nextInt(campo.length);
            int y = valor.nextInt(campo[0].length);
            if(campo[y][x] == Tile.LIMPO) {
                campo[y][x] = Tile.LIXO;
                lixo--;
            }           
        }
    }
    
    public boolean temLixeiraComoVizinho(Ponto pos) {
    	Ponto[] vizinhos = getVizinhos(pos);
    	for (int i = 0; i < vizinhos.length; i++) {
    		Ponto v = vizinhos[i];
    		if (0 <= v.getX() && v.getX() < campo.length &&
		    	0 <= v.getY() && v.getY() < campo.length &&
		    	isLixeira(v))
    			return true;
    	}
    	return false;
    }
    
    public boolean temRecargaComoVizinho(Ponto pos) {
    	Ponto[] vizinhos = getVizinhos(pos);
    	for (int i = 0; i < vizinhos.length; i++) {
    		Ponto v = vizinhos[i];
    		if (0 <= v.getX() && v.getX() < campo.length &&
		    	0 <= v.getY() && v.getY() < campo.length &&
		    	isRecarga(v))
    			return true;
    	}
    	return false;
    }

    private void geraPosicoesParaRecarga(int quantidadePosicoesDeRecarga) {
        Random valor = new Random();
        recargas = new Recarga[quantidadePosicoesDeRecarga];
        while (quantidadePosicoesDeRecarga > 0) {
            int x = valor.nextInt(campo.length);
            int y = valor.nextInt(campo[0].length);
            if (isPontoValido(new Ponto(x, y)) && isDentroDasAreasParaLixeirasRegargas(y, x)) {
                Recarga pontoDeRecarga = new Recarga(x, y);
                recargas[quantidadePosicoesDeRecarga - 1] = pontoDeRecarga;
                campo[y][x] = Tile.RECARGA;
                quantidadePosicoesDeRecarga--;
            }
        }
    }

    private boolean isDentroDasAreasParaLixeirasRegargas(int x, int y) {
        if ((x < posicaoParede1 || posicaoParede2 < x) && (2 < y && y < campo.length - 2))
            return true;
        else
            return false;
    }
    
    private void geraPosicoesParaDescarregarLixo(int quantidadePosicoesDeLixeira) {
        Random valor = new Random();
        lixeiras = new Lixeira[quantidadePosicoesDeLixeira];
        while (quantidadePosicoesDeLixeira > 0) {
            int x = valor.nextInt(campo.length);
            int y = valor.nextInt(campo[0].length);
            if (isPontoValido(new Ponto(x, y)) && isDentroDasAreasParaLixeirasRegargas(y, x)) {
                Lixeira pontoDeLixeira = new Lixeira(x, y);
                lixeiras[quantidadePosicoesDeLixeira - 1] = pontoDeLixeira;
                campo[y][x] = Tile.LIXEIRA;
                quantidadePosicoesDeLixeira--;
            }
        }
    }

    private void constroiParedes() {
        if (campo == null)
            return;
        
        //assumindo por hora que a sala seja sempre quadrada.
        //tambem nao estou me preocupando que a sala seja tao pequena,
        //que as paredes acabem dividindo ela em 2 ou mais salas
        posicaoParede1 = (int) (0.25 * this.campo[0].length);
        posicaoParede2 = (int) (0.75 * this.campo[0].length) - 1;
        for (int i = 2; i < campo.length - 2; i++) {
            campo[posicaoParede1][i] = Tile.PAREDE;
            campo[posicaoParede2][i] = Tile.PAREDE;
            totalParedes += 2;
        }
        campo[posicaoParede1 - 1][2] = Tile.PAREDE;
        campo[posicaoParede1 - 1][campo.length - 3] = Tile.PAREDE;
        campo[posicaoParede2 + 1][2] = Tile.PAREDE;
        campo[posicaoParede2 + 1][campo.length - 3] = Tile.PAREDE;
        totalParedes += 4;
    }

    //tentar buscar a posicao 11x11 (corner oposto da sala, com 2 paredes em zig-zag)
    private void constroiParedesDeTeste1() {
        if (campo == null)
            return;
        
        posicaoParede1 = (int) (0.3 * this.campo[0].length);
        posicaoParede2 = (int) (0.6 * this.campo[0].length);
        for (int i = 0; i < campo.length - 2; i++) {
            campo[i][posicaoParede1] = Tile.PAREDE;
            totalParedes++;
        }
        for (int i = 2; i < campo.length; i++) {
            campo[i][posicaoParede2] = Tile.PAREDE;
            totalParedes++;
        }
    }
    
    //tentar buscar a posicao 5x5 (imediatamente atras da parede)
    private void constroiParedesDeTeste2() {
        if (campo == null)
            return;
        
        posicaoParede1 = (int) (0.4 * this.campo[0].length);
        for (int i = 2; i < campo.length - 2; i++) {
            campo[i][posicaoParede1] = Tile.PAREDE;
            totalParedes++;
        }
    }    
    
    private void criaSala(int tamanho) {
        if(tamanho < 12) 
            tamanho = 12;
        this.campo = new Tile[tamanho][tamanho];
        this.altura = campo.length;
        this.largura = campo[0].length;
        
        for(int i = 0; i < campo.length; i++) {
            for(int j = 0; j < campo[i].length ; j++)
                campo[i][j] = Tile.LIMPO;
        }       
        System.out.println("Tamanho da sala eh " + campo.length + "x" + campo[0].length);
    }
    
    public boolean isPosicaoAtualSuja(int x, int y) {
    	if (campo[y][x] == Tile.LIXO)
    		return true;
    	else
    		return false;
    }
    
    public void limpaPosicaoAtual(int x, int y) {
    	if (campo[y][x] == Tile.LIXO)
    		campo[y][x] = Tile.LIMPO_E_VISITADO;
    }
    
    public Ponto buscaLixeiraMaisProxima(int x, int y) {
    	int menorDistancia = Integer.MAX_VALUE;
    	Lixeira lixeiraEscolhida = null;
    	for (int i = 0; i < lixeiras.length; i++) {
    		int distanciaAtual = Math.abs(lixeiras[i].getX() - x) + Math.abs(lixeiras[i].getY() - y);
    		if (distanciaAtual < menorDistancia) {
    			menorDistancia = distanciaAtual;
    			lixeiraEscolhida = lixeiras[i];
    		}
    	}
    	return lixeiraEscolhida;
    }
    
    public Ponto buscaRecargaMaisProxima(int x, int y) {
    	int menorDistancia = Integer.MAX_VALUE;
    	Recarga recargaEscolhida = null;
    	for (int i = 0; i < recargas.length; i++) {
    		int distanciaAtual = Math.abs(recargas[i].getX() - x) + Math.abs(recargas[i].getY() - y);
    		if (distanciaAtual < menorDistancia) {
    			menorDistancia = distanciaAtual;
    			recargaEscolhida = recargas[i];
    		}
    	}
    	return recargaEscolhida;
    }
    
    //movimento zig-zag. Mais rapido, mas esta com problemas para garantir a limpeza Total da sala
    public Ponto proximaPosicaoParaLimpeza(Ponto posAtual) {
    	Ponto aux = posAtual;
    	do {
    		if (moverParaDireita)
    			aux = aux.direito();
    		else
    			aux = aux.esquerdo();
    		
    		if (aux.getY() >= largura) {
    			aux = aux.inferiorEsquerdo();
    			moverParaDireita = false;
    		}
    		else if (aux.getY() < 0) {
    			aux = aux.inferiorDireito();
    			moverParaDireita = true;
    		}
    	} while(!isPontoValido(aux));
    	return aux;
    }
    
    public Ponto proximaPosicaoParaLimpeza() {
    	for (int i = 0; i < campo.length; i++) {
    		for (int j = 0; j < campo[i].length; j++) {
    			if (campo[j][i] == Tile.LIXO)
    				return new Ponto(i, j);
    		}
    	}
    	return new Ponto(0, 0);
    }

    public boolean isPontoValido(Ponto ponto) {
    	if (0 <= ponto.getX() && ponto.getX() < campo.length && 
    		0 <= ponto.getY() && ponto.getY() < campo.length &&
    		!isParede(ponto) && !isLixeira(ponto) && !isRecarga(ponto))
    		return true;
    	else
    		return false;
    }    
    
    public boolean isParede(Ponto ponto) {
    	if (campo[ponto.getY()][ponto.getX()] == Tile.PAREDE)
    		return true;
    	else
    		return false;
    }
    
    public boolean isLixeira(Ponto ponto) {
    	if (campo[ponto.getY()][ponto.getX()] == Tile.LIXEIRA)
    		return true;
    	else
    		return false;
    }
    
    public boolean isRecarga(Ponto ponto) {
    	if (campo[ponto.getY()][ponto.getX()] == Tile.RECARGA)
    		return true;
    	else
    		return false;
    }
    
    //robo eh passado como parametro ja que a sala desconhece a posicao dele
    public void printSala(Automato robo) {
    	int posxRobo = robo.getPosicaoAtual().getX();
    	int posyRobo = robo.getPosicaoAtual().getY();
    	for (int i = 0; i < campo.length; i++) {
    		for (int j = 0; j < campo[0].length; j++) {
    			if (j == posyRobo && i == posxRobo)
    				System.out.print(" R ");
    			else if (aes != null && aes.caminhoPercorrido.contains(new Ponto(i, j)))
    				System.out.print(" # ");
    			else
    				System.out.print(" " + tabelaParaImpressao.get(campo[j][i]) + " ");
    		}
    		System.out.println();
    	}
    }
}