protected PredictiveInputFrame<AutoCompletePopupEntry>
    createCompleteWordDialog(final String title,
                             final int start,
                             final int loc,
                             final SizedIterable<String> actionNames,
                             final SizedIterable<KeyStroke> actionKeyStrokes,
                             final Runnable canceledAction,
                             final SizedIterable<Runnable3<AutoCompletePopupEntry,Integer,Integer>> acceptedActions) {
    final SimpleBox<PredictiveInputFrame<AutoCompletePopupEntry>> dialogThunk =
      new SimpleBox<PredictiveInputFrame<AutoCompletePopupEntry>>();
    // checkbox whether Java API classes should be completed as well
    _completeJavaAPICheckbox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String curMask = dialogThunk.value().getMask();
        DrJava.getConfig().setSetting(OptionConstants.DIALOG_COMPLETE_JAVAAPI, _completeJavaAPICheckbox.isSelected());
        if (_completeJavaAPICheckbox.isSelected()) addJavaAPI(); else removeJavaAPI();
        dialogThunk.value().setItems(true,_allEntries);
        dialogThunk.value().setMask(curMask);
        dialogThunk.value().resetFocus();
      }
    });
    PlatformFactory.ONLY.setMnemonic(_completeJavaAPICheckbox,'j');
    PredictiveInputFrame.InfoSupplier<AutoCompletePopupEntry> info = 
      new PredictiveInputFrame.InfoSupplier<AutoCompletePopupEntry>() {
      public String value(AutoCompletePopupEntry entry) {
        // show full class name as information
        StringBuilder sb = new StringBuilder();
        sb.append(entry.getFullPackage());
        sb.append(entry.getClassName());
        return sb.toString();
      }
    };
    
    List<PredictiveInputFrame.CloseAction<AutoCompletePopupEntry>> actions
      = new ArrayList<PredictiveInputFrame.CloseAction<AutoCompletePopupEntry>>();

    Iterator<String> nameIt = actionNames.iterator();
    Iterator<Runnable3<AutoCompletePopupEntry,Integer,Integer>> actionIt =
      acceptedActions.iterator();
    Iterator<KeyStroke> ksIt = actionKeyStrokes.iterator();
    
    for(int i = 0; i<acceptedActions.size(); ++i) {
      final String name = nameIt.next();
      final Runnable3<AutoCompletePopupEntry,Integer,Integer> runnable = actionIt.next();
      final KeyStroke ks = ksIt.next();
      
      PredictiveInputFrame.CloseAction<AutoCompletePopupEntry> okAction =
        new PredictiveInputFrame.CloseAction<AutoCompletePopupEntry>() {
        public String getName() { return name; }
        public KeyStroke getKeyStroke() { return ks; }
        public String getToolTipText() { return "Complete the identifier"; }
        public Object value(final PredictiveInputFrame<AutoCompletePopupEntry> p) {
          _lastState = p.getFrameState();
          if (p.getItem() != null) {
            Utilities.invokeAndWait(new Runnable() {
              public void run() {
                runnable.run(p.getItem(), start, loc);
              }
            });
          }
          else {
            Utilities.invokeAndWait(canceledAction);
          }
          return null;
        }
      };
      actions.add(okAction);
    }

    PredictiveInputFrame.CloseAction<AutoCompletePopupEntry> cancelAction = 
      new PredictiveInputFrame.CloseAction<AutoCompletePopupEntry>() {
      public String getName() { return "Cancel"; }
      public KeyStroke getKeyStroke() { return KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0); }
      public String getToolTipText() { return null; }
      public Object value(PredictiveInputFrame<AutoCompletePopupEntry> p) {
        _lastState = p.getFrameState();
        Utilities.invokeAndWait(canceledAction);
        return null;
      }
    };    
    actions.add(cancelAction);

    // Note: PredictiveInputModel.* is statically imported
    java.util.ArrayList<MatchingStrategy<AutoCompletePopupEntry>> strategies =
      new java.util.ArrayList<MatchingStrategy<AutoCompletePopupEntry>>();
    strategies.add(new FragmentStrategy<AutoCompletePopupEntry>());
    strategies.add(new PrefixStrategy<AutoCompletePopupEntry>());
    strategies.add(new RegExStrategy<AutoCompletePopupEntry>());
    
    GoToFileListEntry entry = new GoToFileListEntry(new DummyOpenDefDoc() {
      public String getPackageNameFromDocument() { return ""; }
    }, "dummyComplete");
    dialogThunk.set(new PredictiveInputFrame<AutoCompletePopupEntry>(null,
                                                                     title,
                                                                     true, // force
                                                                     true, // ignore case
                                                                     info,
                                                                     strategies,
                                                                     actions,
                                                                     actions.size()-1, // cancel is last
                                                                     entry) {
      protected JComponent[] makeOptions() {
        return new JComponent[] { _completeJavaAPICheckbox };
      }
    });
    dialogThunk.value().setSize(dialogThunk.value().getSize().width, 500);
    dialogThunk.value().setLocationRelativeTo(_mainFrame);
    return dialogThunk.value();
  }