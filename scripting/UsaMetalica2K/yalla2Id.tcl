# yalla2Id.tcl

if { $yalla2Id == 0 } { set yalla2Buc 0 } else {
    if { $yalla2Buc == 0 } { set yalla2Buc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $yalla2Id getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "yalla"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
