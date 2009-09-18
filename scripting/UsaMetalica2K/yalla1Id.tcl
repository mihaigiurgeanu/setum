# yalla1Id.tcl

if { $yalla1Id == 0 } { set yalla1Buc 0 } else {
    if { $yalla1Buc == 0 } { set yalla1Buc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $yalla1Id getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "yalla"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
