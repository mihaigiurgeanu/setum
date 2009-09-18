# copiatCheiId.tcl

if { $copiatCheieId == 0 } { set copiatCheieBuc 0 } else {
    if { $copiatCheieBuc == 0 } { set copiatCheieBuc 1 }
}

source "$scripting_root/commons.tcl" ;# includ definitii comune

set discontinued [product_by_id $copiatCheieId getDiscontinued]
if {$discontinued == 1} { 
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/discontinued" "copiat cheie"
    $response setCode [java::field ro.kds.erp.biz.ResponseBean CODE_ERR_VALIDATION]
}
