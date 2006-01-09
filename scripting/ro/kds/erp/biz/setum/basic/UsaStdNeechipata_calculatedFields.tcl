set absoluteGain [expr $sellPrice - $entryPrice]
if {$entryPrice != 0 } {
    set relativeGain [expr $absoluteGain * 100 / $entryPrice]
}
