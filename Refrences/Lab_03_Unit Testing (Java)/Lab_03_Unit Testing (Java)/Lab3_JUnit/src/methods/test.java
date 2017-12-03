package methods;
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("foo 1,2,1,2 = "+foo(1,2,1,2));	
		System.out.println("bug 1 = "+bug(1));
		System.out.println("1,2,1,1 = "+foo(1,2,1,1));
		//System.out.println("2,1,1,1 = "+foo(2,1,1,1));
		System.out.println("1,2,1,2 = "+foo(1,2,1,2));
	}
	public static int foo (int a, int b, int c, int d) {
		int e;
		if (a == 0) {
			return 0;
		}
		int x = 0;
		if ((a==b) || ((c == d) && bug(a) )) {
			x=1;
		}
		e = 1/x;
		return e;
	} 
	public static boolean  bug(int m) {
		if (m != 1) {
			return true;
		}
		else {
			return false;
		}
	}

}
