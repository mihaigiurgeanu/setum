# alteSisteme2Id.tcl

if { $alteSisteme2Id == 0 } { set alteSisteme2Buc 0 } else {
    if { $alteSisteme2Buc == 0 } { set alteSisteme2Buc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $alteSisteme2Id getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "alte sisteme"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
