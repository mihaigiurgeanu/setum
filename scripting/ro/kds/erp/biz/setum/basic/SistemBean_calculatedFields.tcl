# SistemBean_calculatedFields.tcl
#
# Campuri calculate pentru form-ul de sisteme

set absoluteGainSP [expr $sellPrice - $entryPrice]
set absoluteGaimPP [expr $partPrice - $entryPrice]

if {$entryPrice != 0 } {
    set relativeGainSP [expr $absoluteGainSP * 100/$entryPrice]
    set relativeGainPP [expr $absoluteGainPP * 100/$entryPrice]
}
