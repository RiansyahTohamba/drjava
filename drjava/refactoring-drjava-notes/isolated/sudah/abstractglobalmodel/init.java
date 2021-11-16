class IsolatedInit{
  // CINT = 31 matches
  // cek saja sudah berapa todo untuk di extract?
  
  /* 
  didalam init._gainVisitor terdapat
  1. itemcase
  2. filecase
  3. stringcase
  bagaimana caranya _gainVisitor ini digunakan?
    digunakan oleh dat, didalam nested function _documentNavigator.addNavigationListener
    dat.execute(_gainVisitor, modelInitiated); 

  */
  /*

	sesuai namanya, init memang inisasi
  dan inisiasi itu memang ditoleransi DCH nya
	sudah tidak ada lagi yang bisa di optimize
  */
  private void _init() {
    /** This visitor is invoked by the DocumentNavigator to update _activeDocument among other things */
    final NodeDataVisitor<OpenDefinitionsDocument, Boolean>  _gainVisitor = 
      new NodeDataVisitor<OpenDefinitionsDocument, Boolean>() {

      public Boolean itemCase(OpenDefinitionsDocument doc, Object... p) {
        _setActiveDoc(doc);  // sets _activeDocument, the shadow copy of the active document   
        final File oldDir = _activeDirectory;  // _activeDirectory can be null
        final File dir = doc.getParentDirectory();  // dir can be null
        if (dir != null && ! dir.equals(oldDir)) {
          _activeDirectory = dir;
          _notifier.currentDirectoryChanged(_activeDirectory);
        }
        return Boolean.valueOf(true);
      }

      public Boolean fileCase(File f, Object... p) {
        if (! f.isAbsolute()) { // should never happen because all file names are canonicalized
          File root = _state.getProjectFile().getParentFile().getAbsoluteFile();
          f = new File(root, f.getPath());
        }
        _activeDirectory = f;  // Invariant: activeDirectory != null
        _notifier.currentDirectoryChanged(f);
        return Boolean.valueOf(true);
      }
      public Boolean stringCase(String s, Object... p) { return Boolean.valueOf(false); }

    };
    
    /** Listener that invokes the _gainVisitor when a selection is made in the document navigator. */
    _documentNavigator.addNavigationListener(new INavigationListener<OpenDefinitionsDocument>() {
      public void gainedSelection(NodeData<? extends OpenDefinitionsDocument> dat, boolean modelInitiated) {
        dat.execute(_gainVisitor, modelInitiated); 
      }
      public void lostSelection(NodeData<? extends OpenDefinitionsDocument> dat, boolean modelInitiated) {
      /* not important, only one document selected at a time */ }
    });
    
    // The document navigator gets the focus in 
    _documentNavigator.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        Utilities.invokeLater(new Runnable() { public void run() { _notifier.focusOnDefinitionsPane(); } });
      }
      public void focusLost(FocusEvent e) { }
    });
    
    _ensureNotEmpty();
    setActiveFirstDocument();
    
    // setup option listener for clipboard history
    OptionListener<Integer> clipboardHistorySizeListener = new OptionListener<Integer>() {
      public void optionChanged(OptionEvent<Integer> oce) {
        ClipboardHistoryModel.singleton().resize(oce.value);
      }
    };

    DrJava.getConfig().addOptionListener(CLIPBOARD_HISTORY_SIZE, clipboardHistorySizeListener);
    ClipboardHistoryModel.singleton().resize(DrJava.getConfig().getSetting(CLIPBOARD_HISTORY_SIZE).intValue());
    
    // setup option listener for browser history
    OptionListener<Integer> browserHistoryMaxSizeListener = new OptionListener<Integer>() {
      public void optionChanged(OptionEvent<Integer> oce) {
        getBrowserHistoryManager().setMaximumSize(oce.value);
      }
    };

    DrJava.getConfig().addOptionListener(BROWSER_HISTORY_MAX_SIZE, browserHistoryMaxSizeListener);
    // extract getMaximumSize()
    int maximumSize = getMaximumSize();
    getBrowserHistoryManager().setMaximumSize(maximumSize);
  }
}