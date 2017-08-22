import java.util.Random;

public class Sala {
	
	public enum Tile { LIMPO, LIXEIRA, LIXO, PAREDE, RECARGA};
	
	protected Tile[][] campo;
	protected int altura;
	protected int largura;
	protected Lixeira[] lixeiras;
	protected Recarga[] recargas;
	
	private int totalParedes;
	private int totalLixeiras;
	private int totalRecargas;
	
	public Sala(int tamanho, int lixeiras, int recargas){
		criaSala(tamanho);
		constroiParedes();
		geraLixeiras(lixeiras);
		geraRecargas(recargas);
		geraLixo();
	}

	private void geraLixo() {
		double porcentagemLixo = 0.4 + new Random().nextDouble() * 0.45;
		int lixo = (int) (porcentagemLixo *(campo.length * campo[0].length));
		
		int espaçoLivre = campo.length * campo[0].length - totalParedes - totalLixeiras - totalRecargas;
		if(lixo > espaçoLivre) {
			lixo = espaçoLivre;
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

	private void geraRecargas(int recargas) {
		// TODO Auto-generated method stub
		
	}

	private void geraLixeiras(int lixeiras) {
		// TODO Auto-generated method stub
		
	}

	private void constroiParedes() {
		// TODO Auto-generated method stub
		
	}

	private void criaSala(int tamanho) {
		if(tamanho < 12) tamanho = 12;
		this.campo = new Tile[tamanho][tamanho];
		this.altura = campo.length;
		this.largura = campo[0].length;
		
		for(int i=0; i< campo.length; i++) {
			for(int j= 0; j < campo[0].length ; j++) {
				campo[i][j] = Tile.LIMPO;
			}
		}		
		System.out.println("Tamanho da sala é " + campo.length + "x" + campo[0].length);
		System.out.println("Total de espaços: " + campo.length * campo[0].length);
	}
	
}