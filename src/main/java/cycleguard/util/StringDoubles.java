package cycleguard.util;

public class StringDoubles {
	public static double toDouble(String s) {
		return Double.parseDouble(s);
	}
	public static String toString(double d) {
		return String.format("%.1f", d);
	}
}
