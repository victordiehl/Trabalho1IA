
public class Ponto {
	private int X, Y;
	
	public Ponto(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}
	
	public Ponto superiorEsquerdo() {
		return new Ponto(this.X - 1, this.Y - 1);
	}
	
	public Ponto superior() {
		return new Ponto(this.X, this.Y - 1);
	}
	
	public Ponto superiorDireito() {
		return new Ponto(this.X + 1, this.Y - 1);
	}
	
	public Ponto esquerdo() {
		return new Ponto(this.X - 1, this.Y);
	}
	
	public Ponto direito() {
		return new Ponto(this.X + 1, this.Y);
	}
	
	public Ponto inferiorEsquerdo() {
		return new Ponto(this.X - 1, this.Y + 1);
	}
	
	public Ponto inferior() {
		return new Ponto(this.X, this.Y + 1);
	}
	
	public Ponto inferiorDireito() {
		return new Ponto(this.X + 1, this.Y + 1);
	}
	
	@Override
	public boolean equals(Object p) {
		return this.X == ((Ponto)p).getX() && this.Y == ((Ponto)p).getY();
	}
}
