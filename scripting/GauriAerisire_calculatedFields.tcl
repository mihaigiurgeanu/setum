# GauriAerisire_calculatedFields.tcl
source "$scripting_root/commons.tcl"

set unit_price [[product_by_code 11098 1 getPrice1] doubleValue]

set price1 [expr $unit_price * $quantity]
set sellPrice $price1
