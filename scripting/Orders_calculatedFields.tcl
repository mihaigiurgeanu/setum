# Orders_calculatedFields.tcl

package require java;

java::import org.objectweb.util.monolog.api.BasicLevel

source "$scripting_root/commons.tcl"

proc log {message} {
    global logger
    if [info exists logger] {
	$logger {log int Object} [java::field BasicLevel INFO] $message
    } else {
	puts $message
    }
}

#set termenLivrare1 [expr $termenLivrare + 1 * 24 * 3600]


# Campuri comanda

if {[info exists attribute5] == 0  || [string length $attribute5] == 0} {
    set attribute5 RON
    set attribute4 1
}

if {[info exists attribute4] == 0  
    || [string length $attribute4] == 0 
    || $attribute4 == 0} {

    set attribute4 1
}

log "pas 1"

set totalTva [expr $total * ($tvaPercent + 100.00)/100.00]
set totalFinal [expr $total * (100.00 - $discount)/100.00]
set totalFinalTva [expr $totalFinal * (100.00 + $tvaPercent)/100.00]
set diferenta [expr $totalFinalTva - $currencyPayedAmount * $attribute4]

log "pas 2"

if {$diferenta > 0} {
    log "pas 2.0.1"
    set currencyDiferenta [expr (round(100.00 * $diferenta / $attribute4) + 0.0)/ 100.00]
} else {
    log "pas 2.0.2"
    set currencyDiferenta 0
}

log "pas 2.1"
set totalCurrency [expr (round(100.00 * $total / $attribute4) + 0.0)/ 100.00]
log "pas 2.2"
set totalTvaCurrency [expr (round(100.00 * $totalTva / $attribute4) + 0.0)/ 100.00]
log "pas 2.3"
set totalFinalCurrency [expr (round(100.00 * $totalFinal / $attribute4) + 0.0)/ 100.00]
log "pas 2.4"
set totalFinalTvaCurrency [expr (round(100.00 * $totalFinalTva / $attribute4) + 0.0)/ 100.00]

log "pas 3"

# daca este ales "alta localintate" nu modific valoarea locatiei
if {$localitate != 0} {
    set localitateId [product_by_code 12005 $localitate getProductId]
    set localitateAlta [product_by_code 12005 $localitate getName]
    set distanta [attribute_dbl $localitateId distanta]
}


log "pas 4"

# Campuri linie de comanda

set value [expr 0.0 + 1.0 * $price * $quantity]
set tax [expr 0.0 + 1.0 * $value * $tvaPercent / 100.00]

if { $productPrice != 0 } {
    set priceRatio [expr (0.0 + $price - $productPrice) * 100 / $productPrice]
} else {
    set priceRatio 0
}

log "pas 5"

# Campuri proforma
if {[info exists proformaExchangeRate] == 0 || $proformaExchangeRate == 0} {
    set proformaExchangeRate $attribute4
}

if { $proformaExchangeRate == 0 } { set proformaExchangeRate 1.0 }

set proformaTotal [expr 0.0 + $proformaAmount + $proformaTax]

set proformaCurrency $attribute5

set proformaTaxCurrency [expr 0.0 + $proformaTax /  $proformaExchangeRate]
set proformaAmountCurrency [expr 0.0 + $proformaAmount / $proformaExchangeRate]
set proformaTotalCurrency [expr 0.0 + $proformaAmountCurrency + $proformaTaxCurrency]

if { $totalFinal != 0 } {
    set proformaPercent [expr 0.0 + $proformaAmountCurrency * $attribute4 * 100.0 / $totalFinal]
} else {
    set proformaPercent 0
}

if {[info exists proformaContract] == 0 || 
    [string length $proformaContract] == 0} {

    set proformaContract $number
} 

log "pas 6"

# Campuri factura

if {[info exists invoiceExchangeRate] == 0 || $invoiceExchangeRate == 0} {
    set invoiceExchangeRate $attribute4
}

if {[info exists paymentExchangeRate] == 0 || $paymentExchangeRate == 0} {
    set paymentExchangeRate $attribute4
}

set invoiceTotal [expr 0.0 + $invoiceAmount + $invoiceTax]
set invoiceUnpayed [expr 0.0 + $invoiceTotal - $invoicePayed]


