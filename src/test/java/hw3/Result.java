package hw3;

public class Result {

	private int x;
	private int y;
	private int maxNorm;

	public Result(int x, int y, int maxNorm) {
		super();
		this.x = x;
		this.y = y;
		this.maxNorm = maxNorm;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getMaxNorm() {
		return maxNorm;
	}

	public void setMaxNorm(int maxNorm) {
		this.maxNorm = maxNorm;
	}
	
	@Override
	public String toString() {
		return "x=" + x + ";y=" + y + ";maxNorm=" + maxNorm;
	}

}
