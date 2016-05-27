package util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class ReflectionHelper {

	/** 
	 * Cache the already introspected {@link Class#getFields()}
	 * The cache is stored per thread to insure thread-safety without synchronization oveerhead
	 * */
	public static final ThreadLocal<Map<Class<?>, Map<String, Field>>> classFieldsCache = new ThreadLocal<Map<Class<?>, Map<String, Field>>>() {
		protected Map<java.lang.Class<?>, java.util.Map<String, Field>> initialValue() {
			return new HashMap<Class<?>, Map<String, Field>>();
		};
	};

	/**Cache the already introspected PropertyDescriptors */
	public static final Map<Class<?>, Map<String, PropertyDescriptor>> propDescCache = new HashMap<Class<?>, Map<String, PropertyDescriptor>>();

	public static ThreadLocal<NumberFormat> numberFormat = new ThreadLocal<NumberFormat>() {
		protected NumberFormat initialValue() {
			return NumberFormat.getInstance();
		};
	};

	/**
	 * Create a new instance of the given class name for the given constructor arguments
	 * @param className the name of the class to instantiate
	 * @param args the constructor arguments
	 * @throws CLIPException if there is not matching constructor
	 */
	public static Object createObject(String className, Object... arguments) {
		try {
			return createObject(Class.forName(className), arguments);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Create a new instance of the given Class for the given constructor arguments
	 * @param klass the type 
	 * @param args the constructor arguments
	 * @throws CLIPException if there is not matching constructor
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createObject(Class<?> klass, Object... args) {
		Constructor<?>[] constructors = klass.getConstructors();
		for (Constructor<?> c : constructors) {
			if (typeMatch(c.getParameterTypes(), args)) {
				try {
					return (T) c.newInstance(args);
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}
		}
		throw new RuntimeException();
	}

	/**
	 * 
	 * @param parameterTypes
	 * @param args
	 * @return true if the specified args are of the same number and type as the given parameterTypes
	 */
	private static boolean typeMatch(Class<?>[] parameterTypes, Object[] args) {
		if (parameterTypes.length != args.length) {
			return false;
		}
		int index = 0;
		for (Object arg : args) {
			if (arg != null && !(parameterTypes[index++].isAssignableFrom(arg.getClass()))) {
				return false;
			}

		}
		return true;
	}

	/**
	 * Get the value of the specified attribute for the given object
	 * <p>
	 * The attribute must comply with the JavaBean convention, i.e. the public [type] get[AttributeName]() method must exist
	 * @return the 
	 */
	public static Object getPropertyValue(String attributeName, Object o) {
		PropertyDescriptor descriptor = getPropertyDescriptor(attributeName, o);
		if (descriptor == null) {
			throw new RuntimeException("No attribue with name " + attributeName + " found in " + o.getClass().getSimpleName());
		}
		if (descriptor.getReadMethod() == null) {
			throw new RuntimeException("No read method for " + o.getClass().getSimpleName() + "." + descriptor.getName());
		}
		try {
			return descriptor.getReadMethod().invoke(o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object setPropertyValue(Object o, String attributeName, String value) {
		PropertyDescriptor descriptor = getPropertyDescriptor(attributeName, o);
		if (descriptor == null) {
			throw new RuntimeException("No attribue with name " + attributeName + " found in " + o.getClass().getSimpleName());
		}
		if (descriptor.getWriteMethod() == null) {
			throw new RuntimeException("No write method for " + o.getClass().getSimpleName() + "." + descriptor.getName());
		}
		try {
			Object convertedValue = convert(descriptor.getPropertyType(), value);
			return descriptor.getWriteMethod().invoke(o, convertedValue);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private static PropertyDescriptor getPropertyDescriptor(String propertyName, Object o) {
		return getPropertyDescriptor(propertyName, o.getClass());
	}

	private static PropertyDescriptor getPropertyDescriptor(String propertyName, Class<?> klass) {
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(klass);
		} catch (IntrospectionException e) {
			throw new RuntimeException("Cannot introspect class " + klass.getName(), e);
		}
		PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
		for (PropertyDescriptor descriptor : descriptors) {
			if (descriptor.getName().equals(propertyName)) {
				return descriptor;
			}
		}
		return null;
	}

	/**
	 * Get the value of the specified attribute for the given object using {@link Field} level reflection
	 * <p>
	 * No Java Bean convention required
	 * @param fieldName the name of the field. Can be a composite name, e.g. on a Leg instance: racePosition.id.posCode
	 */
	public static Object getFieldValue(String fieldName, Object o) {
		String[] fieldNames = StringUtils.split(fieldName, '.');
		Object res = o;
		for (String f : fieldNames) {
			if (res == null) {
				return null;
			}
			Field field = getField(f, res.getClass());
			if (field == null) {
				throw new RuntimeException();
			}
			try {
				res = field.get(res);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
		return res;
	}

	/**
	 * Retrieve the {@link Field} for the specified {@link Class} and fieldName
	 * <p>
	 * Sets the field to {@link Field#setAccessible(boolean)} to allow getting its value by reflection
	 * @param fieldName
	 * @param class1
	 * @return
	 */
	private static Field getField(String fieldName, Class<? extends Object> klass) {
		Map<String, Field> cached = classFieldsCache.get().get(klass);
		if (cached == null) {
			cacheFields(klass);
			cached = classFieldsCache.get().get(klass);
		}
		return cached.get(fieldName);

	}

	public static Map<String, Field> getFields(Class<? extends Object> klass) {
		Map<String, Field> cached = classFieldsCache.get().get(klass);
		if (cached == null) {
			cacheFields(klass);
		}
		return classFieldsCache.get().get(klass);
	}

	private static void cacheFields(Class<? extends Object> klass) {
		try {
			Class<? extends Object> tmp = klass;
			Map<String, Field> cached = new TreeMap<String, Field>();
			Field[] fields = tmp.getDeclaredFields();
			while (tmp.getSuperclass() != null) {
				fields = (Field[]) ArrayUtils.addAll(fields, tmp.getSuperclass().getDeclaredFields());
				tmp = tmp.getSuperclass();
			}

			for (Field field : fields) {
				field.setAccessible(true);
				cached.put(field.getName(), field);
			}
			classFieldsCache.get().put(klass, cached);

		} catch (SecurityException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Set the attribute values of the given object instance 
	 * @param instance the object instance
	 * @param properties the attribute names
	 * @param values the string representation of the attribute values
	 */
	public static void setPropertyValues(Object instance, List<String> properties, List<String> values) {
		for (int i = 0; i < properties.size(); i++) {
			String attribute = properties.get(i);
			String value = values.get(i);
			setPropertyValue(instance, attribute, value);
		}
	}

	/**
	 * Set the value of the specified attribute using Field level reflection
	 * @param obj the object containing the attribute
	 * @param attribute the attribute name
	 * @param value the string representation, it is converted to proper data type value
	 */
	public static void setValue(Object obj, String attribute, String value) {
		Field field = getField(attribute, obj.getClass());
		if (field == null) {
			throw new RuntimeException();
		}
		field.setAccessible(true);
		Object convertedValue = convert(field.getType(), value);
		try {
			field.set(obj, convertedValue);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Convert the specified String representation to an instance of the specified type
	 */
	private static Object convert(Class<?> type, String value) {
		if (type == String.class) {
			return value;
		} else if (type == Long.class || type == Long.TYPE) {
			return Long.valueOf(value);
		} else if (type == Integer.class || type == Integer.TYPE) {
			return Integer.valueOf(value);
		} else if (type == Double.class || type == Double.TYPE) {
			return Double.valueOf(value);
		} else if (type == Date.class) {
			return DateUtils.parse(value);
		} else {
			throw new RuntimeException();
		}
	}

	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException();
		}
	}

	public static <T extends Object, Y extends Object> void clone(T from, Y too) {

		Class<? extends Object> fromClass = from.getClass();
		Field[] fromFields = fromClass.getDeclaredFields();

		Class<? extends Object> tooClass = too.getClass();
		Field[] tooFields = tooClass.getDeclaredFields();

		if (fromFields != null && tooFields != null) {
			for (Field tooF : tooFields) {
				try {
					// Check if that fields exists in the other method
					Field fromF = fromClass.getDeclaredField(tooF.getName());
					if (fromF.getType().equals(tooF.getType())) {
						if (!Modifier.isStatic(fromF.getModifiers())) {
							tooF.setAccessible(true);
							fromF.setAccessible(true);
							tooF.set(too, fromF.get(from));
						}
					}
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}

		}
	}
}
