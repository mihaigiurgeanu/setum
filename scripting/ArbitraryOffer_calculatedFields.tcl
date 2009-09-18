source "$scripting_root/commons.tcl"




set absoluteGain [expr $price - $sellPrice]

if {$sellPrice != 0} {
    set relativeGain [expr $absoluteGain * 100/$sellPrice]
} else {
    set relativeGain 0
}

set vatPrice [expr $price * 1.19]

set locationCode 0
if { $locationId != 0 } {
    set locationCode [product_by_id $locationId getCode]
}

# daca este ales "alta localintate" nu modific valoarea locatiei
if {$locationCode != 0} {
    set distance [attribute_dbl $locationId distanta]
    set otherLocation [product_by_id $locationId getName]
}

set pret_montaj 0
if { $montajId != 0 } {
    set pret_montaj [[product_by_id $montajId getPrice1] doubleValue]
}


set pret_km [[product_by_code 12006 10 getPrice1] doubleValue]

set valTransport [expr $distance * $pret_km * $deliveries]
set valMontaj [expr $quantity * ($pret_montaj + $montajProcent * $price / 100)]

if {[info exists attribute5] == 0  || [string length $attribute5] == 0} {
    set attribute5 RON
    set attribute6 1
}

if {[info exists attribute6] == 0  
    || [string length $attribute6] == 0 
    || $attribute6 == 0} {

    set attribute6 1
}

