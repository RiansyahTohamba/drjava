MainFrame docs

setUpDrJavaProperties is `the last method` to be refactoring.
nanti dikumpulkan lagi data-data terkait extract method refactoring nya

generate dch drjava lagi!

lalu masukin ke slide!

coba tambah project!

# setUpDrJavaProperties 
sebaiknya dipisah saja
karena sudah punya 10 method
lalu tak ada properti mainframe yg dibutuhkan 

## siapa saja yang memakai setUpDrJavaProperties?
hanya satu function yang pakai, yaitu constructor MainFrame()
wkwk


# kelas MainFrameProperties membutuhkan properti MainFrame apa saja?
= private volatile AbstractGlobalModel _model; (sudah)
= MainFrame.this

properti  ini milik objFrame
= Cannot resolve symbol '_posListener'
= Cannot resolve method 'open(edu.rice.cs.util.FileOpenSelector)'
	open berasal dari kode 
	public void open(FileOpenSelector openSelector) {
= Cannot resolve method '_saveAll()'
= Cannot resolve method '_compileAll()'
= Cannot resolve method '_jumpToLine(int)'


# Berapa jumlah nested class dalam class MainFrame?
	21

## there is list of class nya
	DJFileDisplayManager
	OddDisplayManager
	MenuBar
	PositionListener
	GlassPane
	UIDebugListener
	DJAsyncTaskLauncher
	ModelListener
	MainFontOptionListener
	LineNumbersFontOptionListener
	DoclistFontOptionListener
	ToolBarFontOptionListener
	MenuBarFontOptionListener
	NormalColorOptionListener
	BackgroundColorOptionListener
	ToolBarOptionListener
	LineEnumOptionListener
	LineEnumColorOptionListener
	QuitPromptOptionListener
	RecentFilesOptionListener
	LastFocusListener

# wow, bgmn cara pecahinnya?
= dipecahkan berdasarkan apa?
	berdasarkan class yg di deteksi DCH, berikut list nya:
	mainframe/_createDefScrollPane.java
	mainframe/reloadPanel.java
	mainframe/_storePositionInfo.java
	mainframe/_setUpTabs.java
	mainframe/initGotoFileDialog.java
	mainframe/setUpDrJavaProperties.java
	``
	# reloadpanel dan `_createDefScrollPane`
	reloadpanel berkaitan dengan `_createDefScrollPane`
	jadi bisa dibuat 1 class

	# selanjutnya _storePositionInfo dengan siapa?

	saya buat class PositionInfo first, nanti bisa pakai fasilitas move-method
	lakukan saja dulu move method
	nanti menyusul siapa yg butuh.

	## ini yg butuh `_storePositionInfo`
	_updateSavedConfiguration depend on _storePositionInfo
	xxx depend on _updateSavedConfiguration 


# storePositionInfo cukup di extract method saja dulu
belum di move method di class sendiri
biar lebih cepat

# method apa lagi? 
	mainframe/_setUpTabs.java
	mainframe/initGotoFileDialog.java
	mainframe/setUpDrJavaProperties.java


## _setUpTabs.java


## initGotoFileDialog.java

## setUpDrJavaProperties.java


