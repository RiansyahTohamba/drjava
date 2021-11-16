/* 
	sudah REFACTORING dan dikopi
  
*/
class AbstractConsoleController{
 /** Sets up the view. */
 // sudah beres


 protected void _setupView() {
    _pane.addActionForKeyStroke(getSetting(OptionConstants.KEY_BEGIN_LINE), gotoPromptPosAction);
    _pane.addActionForKeyStroke(getSetting(OptionConstants.KEY_BEGIN_LINE_SELECT), selectToPromptPosAction);
    _pane.addActionForKeyStroke(getSetting(OptionConstants.KEY_END_LINE), gotoEndAction);
    _pane.addActionForKeyStroke(getSetting(OptionConstants.KEY_END_LINE_SELECT), selectToEndAction);

    getConfig().addOptionListener(OptionConstants.KEY_BEGIN_LINE, new OptionListener<Vector<KeyStroke>>() {
      public void optionChanged(OptionEvent<Vector<KeyStroke>> oe) {
        _pane.addActionForKeyStroke(oe.value, gotoPromptPosAction);
      }
    });
    getConfig().addOptionListener(OptionConstants.KEY_BEGIN_LINE_SELECT, new OptionListener<Vector<KeyStroke>>() {
      public void optionChanged(OptionEvent<Vector<KeyStroke>> oe) {
        _pane.addActionForKeyStroke(oe.value, selectToPromptPosAction);
     }
    });
    getConfig().addOptionListener(OptionConstants.KEY_END_LINE, new OptionListener<Vector<KeyStroke>>() {
      public void optionChanged(OptionEvent<Vector<KeyStroke>> oe) {
        _pane.addActionForKeyStroke(oe.value, gotoEndAction);
     }
    });
    getConfig().addOptionListener(OptionConstants.KEY_END_LINE_SELECT, new OptionListener<Vector<KeyStroke>>() {
      public void optionChanged(OptionEvent<Vector<KeyStroke>> oe) {
        _pane.addActionForKeyStroke(oe.value, selectToEndAction);
     }
    });
    
    _pane.addActionForKeyStroke(getSetting(OptionConstants.KEY_CUT), cutAction);
    _pane.addActionForKeyStroke(getSetting(OptionConstants.KEY_COPY), copyAction);
    getConfig().addOptionListener(OptionConstants.KEY_CUT, new OptionListener<Vector<KeyStroke>>() {
      public void optionChanged(OptionEvent<Vector<KeyStroke>> oe) {
        _pane.addActionForKeyStroke(getSetting(OptionConstants.KEY_CUT), cutAction);
     }
    });
    getConfig().addOptionListener(OptionConstants.KEY_COPY, new OptionListener<Vector<KeyStroke>>() {
      public void optionChanged(OptionEvent<Vector<KeyStroke>> oe) {
        _pane.addActionForKeyStroke(getSetting(OptionConstants.KEY_COPY), copyAction);
     }
    });
  }

}