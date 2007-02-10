# totalFinal.tcl
# calculeaza noul discount

if { $total } {
    set discount [expr ($total - $totalFinal) * 100/$total]
}

