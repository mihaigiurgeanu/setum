# manerId.tcl

if { $manerId == 0 } { 
    set manerTip ""
    set manerCuloare ""
    set manerBuc 0
} else {
    if { $manerBuc == 0 } { set manerBuc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $manerId getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "maner"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
