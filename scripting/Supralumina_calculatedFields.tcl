# Supralumina_calculatedFields

source "$scripting_root/commons.tcl" ;# includ definitii comune




# ls si hs sunt date in mm
set suprafata [expr $ls * $hs / 1000 / 1000]


set pretRama [product_by_code 11055 $deschidere getPrice1]

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
    set pretGeam [product_by_id $geamId getPrice1]
} else {
    set pretGeam 0
}


# valoare grilaj
if { $tipGrilaj == 1 } {
    # grilaj STAS
    set pretGrilaj [product_by_id $grilajStasId getPrice1]
} elseif { $tipGrilaj == 2 } {
    set pretGrilaj $valoareGrilajAtipic
} else {
    set pretGrilaj 0
}


set sellPrice [expr $suprafata * ($pretGeam + $pretRama) + $pretGrilaj]
