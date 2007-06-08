# commons.tcl
# Contine definitii comune pentru divers scripturi


package require java
java::import -package ro.kds.erp.biz CommonServicesLocalHome CommonServicesLocal ProductNotAvailable
java::import -package ro.kds.erp.data ProductLocal


set srv [java::cast CommonServicesLocal [$factory local "ejb/CommonServices" [java::field CommonServicesLocalHome class]]]




;# Obtine un produs dupa cod si aplica o metoda produsului. Returneaza valoarea
;# pe care o intoarce aplicarea metodei.
;# Param valmethod contine numele metodei, de exemplu 
;# getPrice1 sau getSellPrice, etc.
proc product_by_code {catid code valmethod} {
    global srv
    if {
	[catch {
	    set product [$srv findProductByCode $catid $code]
	    set val [[[$srv findProductByCode $catid $code] $valmethod] doubleValue]
	} err]
    } {
	puts "Eroare la obtinere $valmethod pentru codul de produs $code din categoria $catid: $err"
	set val 0
    }
    return $val
}


;# Obtine un produs dupa id si aplica o metoda produsului. Returneaza valoarea
;# pe care o intoarce aplicarea metodei.
;# Param valmethod contine numele metodei, de exemplu 
;# getPrice1 sau getSellPrice, etc.
proc product_by_id {pid valmethod} {
    global srv
    if {[catch {set val [[[$srv findProductById $pid] $valmethod] doubleValue]} err]} {
	puts "Eroare la obtinere $valmethod pentru produsul $pid: $err"
	set val 0
    }
    return $val
}



