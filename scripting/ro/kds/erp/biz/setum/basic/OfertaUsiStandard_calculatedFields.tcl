set absoluteGain [expr $price - $entryPrice]
if { $entryPrice != 0 } {
    set relativeGain [expr $absoluteGain * 100/$entryPrice]
}
