public class D implements IntBox {
  public static String NAME = "D";
  public static String getName() { return NAME; }
  public int get() { return new A().get() + new B().get() + new C().get() - 2; }
}
