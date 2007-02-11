# Reguli de validare pentru usa metalica



# Nume variabile

# nume variabila		- explicatie variabila

# response			- obiectul care contine raspunsul catre client

# lframe			- ltoc
# cframe			- ctoc
# bframe			- btoc


# proc report_err {field msg} {
#     global response
#     global errors

#     incr errors;
#     $response addRecord
#     $response addField message $msg
#     $response addField fieldId $field
# }


# proc rule_minmax { field field_name min max } {
#     upvar $field fieldVar
#     if { $fieldVar < $min } { report_err $field "$field_name trebuie sa fie cel putin $min. Valoarea curenta este $fieldVar." }
#     if { $fieldVar > $max } { report_err $field "$field_name trebuie sa fie cel mult $max. Valoarea curenta este $fieldVar." }
# }


package require java
java::import -package ro.kds.erp.biz ResponseBean
java::import -package ro.kds.erp.biz.setum.basic UsaMetalica2KForm

proc rule_minmax { field min max } {
    upvar $field fieldVar
    global response
    if { $fieldVar < $min } { 
	$response addValidationInfo [java::call UsaMetalica2KForm uri $field] [java::field ResponseBean MINRULE_VALIDATION_MESSAGE] $min
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }
    if { $fieldVar > $max } { 
	$response addValidationInfo [java::call UsaMetalica2KForm uri $field] [java::field ResponseBean MAXRULE_VALIDATION_MESSAGE] $max
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }
}



# set errors 0 ;# variabila errors contine numarul de erori

# Conditii pentru toc
rule_minmax lFrame 60 500
rule_minmax bFrame 35 100
rule_minmax cFrame 25 25

rule_minmax lTreshold 60 500
rule_minmax hTreshold 15 100
rule_minmax cTreshold 25 25


# conditii tip foaie pentru 1k

if { $k == 1 } {

    # Foaie Lisa
    if { intFoil == 1 } {
	rule_minmax lFoaie 420 1100
	rule_minmax hFoaie 420 2400
    }

    # Foaie amprentata
    if { intFoil == 2 } {
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
	rule_minmax lFoaieSec 260 1000
	rule_minmax hFoaie 1660 2020
    }
    

}



#$response setCode $errors

