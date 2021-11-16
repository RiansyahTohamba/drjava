class Isolated_addToBrowserHistory{
	// CINT = 8 matches
	// addToBrowserHistory=6.625

	public void addToBrowserHistory(boolean before) {
	     _log.log("AbstractGlobalModel.addToBrowserHistory(" + before + ") called");
	    _notifier.updateCurrentLocationInDoc();
	    final OpenDefinitionsDocument doc = getActiveDocument();
	    assert (doc != null && EventQueue.isDispatchThread()) || Utilities.TEST_MODE;
	    
	    Position startPos = null;
	    Position endPos = null;
	    try {
	      // todo: extract
	      int pos = doc.getCaretPosition();
	      startPos = doc.createPosition(pos);
	      endPos = startPos; 
	    }
	    
	    catch (BadLocationException ble) { throw new UnexpectedException(ble); }
	    BrowserDocumentRegion r = new BrowserDocumentRegion(doc, startPos, endPos);
	    // todo:extract
	    if (before) _browserHistoryManager.addBrowserRegionBefore(r, _notifier);
	    else _browserHistoryManager.addBrowserRegion(r, _notifier);
  }
}