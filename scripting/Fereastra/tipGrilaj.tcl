# tipGrilaj.tcl
# script aplelat la modificarea campului tipGrilaj

# Daca selectez grilaj STAS, sterg valoarea de la grilaj atipic;
# daca selectez grilaj atipic, sterg valoarea de la grilaj STAS

if { $tipGrilaj == 1 } {
    set valoareGrilajAtipic 0
} elseif { $tipGrilaj == 2 } {
    set grilajStasId 0
}

