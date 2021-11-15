package sudah;/*
BELUM REFACTORING

sudah.ErrorPanel.ErrorListPane.ErrorListPane()
constructor dengan DCH 10
417-343


*/

class ErrorPanel{
	class ErrorListPane{
		//      // If we set this pane to be of type text/rtf, it wraps based on words
//      // as opposed to based on characters.
		// 3 extract method: addOptionListener, setColors, getConfig

		public ErrorListPane() {
	      setContentType("text/rtf");
	      setDocument(new ErrorDocument(getErrorDocumentTitle()));
	      setHighlighter(new ReverseHighlighter());
	      
	      addMouseListener(defaultMouseListener);
	      
	      _selectedIndex = 0;
	      _errorListPositions = new Position[0];
	        
	      this.setFont(new Font("Courier", 0, 20));
	      setEditable(false);
	      
	      getConfig().addOptionListener(COMPILER_ERROR_COLOR, new CompilerErrorColorOptionListener());
	      
	      // Set the colors.
			setColors();

			// Add OptionListeners for the colors.
	      getConfig().addOptionListener(DEFINITIONS_NORMAL_COLOR, new ForegroundColorListener());
	      getConfig().addOptionListener(DEFINITIONS_BACKGROUND_COLOR, new BackgroundColorListener());
	      
	      /* Item listener instead of change listener so that this code won't be called (twice) every time the mouse moves
	       * over the _showHighlightsCheckBox (5/26/05)
	       */
	      _showHighlightsCheckBox.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent e) {
	          DefinitionsPane lastDefPane = _frame.getCurrentDefPane();
	          
	          if (e.getStateChange() == ItemEvent.DESELECTED) {
	            lastDefPane.removeErrorHighlight();
	          } else if (e.getStateChange() == ItemEvent.SELECTED) {   
	            getErrorListPane().switchToError(getSelectedIndex());
	          }
	        }
	      });
	      
	      _keymap = addKeymap("ERRORLIST_KEYMAP", getKeymap());
	      
	      addActionForKeyStroke(getConfig().getSetting(OptionConstants.KEY_CUT), cutAction);
	      addActionForKeyStroke(getConfig().getSetting(OptionConstants.KEY_COPY), copyAction);
	      addActionForKeyStroke(getConfig().getSetting(OptionConstants.KEY_PASTE_FROM_HISTORY), pasteAction);

			addOptionListener();
		}
		private void addOptionListener() {
			getConfig().addOptionListener(OptionConstants.KEY_CUT, new OptionListener<Vector<KeyStroke>>() {
			  public void optionChanged(OptionEvent<Vector<KeyStroke>> oe) {
				addActionForKeyStroke(getConfig().getSetting(OptionConstants.KEY_CUT), cutAction);
			  }
			});
			getConfig().addOptionListener(OptionConstants.KEY_COPY, new OptionListener<Vector<KeyStroke>>() {
			  public void optionChanged(OptionEvent<Vector<KeyStroke>> oe) {
				addActionForKeyStroke(getConfig().getSetting(OptionConstants.KEY_COPY), copyAction);
			  }
			});
			getConfig().addOptionListener(OptionConstants.KEY_PASTE_FROM_HISTORY, new OptionListener<Vector<KeyStroke>>() {
			  public void optionChanged(OptionEvent<Vector<KeyStroke>> oe) {
				addActionForKeyStroke(getConfig().getSetting(OptionConstants.KEY_PASTE_FROM_HISTORY), pasteAction);
			  }
			});
		}
		private void setColors() {
			StyleConstants.setForeground(NORMAL_ATTRIBUTES, getConfig().getSetting(DEFINITIONS_NORMAL_COLOR));
			StyleConstants.setForeground(BOLD_ATTRIBUTES, getConfig().getSetting(DEFINITIONS_NORMAL_COLOR));
			setBackground(getConfig().getSetting(DEFINITIONS_BACKGROUND_COLOR));
		}
		private FileConfiguration getConfig(){
	      return DrJssava.getConfig();
	  	}
	}

  
}