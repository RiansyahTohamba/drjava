AutoCompletePopup::createCompleteWordDialog.md

# berapa total refactoring?
change to 4x extract method on AutoCompletePopup::createCompleteWordDialog

## berikut detailnya
createCompleteWordDialog() depend on:
    completeCheckBox()
    getInfo()
    getOkAction()
    getCancelAct()


## function_variable 
dialogThunk

## instance_variable
OptionConstants    


## _completeJavaAPICheckbox

# berapa jumlah lambda pada kelas AutoCompletePopup ?

1. new PredictiveInputFrame<AutoCompletePopupEntry>(params){}
2. new PredictiveInputFrame.InfoSupplier<AutoCompletePopupEntry>(){ String value() { statements} }
3. new PredictiveInputFrame.CloseAction<AutoCompletePopupEntry>()



1. new PredictiveInputFrame<AutoCompletePopupEntry>(params){}
`dialogThunk.set(
        new PredictiveInputFrame<AutoCompletePopupEntry>(null,title,true,true,info,strategies, actions, actions.size()-1, entry) 
    {
        protected JComponent[] makeOptions() {
          return new JComponent[] { _completeJavaAPICheckbox };
        }
    });
`
nama class panjang sekali
paramaternya juga banyak
wkwk

x = new PredictiveInputFrame<AutoCompletePopupEntry>(null,title,true,true,info,strategies, actions, actions.size()-1, entry)

disingkat lagi jadi
x = new Class<A>(params){}    



dikonversi jadi lebih singkat
dialogThunk.set(x);

