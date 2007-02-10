# Reguli de validare pentru usa metalica



# Nume variabile

# nume variabila		- explicatie variabila

# response			- obiectul care contine raspunsul catre client

# lframe			- ltoc
# cframe			- ctoc
# bframe			- btoc


proc report_err {field msg} {
    global response
    global errors

    incr errors;
    $response addRecord
    $response addField message $msg
    $response addField fieldId $field
}


proc rule_minmax { field field_name min max } {
    upvar $field fieldVar
    if { $fieldVar < $min } { report_err $field "$field_name trebuie sa fie cel putin $min. Valoarea curenta este $fieldVar." }
    if { $fieldVar > $max } { report_err $field "$field_name trebuie sa fie cel mult $max. Valoarea curenta este $fieldVar." }
}





set errors 0 ;# variabila errors contine numarul de erori

# Conditii pentru toc
rule_minmax lFrame ltoc 60 500
rule_minmax bFrame btoc 35 100
rule_minmax cFrame ctoc 25 25

rule_minmax lTreshold lprag 60 500
rule_minmax hTreshold hprag 15 100
rule_minmax cTreshold cprag 25 25


# conditii tip foaie pentru 1k

if { $k == 1 } {

    # Foaie Lisa
    if { intFoil == 1 } {
	rule_minmax lFoaie lFoaie 420 1100
	rule_minmax hFoaie hFoaie 420 2400
    }

    # Foaie amprentata
    if { intFoil == 2 } {
	rule_minmax lFoaie lFoaie 690 1000
	rule_minmax hFoaie hFoaie 1660 2090
    }


}


# conditii tip foaie pentru 2k
if { $k == 2 } {

    # Foaie Lisa, k principal
    if { $intFoil == 1 } {
	rule_minmax lFoaie lFoaie 420 1100
	rule_minmax hFoaie hFoaie 420 2400
    }

    # Foaie amprentata, k principal
    if { $intFoil == 2 } {
	rule_minmax lFoaie lFoaie 690 1000
	rule_minmax hFoaie hFoaie 1660 2020
    }

    # Foaie Lisa, k secundar
    if { $intFoil == 1 } {
	rule_minmax lFoaieSec lFoaieSec 165 1100
	rule_minmax hFoaie hFoaie 1660 2020
    }

    # Foaie amprentata, k secundar
    if { $intFoil == 2 } {
	rule_minmax lFoaieSec lFoaieSec 260 1000
	rule_minmax hFoaie hFoaie 1660 2020
    }
    

}



$response setCode $errors

