# broascaId.tcl

if { $broascaId == 0 } { set broascaBuc 0 } else {
    if { $broascaBuc == 0 } { set broascaBuc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $broascaId getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "broasca"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
