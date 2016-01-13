# entryPrice.tcl
#
# Pretul de intrare a fost modificat

set sellPrice [expr $entryPrice + round($entryPrice * $relativeGainSP / 100)]
set partPrice [expr $entryPrice + round($entryPrice * $relativeGainPP / 100)]
