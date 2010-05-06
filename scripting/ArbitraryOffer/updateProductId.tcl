# updateProductId.tcl
# Actualizez pretul ofertei dupa pastrand adaosul procentual
set price [expr ($relativeGain + 100) * $sellPrice / 100]