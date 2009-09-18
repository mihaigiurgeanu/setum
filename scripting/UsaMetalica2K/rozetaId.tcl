# rozetaId.tcl


if { $rozetaId == 0 } {
    set rozetaTip ""
    set rozetaCuloare ""
    set rozetaBuc 0
} else {
    if { $rozetaBuc == 0 } { set rozetaBuc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $rozetaId getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "rozeta"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
