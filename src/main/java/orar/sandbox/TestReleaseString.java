package orar.sandbox;

public class TestReleaseString {
 private void releaseString(String s){
	
	 s=null;
 }
 
 private void increase(int i){
	 i++;
 }
public static void main(String[] args){
	TestReleaseString test= new TestReleaseString();
	String s="abc";
	System.out.println(s);
	test.releaseString(s);
	System.out.println(s);
	
	int i=1;
	System.out.println(i);
	test.increase(i);
	System.out.println(i);
}
}
