# cilindruId.tcl

if { $cilindruId == 0 } { set cilindruBuc 0 } else {
    if { $cilindruBuc == 0 } { set cilindruBuc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $cilindruId getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "cilindru"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
