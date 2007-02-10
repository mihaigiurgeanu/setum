# invoiceTotal.tcl

set invoiceAmount [expr $invoiceTotal * 100 / ($tvaPercent + 100)]
set invoiceTax [expr $invoiceTotal - $invoiceAmount]
