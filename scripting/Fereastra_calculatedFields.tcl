# Fereastra_calculatedFields.tcl

package require java
java::import -package ro.kds.erp.biz CommonServicesLocalHome CommonServicesLocal ProductNotAvailable
java::import -package ro.kds.erp.data ProductLocal



set srv [java::cast CommonServicesLocal [$factory local "ejb/CommonServices" [java::field CommonServicesLocalHome class]]]

;# Obtine un produs dupa cod si aplica o metoda pentru a obtine o valoare
;# valmethod este metoda care trebuie aplicata produsului pentru a obtine
;# valoarea dorita, de exemplu getPrice1 sau getSellPrice, etc.
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


;# Obtine un produs dupa cod si aplica o metoda pentru a obtine o valoare
;# valmethod este metoda care trebuie aplicata produsului pentru a obtine
;# valoarea dorita, de exemplu getPrice1 sau getSellPrice, etc.
proc product_by_id {pid valmethod} {
    global srv
    if {[catch {set val [[[$srv findProductById $pid] $valmethod] doubleValue]} err]} {
	puts "Eroare la obtinere $valmethod pentru produsul $pid: $err"
	set val 0
    }
    return $val
}




# CALCUL FEREASTRA


# valoarea pe metrul patrat (X)
set X [product_by_code 11055 $deschidere getPrice1]


# suprafata ferestrei (lf si hf sunt in mm)
set MP [expr $lf*$hf/1000/1000]

# in functie de deschidere, ajustez pretul pe mentru patrat 
# pentru fereastra mobila; pretul pentru fereastra fixa nu se ajusteaza
if { $deschidere == 2} {
    if {$MP <= 0.1} {
	set X [expr $X * 5]
    } elseif {$MP <= 0.2} {
	set X [expr $X * 3]
    } elseif {$MP <= 0.25} {
	set X [expr $X * 1.8]
    } elseif {$MP <= 0.5} {
	set X [expr $X * 1.3]
    }
}


# pretul pentru tip de geam il aflu fie din setul de geamuri termopane
# fie din setul de geamuri simple
if { $tipGeam == 1 } {
    set geamId $geamSimpluId
} elseif { $tipGeam == 2 } {
    set geamId $geamTermopanId
} else {
    set geamId 0
}


if { $geamId != 0 } {
    set Y [product_by_id $geamId getPrice1]
} else {
    set Y 0
}



# valoare grilaj
if { $tipGrilaj == 1 } {
    # grilaj STAS
    set Z [product_by_id $grilajStasId getPrice1]
} elseif { $tipGrilaj == 2 } {
    set Z $valoareGrilajAtipic
} else {
    set Z 0
}


# A = Valoare rama fereastra = MP * Val fereastra/mp
set A [expr $MP * $X]

# B = Valoare geam = MP * Y
if {$MP > 0.5} {
    set B [expr $MP * $Y]
} else {
    set B [expr $MP * 0.5]
}

# C = valoarea grilajului = Z
set C $Z


# Valoare fereastra = A + B + C
set sellPrice [set price1 [expr $A + $B + $C]]


