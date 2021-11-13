makeCommandPane.md
# info dasar
ExecuteExternalDialog.makeCommandPane
692 - 329 = 360 LOC
cari regex nya

# berapa jumlah DCO?
DCO diwakili oleh object.fungsi()
\.\w+\(
160 match, makanya DCH nya tinggi

# panel diproses seperti apa?
untuk mengetahui jawabannya, kita perlu melihat occurence nya

## berapa jumlah statement yg melibatkan object panel ?
dicari dengan \bpanel\b
hanya 5 matches? wkwk
	2 nya sudah di makeCommandPane
	1 nya just commentar
	2 nya dimana?

## apa statement yg melibatkan 3 variabel panel?
`
main darimana?
panel.add(main, BorderLayout.CENTER);
bottom darimana?
panel.add(bottom, BorderLayout.SOUTH);
`
hanya itu saja, penambahan something pada panel.

## bottom darimana?
sudah di-extract menjadi setBottom()

## main darimana?
cari lewat regex \bmain\b
ketemu di ngapain1()
	18 matches 

	ngapain1(main) ?
	atau 
	getMain() ?


## ada sekian kali proses main
main.setLayout(gridbag);
main.add(commandLineLabel);
main.add(commandLineSP);
main.add(_commandLinePreviewLabel);
main.add(commandLinePreviewSP);
main.add(workDirLabel);
main.add(commandWorkDirLineSP);
main.add(_commandWorkDirBtn);
main.add(commandWorkDirLinePreviewLabel);
main.add(commandWorkDirLinePreviewSP);
main.add(enclosingFileLabel);
main.add(commandEnclosingFileLineSP);
main.add(_commandEnclosingFileBtn);
main.add(commandEnclosingFileLinePreviewLabel);
main.add(commandEnclosingFileLinePreviewSP);
