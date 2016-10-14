package util;

/**
 * To allow mutating objects in lambda expressions (compiler complains about final)
 * @author eh2zamf
 *
 */
public class ObjectHolder {
	private Object value;

	public ObjectHolder(Object value) {
		super();
		this.value = value;
	}
	
	public Object get() {
		return value;
	}

	public void set(Object value) {
		this.value = value;
	}

}
