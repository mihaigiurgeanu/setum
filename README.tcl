

* Exemplu apelare serviciilor ejb din interiorul unui script:

	 package require java

	 java::import -package ro.kds.erp.biz.setum.basic FereastraHome


	 set fereastra [$factory remote "ejb/FereastraHome" [java::field FereastraHome class]]
	 $fereastra [load 124]

