# PanouLateral_calculatedFields

source "$scripting_root/commons.tcl" ;# includ definitii comune
java::import -package org.objectweb.util.monolog.api Level BasicLevel

$logger {log int Object} [java::field BasicLevel DEBUG] "10. Panou lateral: start"

set description "Panou lateral [format %.0f $lpl] x [format %.0f $hpl]"

;# daca este fix nu am deschidere nici balamale
if { $deschidere == 1 } {
    set sensDeschidere 0
    set pozitionareBalamale 0
    set description "$description fix"
} else {
    set description "$description mobil"
}


# lpl si hpl sunt date in mm
set suprafata [expr $lpl * $hpl / 1000 / 1000]

$logger {log int Object} [java::field BasicLevel DEBUG] "20. Panou lateral: pretRama"

set pretRama [[product_by_code 11092 $deschidere getPrice1] doubleValue]

# in functie de deschidere, ajustez pretul pe mentru patrat 
# pentru fereastra mobila; pretul pentru fereastra fixa nu se ajusteaza
if { $deschidere == 2} {
    if {$suprafata <= 0.1} {
	set pretRama [expr $pretRama * 5]
    } elseif {$suprafata <= 0.2} {
	set pretRama [expr $pretRama * 3]
    } elseif {$suprafata <= 0.25} {
	set pretRama [expr $pretRama * 1.8]
    } elseif {$suprafata <= 0.5} {
	set pretRama [expr $pretRama * 1.3]
    }
}


# pretul pentru tip de geam il aflu fie din setul de geamuri termopane
# fie din setul de geamuri simple
if { $tipGeam == 1 } {
    set geamId $geamSimpluId
} elseif { $tipGeam == 2 } {
    set geamId $geamTermopanId
} else {
    set geamId 0
}


if { $geamId != 0 } {
    set pretGeam [[product_by_id $geamId getPrice1] doubleValue]
    set description "$description, geam [product_by_id $geamId getName]"
} else {
    set pretGeam 0
}

$logger {log int Object} [java::field BasicLevel DEBUG] "30. Panou lateral: valoare Grilaj"

# valoare grilaj
if { $tipGrilaj == 1 && $grilajStasId != 0} {
    # grilaj STAS
    set pretGrilaj [[product_by_id $grilajStasId getPrice1] doubleValue]
    set description "$description, grilaj [product_by_id $grilajStasId getName]"
} elseif { $tipGrilaj == 2 } {
    set pretGrilaj $valoareGrilajAtipic
} else {
    set pretGrilaj 0
}

$logger {log int Object} [java::field BasicLevel DEBUG] "40. Panou lateral: pret tabla"

#pret tabla
if { $tipTabla != 0 } {
    set pretTabla [[product_by_code 11080 $tipTabla getPrice1] doubleValue]
    set description "$description, tabla [product_by_code 11080 $tipTabla getName]"
} else {
    set pretTabla 0
}

$logger {log int Object} [java::field BasicLevel DEBUG] "suprafata: $suprafata"
$logger {log int Object} [java::field BasicLevel DEBUG] "pretGeam: $pretGeam"
$logger {log int Object} [java::field BasicLevel DEBUG] "pretRama: $pretRama"
$logger {log int Object} [java::field BasicLevel DEBUG] "pretGrilaj: $pretGrilaj"
$logger {log int Object} [java::field BasicLevel DEBUG] "pretTabla: $pretTabla"

if {$suprafata < 0.5} {
    set supr_geam 0.5
} else {
    set supr_geam $suprafata
}

set sellPrice [expr $supr_geam * $pretGeam + $suprafata * $pretRama + $suprafata * $pretTabla + $pretGrilaj]
set sellPrice [expr $quantity * $sellPrice]
set price1 $sellPrice ;# pretul cu usa


$logger {log int Object} [java::field BasicLevel DEBUG] "50. Panou lateral: end"
