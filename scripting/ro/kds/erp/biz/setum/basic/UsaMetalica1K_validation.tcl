# Validation rules for UsaMetalica1K

set errors 0 ;# keep kount of the validation errors

if { $intFoil == 1 && $lg < 500 } {


    incr errors ;# errors = errors + 1
    $response addRecord
    $response addField message "Lminim trebuie sa fie cel putin 500 pentru foaie Lisa" 
    $response addField fieldId lg
}

$response setCode $errors
