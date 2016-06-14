package util;

/**
 * To allow incrementing ints in lambda expressions (compiler complains about final)
 * @author eh2zamf
 *
 */
public class IntHolder {
	private int value;

	public IntHolder(int value) {
		super();
		this.value = value;
	}
	
	public int get() {
		return value;
	}

	public void set(int value) {
		this.value = value;
	}
	
	public void add(int value) {
		this.value += value;
	}
	
	public int addAndGet(int value) {
		this.value += value;
		return value;
		
	}

	public int getAndAdd(int i) {
		int res = value;
		this.value+=i;
		return res;
	}
	

}
