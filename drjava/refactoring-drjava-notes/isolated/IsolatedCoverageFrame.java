class IsolatedCoverageFrame{
	// DCH highlight=10
//  CINT = ?

 private void highlight(Map<String, List<String>> lineColors, boolean selOnly) {
    
        /* Get an iterator over the documents to be highlighted */
        Iterator<OpenDefinitionsDocument> iter;
        if (!selOnly) {
            iter = _model.getDocumentNavigator().getDocuments().
                iterator();
        } else {
            iter = _model.getDocumentNavigator().getSelectedDocuments().
                iterator();
        }

        while (iter.hasNext()) {

            /* Get the file to highlight */
            OpenDefinitionsDocument o = iter.next(); 
            final DefinitionsPane pane = _mainFrame.getDefPaneGivenODD(o);

            try {
                List<String> colors = lineColors.get(o.getQualifiedClassName());

                /* Highlight each line */
                for (int i = 0; i < colors.size(); i++) {
                    String color = colors.get(i);
              
                    Color c = Color.black;
                    if (color.equals("")) {
                        continue;
                    } else if (color.equals("green")) {
                        c = Color.green;
                    } else if (color.equals("red")) {
                        c = Color.red;
                    } else if (color.equals("yellow")) {
                        c = Color.yellow;
                    }
      
                    final HighlightInfo info = pane.getHighlightManager().
                        addHighlight(o._getOffset(i), o._getOffset(i+1), 
                        new ReverseHighlighter.DrJavaHighlightPainter(c));

                    CompilerListener removeHighlight = new DummyCompilerListener() {

                        @Override public void compileAborted(Exception e) {
                            /**
                             * Gets called if there are modified files and the 
                             * user chooses NOT to save the files see bug 
                             * report 2582488: Hangs If Testing Modified File, 
                             * But Choose "No" for Saving
                             */
                            final CompilerListener listenerThis = this;
                            _model.getCompilerModel().removeListener(listenerThis); 
                        }

                        @Override public void compileEnded(File workDir, 
                            List<? extends File> excludedFiles) {

                            final CompilerListener listenerThis = this;

                            try {

                                if (_model.hasOutOfSyncDocuments() || _model.
                                    getNumCompilerErrors() > 0) {
                                    return;
                                }

                                EventQueue.invokeLater(new Runnable() {  
                                    /**
                                     * Defer running this code; would prefer
                                     * to waitForInterpreter.
                                     */
                                    public void run() {
                                        pane.getHighlightManager().
                                        removeHighlight(info);
                                    }
                                });
                            }

                            finally {

                                /* Remove listener after its first execution */
                                EventQueue.invokeLater(new Runnable() { 
                                    public void run() { 
                                        _model.getCompilerModel().
                                        removeListener(listenerThis); 
                                    }
                                });
                            }
                        }
                    }; /* end coverage listener */

                    _model.getCompilerModel().addListener(removeHighlight);
                }

            } catch (ClassNameNotFoundException e) {
                continue;
            }
        }
    }

}

