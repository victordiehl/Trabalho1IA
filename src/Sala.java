import java.util.Random;

public class Sala {
    
    public enum Tile { LIMPO, LIXEIRA, LIXO, PAREDE, RECARGA, LIMPO_E_VISITADO };
    
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
        criaSala(tamanhoSala);
        constroiParedes();
        geraPosicoesParaDescarregarLixo(quantidadeLixeiras);
        geraPosicoesParaRecarga(quantidadeRecargas);
        geraPosicoesComLixo();
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
        while (quantidadePosicoesDeRecarga > 0) {
            int x = valor.nextInt(campo.length);
            int y = valor.nextInt(campo[0].length);
            if (isDentroDasAreasParaLixeirasRegargas(x, y)) {
                Recarga pontoDeRecarga = new Recarga(x, y);
                recargas[quantidadePosicoesDeRecarga - 1] = pontoDeRecarga;
                quantidadePosicoesDeRecarga--;
            }
        }
    }

    private boolean isDentroDasAreasParaLixeirasRegargas(int x, int y) {
        //p1, p2, e a altura das paredes foram calculadas em 'constroiParedes'
        //Seria interessante move-las como atributos da classe
        if ((x < posicaoParede1 || x > posicaoParede2) && (y > 2 || y < this.campo.length - 2))
            return true;
        else
            return false;
    }
    
    private void geraPosicoesParaDescarregarLixo(int quantidadePosicoesDeLixeira) {
        Random valor = new Random();        
        while (quantidadePosicoesDeLixeira > 0) {
            int x = valor.nextInt(campo.length);
            int y = valor.nextInt(campo[0].length);
            if (isDentroDasAreasParaLixeirasRegargas(x, y)) {
                Lixeira pontoDeLixeira = new Lixeira(x, y);
                lixeiras[quantidadePosicoesDeLixeira - 1] = pontoDeLixeira;
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
        posicaoParede1 = (int) 0.25 * this.campo[0].length;
        posicaoParede2 = (int) 0.75 * this.campo[0].length;
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
    	if (campo[x][y] == Tile.LIXO)
    		campo[x][y] = Tile.LIMPO_E_VISITADO;
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
}