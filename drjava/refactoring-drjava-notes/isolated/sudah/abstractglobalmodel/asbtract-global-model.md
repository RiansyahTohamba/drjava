# start - mulai build proses serangan! 

klo init-init biasanya hanya berisi 'new Class()'
ada juga init yang tidak pakai new Class()

# mulai dari AbstractGlobalModel

AbstractGlobalModel
_init=10
_locateClassFile=7.75
addToBrowserHistory=6.625


## addToBrowserHistory() ?

## _locateClassFile() ?


## _init() - tidak terdeteksi jdeodorant
hanya dipakai di local class, makanya privat

length = 366-297 = 69 LOC
===== 297
_gainVisitor = 299 - 325
_documentNavigator.addNavigationListener = 328 - 333
_documentNavigator.addFocusListener = 336 - 343

browserHistoryMaxSizeListener = 359 - 363
setMaximumSize
===== 366
saya sudah extract satu method 'maximumSize()'

 
didalam init._gainVisitor terdapat
1. itemcase
2. filecase
3. stringcase



# tahapan experiment
## gunakan JDeodorant untuk memberikan rekomendasi refactoring drjava!
	buka dch-jcodeodor/drjava-tahap-awal.xls
		ambil file biji
	create-branch `exp-tahap-1`

## after-refactoring generate ulang .sqlite nya
	beri nama:
	2-tahap-1-drjava.sqlite

# (sudah) fun-fact: false positive detected class
	test function
	38 fungsi berjenis test
	cek saja sheet 
	nanti di refactor, pindahkan saja ke folder test
	pindahkan saja dulu, agar pas di-test jadi tdk include lagi project test nya! 
	sudah 
	undo move testClass which saved in production-code (src). 
	It turn out testClases used in the production code.
	testclass nya bisa dianggap production code juga
	karena memang dipakai oleh class prod lain wkwkw.	
	nanti didalami lagi apakah diikutkan dalam data atau tidak 

# hasil deteksi JDeodorant
dari 3 method ini, ga ada yg terdeteksi jdeodorant sbg smell feature-envy, god-class, maupun long method.
ini menujukkan DCH tidak terkait dengan LongMethod.
Sebelumnya sy pikir, DCH dan LongMethod berkorelasi kuat.
ternyata tidak!
dari parsing nya, DCH memperhitungkan jumlah '.'
jumlah titik ini menujukkan call yg dilakukan suatu method.


# 2 kendala intellijdeodorant tdk bisa digunakan
1. build selalu gagal: java: error: invalid source release: 16
   jadi 2 menu di intellij harus disesuaikan
2. interface JUnitModel tidak dapat digunakan
   jangan buka semua direktori
   buka sesuai scope folder 'drjava/drjava'
   bukan 'drjava/*'
   
