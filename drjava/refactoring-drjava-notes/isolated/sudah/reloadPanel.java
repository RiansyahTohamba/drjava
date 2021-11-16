/* 
SUDAH REFACTOR

	ExtractMethod 2x: setRegion, updateMillis

	MainFrame$240 untuk reloadPanel?
		MainFrame$240 karena penulisan kode nya seperti ini
		MainFrame._createDefScrollPane.addDocumentListener.reloadPanel

	class MainFrame{
		JScrollPane _createDefScrollPane(){
			doc.addDocumentListener(new DocumentUIListener() {
				reloadPanel(3-params)
			)}
		}
	}
*/
class MainFrame{
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
  

}