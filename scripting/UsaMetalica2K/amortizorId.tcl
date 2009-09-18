# amortizorId.tcl


if { $amortizorId == 0 } { set amortizorBuc 0 } else {
    if { $amortizorBuc == 0 } { set amortizorBuc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $amortizorId getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "amortizor"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
