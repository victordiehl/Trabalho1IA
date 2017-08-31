import java.util.Random;

public class Sala {
    
    public enum Tile { LIMPO, LIXEIRA, LIXO, PAREDE, RECARGA };
    
    protected Tile[][] campo;
    protected int altura;
    protected int largura;
    protected Lixeira[] lixeiras;
    protected Recarga[] recargas;
    
    private int totalParedes = 0;
    private int totalLixeiras;
    private int totalRecargas;
    
    //PROBLEMA! parametros sobreescrevem o array de Lixeiras e Recargas. Devemos renomear os parametros.
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

    private void geraRecargas(int recargas) {
        Random valor = new Random();        
        while (recargas > 0) {
            int x = valor.nextInt(campo.length);
            int y = valor.nextInt(campo[0].length);
            if (dentroDasAreasParaLixeirasRegargas(x, y)) {
                Recarga pontoDeRecarga = new Recarga(x, y);
                recargas[recargas - 1] = pontoDeRecarga;
                recargas--;
            }
        }
    }

    private boolean dentroDasAreasParaLixeirasRecargas(int x, int y) {
        //p1, p2, e a altura das paredes foram calculadas em 'constroiParedes'
        //Seria interessante move-las como atributos da classe
        if ((x < p1 || x > p2) && (y > 2 || y < this.campo.length - 2))
            return true;
        else
            return false;
    }
    
    private void geraLixeiras(int lixeiras) {
        Random valor = new Random();        
        while (lixeiras > 0) {
            int x = valor.nextInt(campo.length);
            int y = valor.nextInt(campo[0].length);
            if (dentroDasAreasParaLixeirasRegargas(x, y)) {
                Lixeira pontoDeLixeira = new Lixeira(x, y);
                lixeiras[lixeiras - 1] = pontoDeLixeira;
                lixeiras--;
            }
        }
    }

    private void constroiParedes(int ) {
        if (campo == null)
            return;
        
        //assumindo por hora que a sala seja sempre quadrada.
        //tambem nao estou me preocupando que a sala seja tao pequena,
        //que as paredes acabem dividindo ela em 2 ou mais salas
        int p1 = (int) 0.25 * this.campo[0].length;
        int p2 = (int) 0.75 * this.campo[0].length;
        for (int i = 2; i < campo.length - 2; i++) {
            campo[i][p1] = Tile.PAREDE;
            campo[i][p2] = Tile.PAREDE;
            totalParedes++;
        }
        campo[2][p1 - 1] = Tile.PAREDE;
        campo[campo.length - 3][p1 - 1] = Tile.PAREDE;
        campo[2][p2 + 1] = Tile.PAREDE;
        campo[campo.length - 3][p2 + 1] = Tile.PAREDE;
        totalParedes += 4;
    }

    private void criaSala(int tamanho) {
        if(tamanho < 12) 
            tamanho = 12;
        this.campo = new Tile[tamanho][tamanho];
        this.altura = campo.length;
        this.largura = campo[0].length;
        
        for(int i = 0; i < campo.length; i++) {
            for(int j = 0; j < campo[i].length ; j++) {
                campo[i][j] = Tile.LIMPO;
            }
        }       
        System.out.println("Tamanho da sala eh " + campo.length + "x" + campo[0].length);
        System.out.println("Total de espacos: " + campo.length * campo[0].length);
    }
    
}