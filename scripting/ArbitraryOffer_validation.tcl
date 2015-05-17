# ArbitraryOffer_validation.tcl
# reguli de validare pentru ArbitraryOffer

package require java
java::import -package ro.kds.erp.biz ResponseBean

if {$montajId == 0} {
    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/ArbitraryOffer#montajNeselectat" 0
    $response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
}