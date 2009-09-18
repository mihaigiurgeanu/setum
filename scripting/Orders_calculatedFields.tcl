# Orders_calculatedFields.tcl

source "$scripting_root/commons.tcl"


#set termenLivrare1 [expr $termenLivrare + 1 * 24 * 3600]


# Campuri comanda

set totalTva [expr $total * ($tvaPercent + 100)/100]
set totalFinal [expr $total * (100 - $discount)/100]
set totalFinalTva [expr $totalFinal * (100 + $tvaPercent)/100]
set diferenta [expr $totalFinalTva - $payedAmount]


# daca este ales "alta localintate" nu modific valoarea locatiei
if {$localitate != 0} {
    set localitateId [product_by_code 12005 $localitate getProductId]
    set localitateAlta [product_by_code 12005 $localitate getName]
    set distanta [attribute_dbl $localitateId distanta]
}



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


if {[info exists attribute5] == 0  || [string length $attribute5] == 0} {
    set attribute5 RON
    set attribute4 1
}

if {[info exists attribute4] == 0  
    || [string length $attribute4] == 0 
    || $attribute4 == 0} {

    set attribute4 1
}
