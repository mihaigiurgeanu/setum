# proformaPercent.tcl
# campul proformaPercent a fost modificat de utilizator

set proformaAmount [expr $proformaPercent * $totalFinal * $proformaExchangeRate / $attribute4 / 100]

set proformaTax [expr $proformaAmount * $tvaPercent / 100]

