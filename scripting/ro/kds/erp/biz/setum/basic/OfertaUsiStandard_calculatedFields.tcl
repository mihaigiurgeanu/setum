set absoluteGain [expr $price - $entryPrice]
if { $entryPrice != 0 } {
    set relativeGain [expr $absoluteGain * 100/$entryPrice]
}

set period [expr ($dateTo - $dateFrom)/(3600 * 24)]

