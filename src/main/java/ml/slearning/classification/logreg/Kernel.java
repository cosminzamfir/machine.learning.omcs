package ml.slearning.classification.logreg;

/**
 * Increase the dimension of x by adding powers of x or other transformations.
 * To support non linear clasiffications
 * 
 * @author Cosmin Zamfir
 */
public interface Kernel {

	double[] transform(double[] x); 
}
