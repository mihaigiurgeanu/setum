# Valorile campurilor calculate

if {$price == 0} {
    set price [expr $sellPrice]
}

set vatPrice [expr round($price * 1.24)]

set period [expr ($dateTo - $dateFrom)/(3600 * 24)]

set absoluteGain [expr $price - $entryPrice]

if {$entryPrice != 0} {
    set relativeGain [expr $absoluteGain * 100/ $entryPrice]
}

