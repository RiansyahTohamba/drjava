# start - mulai build proses serangan! 

klo init-init biasanya hanya berisi 'new Class()'
ada juga init yang tidak pakai new Class()

# mulai dari AbstractGlobalModel

AbstractGlobalModel
	_init=10
	addToBrowserHistory=6.625
	_locateClassFile=7.75

## _init()
hanya dipakai di local class, makanya privat

length = 366-297 = 69 LOC
===== 297
_gainVisitor = 299 - 325
_documentNavigator.addNavigationListener = 328 - 333
_documentNavigator.addFocusListener = 336 - 343

browserHistoryMaxSizeListener = 359 - 363
setMaximumSize
===== 366


# intellijdeodorant tidak bisa digunakan

2 kendala
1. build selalu gagal: java: error: invalid source release: 16
2. interface JUnitModel tidak dapat digunakan

## addToBrowserHistory()


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
