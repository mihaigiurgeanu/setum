source "$scripting_root/commons.tcl"

java::import -package ro.kds.erp.biz.setum OfertaUsiStandardBean

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


set valVatMontaj [expr $valMontaj * 1.24]
set valVatTransport [expr $valTransport * 1.24]

set vatPrice [java::call OfertaUsiStandardBean {round double} [expr $price * 1.24]]

set price [expr $vatPrice / 1.24]

# calculez adaosul fata de pretul de vanzare;
# ca sa calculez un adaos real, calculez un sellPrice rotunjit exact dupa
# formula cu care rotunjesc si pretul din oferta.
set sellVatPrice [java::call OfertaUsiStandardBean {round double} [expr $sellPrice * 1.24]]
set sellPriceRotunjit [expr $sellVatPrice / 1.24]

set absoluteGain [expr $price - $sellPriceRotunjit]

if {$sellPriceRotunjit != 0} {
    set relativeGain [expr $absoluteGain * 100/$sellPriceRotunjit]
} else {
    set relativeGain 0
}
