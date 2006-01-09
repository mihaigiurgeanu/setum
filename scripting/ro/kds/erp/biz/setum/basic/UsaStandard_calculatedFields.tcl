if {[info exists usaName]} {
    set name $usaName
    set code $usaCode
} else {
    set name {}
    set code {}
}
if {[info exists broascaName]} {
    set name "$name-$broascaName"
    set code "$code-$broascaCode"
}
if {[info exists cilindruName]} {
    set name "$name-$cilindruName"
    set code "$code-$cilindruCode"
}
if {[info exists sildName]} {
    set name "$name-$sildName"
    set code "$code-$sildCode"
}
if {[info exists yallaName]} then {
    set name "$name-$yallaName"
    set code "$code-$yallaCode"
}
if {[info exists vizorName]} {
    set name "$name-$vizorName"
    set code "$code-$vizorCode"
}
