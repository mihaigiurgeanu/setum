# alteSisteme1Id.tcl

if { $alteSisteme1Id == 0 } { set alteSisteme1Buc 0 } else {
    if { $alteSisteme1Buc == 0 } { set alteSisteme1Buc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $alteSisteme1Id getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "alte sisteme"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
