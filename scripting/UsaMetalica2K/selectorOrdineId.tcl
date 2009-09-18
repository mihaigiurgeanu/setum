# selectorOrdineId.tcl

if { $selectorOrdineId == 0 } { set selectorOrdineBuc 0 } else {
    if { $selectorOrdineBuc == 0 } { set selectorOrdineBuc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $selectorOrdineId getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "selector ordine"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
