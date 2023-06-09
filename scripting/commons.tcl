# commons.tcl
# Contine definitii comune pentru divers scripturi
# SYNOPSYS
#   source "$scripting_root/commons.tcl" ;# includ definitii comune
#   set price [[product_by_id geamId getPrice1] doubleValue]
#   set price [[product_by_code $categId $productCode getPrice1] doubleValue]
#   set distance [attribute_int $locationId distance]


package require java
java::import -package ro.kds.erp.biz CommonServicesLocalHome CommonServicesLocal ProductNotAvailable
java::import -package ro.kds.erp.data ProductLocal


set srv [java::cast CommonServicesLocal [$factory local "ejb/CommonServices" [java::field CommonServicesLocalHome class]]]


;# folosita ca valoare de return pentru un obiect null in cazul 
;# procedurilor product_by_code_safe si product_by_id_safe.
proc nullvalobj {{argname {}}} {
    if {[string equal $argname doubleValue]} {
	return 0
    }
    return {}
}

;# Obtine un produs dupa cod si aplica o metoda produsului. Returneaza valoarea
;# pe care o intoarce aplicarea metodei.
;# Param valmethod contine numele metodei, de exemplu 
;# getPrice1 sau getSellPrice, etc.
proc product_by_code {catid code valmethod} {
    global srv
    if {
	[catch {
	    set product [$srv findProductByCode $catid $code]
	    set val [[$srv findProductByCode $catid $code] $valmethod]
	} err]
    } {
	puts "Eroare la obtinere $valmethod pentru codul de produs $code din categoria $catid: $err"
	set val {}
    }
    return $val
}

;# La fel ca pentru product_by_code dar pentru apeluri care se asteapta
;# la un obiect ca rezultat (de exemplu Double).
proc product_by_code_safe {catid code valmethod} {
    set retval [product_by_code $catid $code $valmethod]
    if {[string length $retval] > 0} {
	return $retval
    } {
	return nullvalobj
    }    
}

;# Obtine un produs dupa id si aplica o metoda produsului. Returneaza valoarea
;# pe care o intoarce aplicarea metodei.
;# Param valmethod contine numele metodei, de exemplu 
;# getPrice1 sau getSellPrice, etc.
proc product_by_id {pid valmethod} {
    global srv
    
    java::try {
	if {[catch {set val [[$srv findProductById $pid] $valmethod]} err]} {
	    puts "Eroare la obtinere $valmethod pentru produsul $pid: $err"
	    set val {}
	}
    } catch {Exception e} {
	puts "Eroare la obtinere $valmethod pentru produsul $pid: $err"
	set val {}
    }
    return $val 
}

;# La fel ca pentru product_by_code dar pentru apeluri care se asteapta
;# la un obiect ca rezultat (de exemplu Double).
proc product_by_id_safe {pid valmethod} {
    set retval [product_by_id $pid $valmethod]
    if {[string length $retval] > 0} {
	return $retval
    } {
	return nullvalobj
    }
}

proc attribute_str {pid attr} {
    global srv
    if {[catch {set val [[$srv getAttributeByProductId $pid $attr] getStringValue]} err]} {
	puts "Nu pot citi atributul $attr pentru produsul $pid: $err"
	set val {}
    }
    return $val
}

proc attribute_int {pid attr} {
    global srv
    if {[catch {set val [[$srv getAttributeByProductId $pid $attr] getIntValue]} err]} {
	puts "Nu pot citi atributul $attr pentru produsul $pid: $err"
	set val 0
    }
    return $val
}

proc attribute_dbl {pid attr} {
    global srv
    if {[catch {set val [[$srv getAttributeByProductId $pid $attr] getDoubleValue]} err]} {
	puts "Nu pot citi atributul $attr pentru produsul $pid: $err"
	set val 0
    }
    return $val
}

;# Obtine lista de reguli pentru un anumit set
proc get_rules {rulesSetName} {
    global srv
    return [$srv getRules $rulesSetName]
}

;# Evaluare reguli intr-un set
proc eval_rules {rulesSetName} {
    global logger

    set rules [get_rules $rulesSetName]
    set iterator [$rules iterator]
    while {[$iterator hasNext]} {
	set rule [java::cast ro.kds.erp.rules.RuleLocal [$iterator next]]
	set ruleName [$rule getName]
	set condition [$rule getCondition]
	set message [$rule getMessage]
	if {[$rule getErrorFlag]} {set errorScript {$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]}} else {set errorScript {}}
	
	set response_obj {$response}

	set script "if {$condition} {$response_obj addValidationInfo {http://www.kds.ro/readybeans/rdf/validation/message/empty} \"$ruleName: $message\" \n\t$errorScript}"

	$logger {log int Object} [java::field BasicLevel DEBUG] "Regula: $rulesSetName -- $ruleName"
	$logger {log int Object} [java::field BasicLevel DEBUG] "Script regula:\n$script"

	if {[catch {set result [uplevel 1 $script]} err]} {
	    $logger {log int Object} [java::field BasicLevel ERROR] "\tRule $ruleName could not be executed due to an error: $err"
	} 
    }
}
