# Orders_calculatedFields.tcl



#set termenLivrare1 [expr $termenLivrare + 1 * 24 * 3600]


# Campuri comanda

set totalTva [expr $total * ($tvaPercent + 100)/100]
set totalFinal [expr $total * (100 - $discount)/100]
set totalFinalTva [expr $totalFinal * (100 + $tvaPercent)/100]
set diferenta [expr $totalFinalTva - $payedAmount]



# Campuri linie de comanda

set value [expr $price * $quantity]
set tax [expr $value * $tvaPercent / 100]

if { $productPrice != 0 } {
    set priceRatio [expr ($price - $productPrice) * 100 / $productPrice]
} else {
    set priceRatio 0
}


# Campuri factura

set invoiceTotal [expr $invoiceAmount + $invoiceTax]
set invoiceUnpayed [expr $invoiceTotal - $invoicePayed]


