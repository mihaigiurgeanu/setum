# proformaTotal.tcl

set proformaAmount [expr $proformaTotal * 100 / ($tvaPercent + 100)]
set proformaTax [expr $proformaTotal - $proformaAmount]
