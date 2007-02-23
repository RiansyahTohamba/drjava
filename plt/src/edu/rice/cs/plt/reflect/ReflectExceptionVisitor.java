package edu.rice.cs.plt.reflect;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>A visitor for {@link ReflectException}s.  By default, each method simply defers to the {@link #defaultCase}
 * method.  An implementation should define {@code defaultCase} and override any methods corresponding to
 * exceptions that deserve special treatment.</p>
 * 
 * <p>If, as is often the case in exception handling, no return value is required, the return type can be
 * specified as {@link Void}, and all methods should simply return {@code null}.</p>
 */
public abstract class ReflectExceptionVisitor<T> {
  
  protected abstract T defaultCase(Exception e);
  
  /**
   * Handle a {@code ClassNotFoundException}, which can occur if a class name does not refer to
   * any available classes.
   */
  public T forClassNotFound(ClassNotFoundException e) { return defaultCase(e); }
  
  /** 
   * Handle a {@code NoSuchFieldException}, which can occur when a field name does not refer to
   * a publicly-accessible field of a class.
   */
  public T forNoSuchField(NoSuchFieldException e) { return defaultCase(e); }

  /** 
   * Handle a {@code NoSuchMethodException}, which can occur when a method or constructor name 
   * and signature do not refer to a publicly-accessible method of a class.
   */
  public T forNoSuchMethod(NoSuchMethodException e) { return defaultCase(e); }
  
  /**
   * Handle an {@code IllegalArgumentException}, which can occur if an object whose member is
   * being accessed does not have the required type, or if the arguments to a method or constructor
   * do not match the required signature.  (It can also occur in the rare circumstance that an
   * accessible enum constructor reflectively is invoked.)
   */
  public T forIllegalArgument(IllegalArgumentException e) { return defaultCase(e); }
  
  /**
   * Handle a {@code NullPointerException}, which can occur when statically accessing a non-static
   * member, or when attempting to access a member of the object {@code null}.
   */
  public T forNullPointer(NullPointerException e) { return defaultCase(e); }
  
  /**
   * Handle a {@code ClassCastException}, which can occur when attempting to dynamically cast
   * an object to the generic type of a class object.
   * @see ReflectUtil#cast
   */
  public T forClassCast(ClassCastException e) { return defaultCase(e); }
  
  /**
   * Handle an {@code InvocationTargetException}, which occurs whenever a {@link Throwable}
   * is thrown by a reflectively-executed method or constructor.  Note that <em>all</em> throwables
   * are wrapped in this way, and so a good practice would be to re-throw any that are unexpected 
   * (such as errors).  Because {@code InvocationTargetException} allows construction without
   * a {@code cause} parameter, {@code e.getCause()} may be {@code null}.
   */
  public T forInvocationTarget(InvocationTargetException e) { return defaultCase(e); }
  
  /**
   * Handle an {@code InstantiationException}, which can occur if an attempt is made to construct
   * a non-constructable class (such as an abstract class).
   */
  public T forInstantiation(InstantiationException e) { return defaultCase(e); }
  
  /**
   * Handle an {@code IllegalAccessException}, which occurs when an attempt is made to write to a
   * final field or to access an inaccessible class or class member.
   */
  public T forIllegalAccess(IllegalAccessException e) { return defaultCase(e); }
  
  /**
   * Handle a {@code SecurityException}, which can occur whenever a security manager restricts access
   * to reflection operations.
   */
  public T forSecurity(SecurityException e) { return defaultCase(e); }
}
