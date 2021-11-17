class MainFrame{
/*
refactoring log:
 5x extract method:
 getInfoSupplier, getInfoSupplier, getCancelAction, getStrategies
 setGotoFileDialog
 */
  /** Initialize dialog if necessary. */
  void initGotoFileDialog() {
    if (_gotoFileDialog == null) {
      PredictiveInputFrame.InfoSupplier<GoToFileListEntry> info = getInfoSupplier();
      PredictiveInputFrame.CloseAction<GoToFileListEntry> okAction = getInfoSupplier();
      PredictiveInputFrame.CloseAction<GoToFileListEntry> cancelAction = getCancelAction();
      ArrayList<PredictiveInputModel.MatchingStrategy<GoToFileListEntry>> strategies = getStrategies();

      List<PredictiveInputFrame.CloseAction<GoToFileListEntry>> actions
        = new ArrayList<PredictiveInputFrame.CloseAction<GoToFileListEntry>>();

      actions.add(okAction);
      actions.add(cancelAction);

      setGotoFileDialog(info, strategies, actions);
      // putting one dummy entry in the list; it will be changed later anyway
      if (DrJava.getConfig().getSetting(DIALOG_GOTOFILE_STORE_POSITION).booleanValue()) {
        _gotoFileDialog.setFrameState(DrJava.getConfig().getSetting(DIALOG_GOTOFILE_STATE));
      }

    }
  }

  private void setGotoFileDialog(PredictiveInputFrame.InfoSupplier<GoToFileListEntry> info, ArrayList<PredictiveInputModel.MatchingStrategy<GoToFileListEntry>> strategies, List<PredictiveInputFrame.CloseAction<GoToFileListEntry>> actions) {
    boolean isForce = true;
    boolean isIgnoreCase = true;
    int cancel = 1; // cancel is action 1

    _gotoFileDialog = new PredictiveInputFrame<GoToFileListEntry>(MainFrame.this,
                                                  "Go to File", isForce, isIgnoreCase,
            info, strategies, actions, cancel,
                                                  new GoToFileListEntry(null, "dummyGoto"))
    {
      public void setOwnerEnabled(boolean b) {
        if (b) hourglassOff();
        else hourglassOn();
      }
    };
  }

  private ArrayList<PredictiveInputModel.MatchingStrategy<GoToFileListEntry>> getStrategies() {
    ArrayList<PredictiveInputModel.MatchingStrategy<GoToFileListEntry>> strategies =
      new ArrayList<PredictiveInputModel.MatchingStrategy<GoToFileListEntry>>();

    strategies.add(new PredictiveInputModel.FragmentLineNumStrategy<GoToFileListEntry>());
    strategies.add(new PredictiveInputModel.PrefixLineNumStrategy<GoToFileListEntry>());
    strategies.add(new PredictiveInputModel.RegExLineNumStrategy<GoToFileListEntry>());
    return strategies;
  }

  private PredictiveInputFrame.CloseAction<GoToFileListEntry> getCancelAction() {
    PredictiveInputFrame.CloseAction<GoToFileListEntry> cancelAction =
      new PredictiveInputFrame.CloseAction<GoToFileListEntry>() {
      public String getName() { return "Cancel"; }
      public KeyStroke getKeyStroke() { return KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0); }
      public String getToolTipText() { return null; }
      public Object value(PredictiveInputFrame<GoToFileListEntry> p) {
        hourglassOff();
        return null;
      }
    };
    return cancelAction;
  }

  private PredictiveInputFrame.CloseAction<GoToFileListEntry> getOkAction() {
    PredictiveInputFrame.CloseAction<GoToFileListEntry> okAction =
      new PredictiveInputFrame.CloseAction<GoToFileListEntry>() {
      public String getName() { return "OK"; }
      public KeyStroke getKeyStroke() { return KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0); }
      public String getToolTipText() { return null; }
      public Object value(PredictiveInputFrame<GoToFileListEntry> p) {
        if (p.getItem() != null) {
          final OpenDefinitionsDocument newDoc = p.getItem().getOpenDefinitionsDocument();
          if (newDoc != null) {
            final boolean docChanged = ! newDoc.equals(_model.getActiveDocument());
            final boolean docSwitch = _model.getActiveDocument() != newDoc;
            if (docSwitch) _model.setActiveDocument(newDoc);
            final int curLine = newDoc.getCurrentLine();
            final String t = p.getText();
            final int last = t.lastIndexOf(':');
            if (last >= 0) {
              try {
                String end = t.substring(last + 1);
                int val = Integer.parseInt(end);

                final int lineNum = Math.max(1, val);
                Runnable command = new Runnable() {
                  public void run() {
                    try { _jumpToLine(lineNum); }  // adds this region to browser history
                    catch (RuntimeException e) { _jumpToLine(curLine); }
                  }
                };
                if (docSwitch) {
                  // postpone running command until after document switch, which is pending in the event queue
                  EventQueue.invokeLater(command);
                }
                else command.run();
              }
              catch(RuntimeException e) { /* ignore */ }
            }
            else if (docChanged) {
              // defer executing this code until after active document switch (if any) is complete
              addToBrowserHistory();
            }
          }
        }
        hourglassOff();
        return null;
      }
    };
    return okAction;
  }

  private PredictiveInputFrame.InfoSupplier<GoToFileListEntry> getInfoSupplier() {
    PredictiveInputFrame.InfoSupplier<GoToFileListEntry> info =
      new PredictiveInputFrame.InfoSupplier<GoToFileListEntry>() {
        public String value(GoToFileListEntry entry) {
        final StringBuilder sb = new StringBuilder();
        final OpenDefinitionsDocument doc = entry.getOpenDefinitionsDocument();
        if (doc != null) {
          try {
            try { sb.append(FileOps.stringMakeRelativeTo(doc.getRawFile(), doc.getSourceRoot())); }
            catch(IOException e) { sb.append(doc.getFile()); }
          }
          catch(FileMovedException e) { sb.append(entry + " was moved"); }
          catch(InvalidPackageException e) { sb.append(entry); }
        }
        else sb.append(entry);
        return sb.toString();
      }
      };
    return info;
  }
}
