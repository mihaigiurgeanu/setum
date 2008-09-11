source "$scripting_root/commons.tcl"




set absoluteGain [expr $price - $entryPrice]

if {$entryPrice != 0} {
    set relativeGain [expr $absoluteGain * 100/$entryPrice]
} else {
    set relativeGain 0
}

set vatPrice [expr round($price * 1.19)]

set locationCode [product_by_id $locationId getCode]

# daca este ales "alta localintate" nu modific valoarea locatiei
if {$locationCode != 0} {
    set distance [attribute_int $locationId distanta]
}

set pret_montaj [[product_by_id $montajId getPrice1] doubleValue]

set pret_km [[product_by_code 12006 10 getPrice1] doubleValue]
set valTransport [expr $distance * $pret_km * $deliveries]
set valMontaj [expr $pret_montaj + $montajProcent * $price / 100]
