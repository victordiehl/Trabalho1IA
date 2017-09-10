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
    
    private int posicaoParede1, posicaoParede2;
    
    public Sala(int tamanhoSala, int quantidadeLixeiras, int quantidadeRecargas){
    	tabelaParaImpressao.put(Tile.LIMPO, "*");
    	tabelaParaImpressao.put(Tile.LIXEIRA, "T");
    	tabelaParaImpressao.put(Tile.LIXO, ".");
    	tabelaParaImpressao.put(Tile.PAREDE, "/");
    	tabelaParaImpressao.put(Tile.RECARGA, "+");
    	tabelaParaImpressao.put(Tile.LIMPO_E_VISITADO, " ");
    	
        criaSala(tamanhoSala);
        constroiParedes();
        //geraPosicoesParaDescarregarLixo(quantidadeLixeiras);
        //geraPosicoesParaRecarga(quantidadeRecargas);
        //geraPosicoesComLixo();
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

    private void geraPosicoesParaRecarga(int quantidadePosicoesDeRecarga) {
        Random valor = new Random();
        recargas = new Recarga[quantidadePosicoesDeRecarga];
        while (quantidadePosicoesDeRecarga > 0) {
            int x = valor.nextInt(campo.length);
            int y = valor.nextInt(campo[0].length);
            if (isValido(new Ponto(y, x)) && isDentroDasAreasParaLixeirasRegargas(x, y)) {
                Recarga pontoDeRecarga = new Recarga(y, x);
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
            if (isValido(new Ponto(y, x)) && isDentroDasAreasParaLixeirasRegargas(x, y)) {
                Lixeira pontoDeLixeira = new Lixeira(y, x);
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
            campo[i][posicaoParede1] = Tile.PAREDE;
            campo[i][posicaoParede2] = Tile.PAREDE;
            totalParedes++;
        }
        campo[2][posicaoParede1 - 1] = Tile.PAREDE;
        campo[campo.length - 3][posicaoParede1 - 1] = Tile.PAREDE;
        campo[2][posicaoParede2 + 1] = Tile.PAREDE;
        campo[campo.length - 3][posicaoParede2 + 1] = Tile.PAREDE;
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
    
    public void limpaPosicaoAtual(int x, int y) {
    	if (campo[y][x] == Tile.LIMPO)
    		campo[y][x] = Tile.LIMPO_E_VISITADO;
    }
    
    public Ponto buscaLixeiraMaisProxima(int x, int y) {
    	int menorDistancia = Integer.MAX_VALUE;
    	Lixeira lixeiraEscolhida = null;
    	for (int i = 0; i < lixeiras.length; i++) {
    		int distanciaAtual = Math.abs(lixeiras[i].getX() - x) + Math.abs(lixeiras[i].getY() - y);
    		if (distanciaAtual < menorDistancia)
    			lixeiraEscolhida = lixeiras[i];
    	}
    	return lixeiraEscolhida;
    }
    
    public Ponto buscaRecargaMaisProxima(int x, int y) {
    	int menorDistancia = Integer.MAX_VALUE;
    	Recarga recargaEscolhida = null;
    	for (int i = 0; i < recargas.length; i++) {
    		int distanciaAtual = Math.abs(recargas[i].getX() - x) + Math.abs(recargas[i].getY() - y);
    		if (distanciaAtual < menorDistancia)
    			recargaEscolhida = recargas[i];
    	}
    	return recargaEscolhida;
    }

    public boolean isValido(Ponto ponto) {
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
    			if (i == posyRobo && j == posxRobo)
    				System.out.print(" R ");
    			else
    				System.out.print(" " + tabelaParaImpressao.get(campo[i][j]) + " ");
    		}
    		System.out.println();
    	}
    }
}