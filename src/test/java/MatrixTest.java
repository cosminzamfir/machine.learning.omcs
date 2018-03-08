import ml.model.function.Function;
import ml.model.function.NormalFunction;

public class MatrixTest {

	public static void main(String[] args) {
		Function f = new NormalFunction(0, 1);
		f.plot(-3, 3, 10000);

		for (int i = 0; i < 10; i++) {
			final Function tmp = f;
			Function fd  = (x -> tmp.derivativeAt(x, 0.01));
			//fd.plot(-3, 3, 1000, String.valueOf(i));
			f = fd;
		}
		f.plot(-3, 3, 10000);
	}}