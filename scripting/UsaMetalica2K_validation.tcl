# Reguli de validare pentru usa metalica


package require java
java::import -package ro.kds.erp.biz ResponseBean
java::import -package ro.kds.erp.biz.setum.basic UsaMetalica2KForm

proc rule_minmax { field min max } {
    upvar $field fieldVar
    global response
    if { $fieldVar < $min } { 
	$response addValidationInfo [java::call UsaMetalica2KForm uri $field] [java::field ResponseBean MINRULE_VALIDATION_MESSAGE] "$min mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }
    if { $fieldVar > $max } { 
	$response addValidationInfo [java::call UsaMetalica2KForm uri $field] [java::field ResponseBean MAXRULE_VALIDATION_MESSAGE] "$max mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }
}



# set errors 0 ;# variabila errors contine numarul de erori

# Conditii pentru toc
rule_minmax lFrame 60 500
rule_minmax cFrame 25 25

rule_minmax lTreshold 60 500
if {$tresholdType != 3} {
    rule_minmax hTreshold 15 100
    rule_minmax bFrame 35 100
} else {
    rule_minmax bFrame 0 100
}
rule_minmax cTreshold 25 25


# conditii tip foaie pentru 1k

if { $k == 1 } {

    # Foaie Lisa
    if { $intFoil == 1 } {
	rule_minmax lFoaie 420 1100
	rule_minmax hFoaie 420 2400
    }

    # Foaie amprentata
    if { $intFoil == 2 } {
	rule_minmax lFoaie 690 1000
	rule_minmax hFoaie 1660 2090
    }


}


# conditii tip foaie pentru 2k
if { $k == 2 } {

    # Foaie Lisa, k principal
    if { $intFoil == 1 } {
	rule_minmax lFoaie 420 1100
	rule_minmax hFoaie 420 2400
    }

    # Foaie amprentata, k principal
    if { $intFoil == 2 } {
	rule_minmax lFoaie 690 1000
	rule_minmax hFoaie 1660 2020
    }

    # Foaie Lisa, k secundar
    if { $intFoil == 1 } {
	rule_minmax lFoaieSec 165 1100
	rule_minmax hFoaie 1660 2020
    }

    # Foaie amprentata, k secundar
    if { $intFoil == 2 } {
	rule_minmax lFoaieSec 365 1000
	if {$lFoaieSec < 600} {
	    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/Usa2K#amprenta_necentrata"
	} else {
	    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/Usa2K#amprenta_necentrata"
	}
	rule_minmax hFoaie 1660 2020
    }
    

    # Sisteme
    if { $lFoaie <= 750 && $lFoaie > 710 } {
	$response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/Usa2K#sistem_incompatibil" "CISA"
    } elseif { $lFoaie <= 710 $$ $lFoaie >= 700 } {
	$response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/Usa2K#sistem_incompatibil" "CISA, ABUS"
    }
}



#$response setCode $errors

