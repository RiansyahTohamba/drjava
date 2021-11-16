/* 
  SUDAH REFACTORING

  _createDefScrollPane
    extract method = 1x

	dari MainFrame
	LOC = 8311 - 8173 = 138

	DCH \w+.\w+() = 40 matches ?
		
	extract MainFrameScrollPane from MainFrame
		38 outgoing dependence
		apa bagusnya di extract saja? ya !
	MainFrame ini besar sekali, kita harus extract dengan hati-hati
*/
class MainFrame{
	/** Create new DefinitionsPane and JScrollPane for an open definitions document.  Package private for testing purposes.
    * @param doc The open definitions document to wrap
    * @return JScrollPane containing a DefinitionsPane for the given document.
    */
  JScrollPane _createDefScrollPane(OpenDefinitionsDocument doc) {

    DefinitionsPane pane = new DefinitionsPane(this, doc);
    pane.addKeyListener(_historyListener);
    pane.addFocusListener(_focusListenerForRecentDocs);
    
    // Add listeners
    _installNewDocumentListener(doc);
    ErrorCaretListener caretListener = new ErrorCaretListener(doc, pane, this);
    pane.addErrorCaretListener(caretListener);
    /*
      didalam addDocumentListener ada
      1. updateUI
      2. reloadPanel
    */
    doc.addDocumentListener(new DocumentUIListener() {
      /** Updates panel displayed in interactions subwindow. */
      private void updateUI(OpenDefin itionsDocument doc, int offset) {
        assert EventQueue.isDispatchThread();
        Component c = _tabbedPane.getSelectedComponent();
        if (c instanceof RegionsTreePanel<?>) {
          reloadPanel((RegionsTreePanel<?>) c, doc, offset);
        }
      }

      public void changedUpdate(DocumentEvent e) { }
      public void insertUpdate(DocumentEvent e) {
        updateUI(((DefinitionsDocument) e.getDocument()).getOpenDefDoc(), e.getOffset()); 
      }
      public void removeUpdate(DocumentEvent e) {
        updateUI(((DefinitionsDocument) e.getDocument()).getOpenDefDoc(), e.getOffset());
      }

    });
    
    // add a listener to update line and column.
    pane.addCaretListener(_posListener);
    
    // add a focus listener to this definitions pane.
    pane.addFocusListener(new LastFocusListener());
    
    // Add to a scroll pane
    final JScrollPane scroll = 
      new BorderlessScrollPane(pane,
              ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
      );
    pane.setScrollPane(scroll);
    //scroll.setBorder(null); // removes all default borders (MacOS X installs default borders)
    if (DrJava.getConfig().getSetting(LINEENUM_ENABLED).booleanValue()) {
      scroll.setRowHeaderView(new LineEnumRule(pane));
    }
    _defScrollPanes.put(doc, scroll);
    return scroll;
  }
  // coarsely update the displayed RegionsTreePanel
  private <R extends OrderedDocumentRegion> void reloadPanel(final RegionsTreePanel<R> p,
                                                             final OpenDefinitionsDocument doc,
                                                             int offset)
  {
    final RegionManager<R> rm = p.getRegionManager();
    SortedSet<R> regions = rm.getRegions(doc);
    if (regions == null || regions.size() == 0) return;
    // Adjust line numbers and line bounds if insert involves newline
    final int numLinesChangedAfter = doc.getDocument().getAndResetNumLinesChangedAfter();
    // interval regions that need line number updating
    Pair<R, R> lineNumInterval = null;
    if (numLinesChangedAfter >= 0)  {
      // insertion/deletion included a newline
      // Update the bounds of the affected regions
      // TODO: These casts are bad! R is not always StaticDocumentRegion (of course).
      // The code only works because the RegionManager implementations happen to not strictly
      // require values of type R.  Either the interface for RegionManager.updateLines()
      // and RegionManager.reload() needs to be generalized, or a means for creating
      // values that are truly of type R needs to be provided.
      @SuppressWarnings("unchecked") R start =
              (R) new StaticDocumentRegion(doc, numLinesChangedAfter, numLinesChangedAfter);
      int len = doc.getLength();
      @SuppressWarnings("unchecked") R end = (R) new StaticDocumentRegion(doc, len, len);
      lineNumInterval = Pair.make(start, end);
    }

    Pair<R, R> interval = rm.getRegionInterval(doc, offset);
    if (interval == null && lineNumInterval == null) return;
    interval = maxInterval(lineNumInterval, interval);

    if (setRegion(p, doc, rm)) return;
    // Queue a request to perform the update

    updateMillis();
  }
  private void updateMillis() {
    _threadPool.submit(new Runnable() {
      // Create and run a new aynchronous task that waits UPDATE_DELAY millis, then performs update in event thread
      public void run() {
        Thread.currentThread().setPriority(UPDATER_PRIORITY);
        synchronized(_updateLock) {
          try { // _pendingUpdate can be updated during waits
            do {
              _waitAgain = false;
              _updateLock.wait(UPDATE_DELAY);
            }
            while (_waitAgain);
          }
          catch(InterruptedException e) { /* fall through */ }
          _tabUpdatePending = false;
        } // end synchronized
        Utilities.invokeLater(_pendingUpdate);
      }
    });
  }

  private <R extends OrderedDocumentRegion> boolean setRegion(final RegionsTreePanel<R> p, OpenDefinitionsDocument doc, final RegionManager<R> rm) {
    final R first = interval.first();
    final R last = interval.second();

    synchronized(_updateLock) {
      if (_tabUpdatePending && _pendingDocument == doc) {  // revise and delay existing task
        _firstRegion = _firstRegion.compareTo(first) <= 0 ? _firstRegion : first;
        _lastRegion = _lastRegion.compareTo(last) >= 0 ? _lastRegion : last;
        _waitAgain = true;
        return true;
      } else {  // create a new update task
        _firstRegion = first;
        _lastRegion = last;
        _pendingDocument = doc;
        _tabUpdatePending = true;
        _pendingUpdate = new Runnable() { // this Runnable only runs in the event thread
          public void run() {
            // TODO: Bad casts!  There's probably no guarantee that R is consistent between invocations,
            // and even if there were, this is a confusing way to go about this process.
            // See above discussion for alternatives.
            @SuppressWarnings("unchecked") R first = (R) _firstRegion;
            @SuppressWarnings("unchecked") R last = (R) _lastRegion;
            rm.updateLines(first, last); // recompute _lineStartPos, _lineEndPos in affected regions
            p.reload(first, last);  // reload the entries whose length may have changed
            p.repaint();
          }
        };  // end _pendingUpdate Runnable
      }
    }
    return false;
  }

}
  