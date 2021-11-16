/* 
  	BELUM REFACTORING
	cek regex nya
*/
class MainFrame{
	private void _setUpTabs() {
	    _updateMenuBars();
	    // Interactions
	    _interactionsController.setPrevPaneAction(_switchToPreviousPaneAction);
	    _interactionsController.setNextPaneAction(_switchToNextPaneAction);

	    JScrollPane interactionsScroll =
	      new BorderlessScrollPane(_interactionsPane,
				  ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
	              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	    _interactionsContainer.add(interactionsScroll, BorderLayout.CENTER);
	    
	    if (_showDebugger) { statementShowDebugger }
	    
	    // hook highlighting listener to bookmark manager
	    _model.getBookmarkManager().addListener(listener);
	    
	    _tabbedPane.addChangeListener(new ChangeListener () {
	      /* Only runs in the event thread. */
	      public void stateChanged(ChangeEvent e) {
	        clearStatusMessage();
	        
	        if (_tabbedPane.getSelectedIndex() == INTERACTIONS_TAB) {
	          // Use EventQueue because this action must execute AFTER all pending events in the event queue
	          _interactionsContainer.setVisible(true);  // kluge to overcome subtle focus bug
	          EventQueue.invokeLater(new Runnable() {  
	            public void run() { _interactionsContainer.requestFocusInWindow(); }  
	          });
	        }
	        else if (_tabbedPane.getSelectedIndex() == CONSOLE_TAB) {
	          // Use EventQueue because this action must execute AFTER all pending events in the event queue
	          EventQueue.invokeLater(new Runnable() { public void run() { _consoleScroll.requestFocusInWindow(); } });
	        }
	        // Update error highlights?
	        if (_currentDefPane != null) {
	          int pos = _currentDefPane.getCaretPosition();
	          _currentDefPane.removeErrorHighlight(); // removes highlighting whenever the current tabbed pane is switched
	          _currentDefPane.getErrorCaretListener().updateHighlight(pos);
	        }
	      }
	    });
	    
	    _tabbedPane.add("Interactions", _interactionsContainer);
	    _tabbedPane.add("Console", _consoleScroll);
	    
	    _interactionsPane.addKeyListener(_historyListener);
	    _interactionsPane.addFocusListener(_focusListenerForRecentDocs);
	    _interactionsController.addFocusListener(new FocusAdapter() {
	      public void focusGained(FocusEvent e){ 
	        _undoAction.setDelegatee(_interactionsController.getUndoAction());
	        _redoAction.setDelegatee(_interactionsController.getRedoAction());  
	      }
	    });
	    
	    _consoleScroll.addKeyListener(_historyListener);
	    _consoleScroll.addFocusListener(_focusListenerForRecentDocs);
	    
	    
	    _tabs.addLast(_compilerErrorPanel);
	    _tabs.addLast(_junitPanel);
	    _tabs.addLast(_javadocErrorPanel);
	    _tabs.addLast(_findReplace);
	    if (_showDebugger) { _tabs.addLast(_breakpointsPanel); }
	    _tabs.addLast(_bookmarksPanel);
	    
	    _interactionsContainer.addFocusListener(new FocusAdapter() {
	      public void focusGained(FocusEvent e) { 
	        EventQueue.invokeLater(new Runnable() { 
	          public void run() {
	            _interactionsPane.requestFocusInWindow(); 
	          }
	        });
	      }
	    });
	    
	    _interactionsPane.addFocusListener(new FocusAdapter() {
	      public void focusGained(FocusEvent e) { _lastFocusOwner = _interactionsContainer; }
	    });

	    _consolePane.addFocusListener(new FocusAdapter() {
	      public void focusGained(FocusEvent e) { _lastFocusOwner = _consoleScroll; }
	    });

	    _compilerErrorPanel.getMainPanel().addFocusListener(new FocusAdapter() {
	      public void focusGained(FocusEvent e) { _lastFocusOwner = _compilerErrorPanel; }
	    });
	    
	    _junitPanel.getMainPanel().addFocusListener(new FocusAdapter() {
	      public void focusGained(FocusEvent e) { _lastFocusOwner = _junitPanel; }
	    });
	    
	    _javadocErrorPanel.getMainPanel().addFocusListener(new FocusAdapter() {
	      public void focusGained(FocusEvent e) { _lastFocusOwner = _javadocErrorPanel; }
	    });
	    
	    _findReplace.getFindField().addFocusListener(new FocusAdapter() {
	      public void focusGained(FocusEvent e) { _lastFocusOwner = _findReplace; }
	    });
	    
	    if (_showDebugger) {
	      _breakpointsPanel.getMainPanel().addFocusListener(new FocusAdapter() {
	        public void focusGained(FocusEvent e) { _lastFocusOwner = _breakpointsPanel; }
	      });
	    }

	    _bookmarksPanel.getMainPanel().addFocusListener(new FocusAdapter() { 
	      public void focusGained(FocusEvent e) { _lastFocusOwner = _bookmarksPanel; }
	    });
	}
}
