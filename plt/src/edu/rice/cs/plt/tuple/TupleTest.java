package edu.rice.cs.plt.tuple;

import junit.framework.TestCase;

public class TupleTest extends TestCase {
  
  public void testWrapper() {
    Wrapper<String> t1 = new Wrapper<String>("fish");
    Wrapper<String> t2 = Wrapper.make(new String("fish"));
    Wrapper<String> t3 = Wrapper.<String>factory().value(new String("fish"));
    assertEqualTuples(t1, t2, t3);
    assertSame("fish", t1.value());
    
    assertFalse(t1.equals(Wrapper.make("foo")));
  }
  
  public void testIdentityWrapper() {
    Wrapper<String> t1 = new IdentityWrapper<String>("fish");
    Wrapper<String> t2 = new IdentityWrapper<String>(new String("fish"));
    Wrapper<String> t3 = IdentityWrapper.make(new String("fish"));
    Wrapper<String> t4 = IdentityWrapper.<String>factory().value("fish");
    assertFalse(t1.equals(t2));
    assertFalse(t1.equals(t3));
    assertEqualTuples(t1, t4);
    assertSame("fish", t1.value());
  }
  
  public void testPair() {
    Integer i = 12;
    Double d = 3.4;
    Pair<Integer, Double> t1 = new Pair<Integer, Double>(i, d);
    Pair<Integer, Double> t2 = Pair.make(new Integer(12), d);
    Pair<Integer, Double> t3 = Pair.<Integer, Double>factory().value(i, new Double(3.4));
    assertEqualTuples(t1, t2, t3);
    assertSame(i, t1.first());
    assertSame(d, t1.second());
    
    assertFalse(t1.equals(Pair.make(12, 3.45)));
    assertFalse(t1.equals(Pair.make(13, 3.4)));
  }
  
  public void testIdentityPair() {
    Integer i = 12;
    Double d = 3.4;
    Pair<Integer, Double> t1 = new IdentityPair<Integer, Double>(i, d);
    Pair<Integer, Double> t2 = IdentityPair.make(new Integer(12), d);
    Pair<Integer, Double> t3 = IdentityPair.<Integer, Double>factory().value(i, new Double(3.4));
    Pair<Integer, Double> t4 = IdentityPair.make(i, d);
    assertFalse(t1.equals(t2));
    assertFalse(t1.equals(t3));
    assertEqualTuples(t1, t4);
    assertSame(i, t1.first());
    assertSame(d, t1.second());
  }
  
  public void testTriple() {
    Integer i = 13;
    Triple<String, Boolean, Integer> t1 = new Triple<String, Boolean, Integer>("foo", true, i);
    Triple<String, Boolean, Integer> t2 = Triple.make(new String("foo"), true, i);
    Triple<String, Boolean, Integer> t3 = Triple.<String, Boolean, Integer>factory().value("foo", true, 
                                                                                           new Integer(13));
    assertEqualTuples(t1, t2, t3);
    assertSame("foo", t1.first());
    assertSame(Boolean.TRUE, t1.second());
    assertSame(i, t1.third());
    
    assertFalse(t1.equals(Triple.make("foo", true, 14)));
    assertFalse(t1.equals(Triple.make("food", true, 13)));
    assertFalse(t1.equals(Triple.make("foo", false, 13)));
  }
  
  public void testIdentityTriple() {
    Integer i = 13;
    Triple<String, Boolean, Integer> t1 = new IdentityTriple<String, Boolean, Integer>("foo", true, i);
    Triple<String, Boolean, Integer> t2 = IdentityTriple.make(new String("foo"), true, i);
    Triple<String, Boolean, Integer> t3 = IdentityTriple.<String, Boolean, Integer>factory().value("foo", true, 
                                                                                                   new Integer(13));
    Triple<String, Boolean, Integer> t4 = IdentityTriple.make("foo", true, i);
    assertFalse(t1.equals(t2));
    assertFalse(t1.equals(t3));
    assertEqualTuples(t1, t4);
    assertSame("foo", t1.first());
    assertSame(Boolean.TRUE, t1.second());
    assertSame(i, t1.third());
  }
  
  public void OptionTest() {
    Option<String> o1 = Option.none();
    Option<String> o2 = Option.some("foo");
    try { Option.unwrap(o1, new RuntimeException("custom exception")); fail("No exception on unwrap"); }
    catch (RuntimeException e) { assertEquals("custom exception", e.getMessage()); }
    assertSame("foo", Option.unwrap(o2, new RuntimeException()));
    o1.apply(new OptionVisitor<String, Void>() {
      public Void forNone() { /* okay */ return null; }
      public Void forSome(String s) { fail("Visitor should go to none case"); return null; }
    });
    o2.apply(new OptionVisitor<String, Void>() {
      public Void forNone() { fail("Visitor should go to some case"); return null; }
      public Void forSome(String s) { assertSame("foo", s); return null; }
    });
  }
    
  
  private void assertEqualTuples(Tuple... tuples) {
    boolean first = true;
    Tuple t1 = null;
    for (Tuple t2 : tuples) {
      if (first) { first = false; }
      else {
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertEquals(t1.toString(), t2.toString());
      }
      t1 = t2;
    }
  }
  
}
