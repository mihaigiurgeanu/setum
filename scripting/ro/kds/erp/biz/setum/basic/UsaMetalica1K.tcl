# DEPRECATED
#
# This script is not used anymore. It was splited into 2 other scripts,
# one for validation and one for computing the calculated fields:
# UsaMetalica1K_validation.tcl
# UsaMetalica1K_calculatedFields.tcl
# 
#



# Validation rules for UsaMetalica1K

set le [expr $lg - $lcorrection - 20]
set he [expr $hg - $hcorrection - 10]

set errors 0

if { $intFoil == 1 && $lg < 500 } {
    incr errors
    $response addRecord
    $response addField message "Lminim trebuie sa fie cel putin 500 pentru foaie Lisa" 
    $response addField fieldId lg
}

$response setCode $errors
