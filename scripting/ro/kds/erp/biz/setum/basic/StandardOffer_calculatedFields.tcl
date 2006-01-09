# Valorile campurilor calculate

if {$price == 0} {
    set price [expr $entryPrice * 110/100]
}

set period [expr ($dateTo - $dateFrom)/(3600 * 24)]

set absoluteGain [expr $price - $entryPrice]

if {$entryPrice != 0} {
    set relativeGain [expr $absoluteGain * 100/ $entryPrice]
}
