# manerSemicilindruId.tcl


if { $manerSemicilindruId == 0 } { set manerSemicilindruBuc 0 } else {
    if { $manerSemicilindruBuc == 0 } { set manerSemicilindruBuc 1 }
}


source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $manerSemicilindruId getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "maner semicilindru"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
