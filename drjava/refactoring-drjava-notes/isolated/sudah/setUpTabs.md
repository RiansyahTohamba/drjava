setUpTabs.md

# hasil akhir refactoring
4x extract method
## rinciannya:
private void _setUpTabs()
	HighlightInfo getHighlightInfo(Breakpoint bp, DefinitionsPane bpPane)
	private void addLastTabs()
	private void setTabbedPane()
	private void setFocusListener()





# sebelum refactoring 
`public void regionAdded(final Breakpoint bp) {
  DefinitionsPane bpPane = getDefPaneGivenODD(bp.getDocument());
  _documentBreakpointHighlights.put(apanih);
  
  apanih = bp, bpPane.getHighlightManager().addHighlight(
  					bp.getStartOffset(), bp.getEndOffset(), 
                    bp.isEnabled() ? DefinitionsPane.BREAKPOINT_PAINTER : DefinitionsPane.DISABLED_BREAKPOINT_PAINTER)


  _updateDebugStatus();
}
`

## apa tipenya _documentBreakpointHighlights?
`
_documentBreakpointHighlights punya tipe IdentityHashMap<Breakpoint, HighlightManager.HighlightInfo>
`

## berapa jumlah params nya?
extract pakai apa?


# apa tipenya `addHighlight` ?
bp, 
bpPane.getHighlightManager().addHighlight(
    bp.getStartOffset(), 
    bp.getEndOffset(), 
    bp.isEnabled() ? 
        DefinitionsPane.BREAKPOINT_PAINTER : 
        DefinitionsPane.DISABLED_BREAKPOINT_PAINTER
)

-----
HighlightManager

-----
par1 = bp
par2 = bpPane.getHighlightManager().addHighlight(
    bp.getStartOffset(), 
    bp.getEndOffset(), 
    bp.isEnabled() ? 
        DefinitionsPane.BREAKPOINT_PAINTER : 
        DefinitionsPane.DISABLED_BREAKPOINT_PAINTER
)



# apakah setelah di refactoring seperti ini?

# bpPane tipenya apa?
