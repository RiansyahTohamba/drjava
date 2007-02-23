package edu.rice.cs.plt.lambda;

import java.io.Serializable;

/**
 * <p>A thunk whose value is set <em>once</em> after creation, but before the first 
 * invocation of {@link #value}.</p>
 * 
 * <p>As a wrapper for arbitrary objects, instances of this class will serialize without error
 * only if the wrapped object is serializable.</p>
 */
public class DelayedThunk<R> implements Box<R>, Serializable {
  
  private R _val;
  private boolean _initialized;
  
  public DelayedThunk() {
    _initialized = false;
    // the value of _val doesn't matter
  }
  
  /**
   * Access the value.
   * @throws IllegalStateException  if the value has not been set
   */
  public R value() {
    if (!_initialized) { throw new IllegalStateException("DelayedThunk is not initialized"); }
    return _val;
  }
  
  /**
   * Set the value.
   * @throws IllegalStateException  if the value has already been set
   */
  public void set(R val) {
    if (_initialized) { throw new IllegalStateException("DelayedThunk is already initialized"); }
    _val = val;
    _initialized = true;
  }
  
  /** Call the constructor (allows {@code T} to be inferred) */
  public static <R> DelayedThunk<R> make() { return new DelayedThunk<R>(); }
  
}
