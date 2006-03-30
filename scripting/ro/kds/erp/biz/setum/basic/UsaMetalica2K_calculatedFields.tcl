# Computes the values of calculated fields

package require java;

java::import org.objectweb.util.monolog.api.BasicLevel



proc log {message} {
    if [info exists logger] {
	$logger log [java::field BasicLevel INFO] $message
    } else {
	puts $message
    }
}


proc read_prop {varname objname propname} {
    upvar $varname buffer
    upvar $objname obj
    if {[info exists obj]} {
	set buffer [[java::prop $obj $propname] doubleValue]
	log "$varname read: $buffer"
    } else {
	log "Object $objname not set. The $varname will be 0"
	set buffer 0
    }
}


set name "$version$subclass$code"

log "lg: $lg"
log "hg: $hg"

set le [expr $lg - $lcorrection - 20]
log "le: $le"

set he [expr $hg - $hcorrection - 10]
log "he: $he"

set se [expr ($le * $he)/1000000]
log "se: $se"


read_prop pret_subcod subclassObj price1
read_prop pret_versiune versionObj price1
read_prop pret_material materialObj price1
read_prop pret_foaieInterioara intFoilObj price1
read_prop pret_foaieExterioara extFoilObj price1
read_prop pret_foaieInterioaraSecundara intFoilSecObj price1
read_prop pret_foaieExterioaraSecundara extFoilSecObj price1
read_prop pret_izolatie isolationObj price1
read_prop pret_deschidere openingDirObj price1
read_prop pret_parteDeschidere openingSideObj price1
read_prop pret_pozitionareFoaie foilPositionObj price1
read_prop pret_tipBroascaBeneficiar benefBroascaTipObj price2
read_prop pret_tipCilindruBeneficiar benefCilindruTipObj price2
read_prop pret_tipSildBeneficiar benefSildTipObj price2
read_prop pret_tipYallaBeneficiar benefYallaTipObj price2
read_prop pret_tipBaraAntipanicaBeneficiar benefBaraAntipanicaTipObj price2
read_prop pret_broasca broasca price1
read_prop pret_cilindru cilindru price1
read_prop pret_copiatCheie copiatCheie price1
read_prop pret_sild sild price1
read_prop pret_rozeta rozeta price1
read_prop pret_maner maner price1
read_prop pret_yalla1 yalla1 price1
read_prop pret_yalla2 yalla2 price1
read_prop pret_baraAntipanica baraAntipanica price1
read_prop pret_selectorOrdine selectorOrdine price1
read_prop pret_amortizor amortizor price1
read_prop pret_decupare decupareSistem price2 ;# manopera
read_prop pret_manerSemicilindru manerSemicilindru price1
read_prop pret_alteSisteme1 alteSisteme1 price1
read_prop pret_alteSisteme2 alteSisteme2 price1

set sellPrice [expr $se * ($pret_subcod + $pret_versiune + $pret_material + $pret_foaieInterioara + $pret_foaieExterioara + $pret_foaieInterioaraSecundara + $pret_foaieExterioaraSecundara + $pret_izolatie) + $pret_deschidere + $pret_parteDeschidere + $pret_pozitionareFoaie + $pret_tipBroascaBeneficiar + $pret_tipCilindruBeneficiar + $pret_tipSildBeneficiar + $pret_tipYallaBeneficiar + $pret_tipBaraAntipanicaBeneficiar + $pret_broasca + $pret_cilindru + $pret_copiatCheie + $pret_rozeta + $pret_maner + $pret_yalla1 + $pret_yalla2 + $pret_baraAntipanica + $pret_selectorOrdine + $pret_amortizor + $pret_decupare + $pret_manerSemicilindru + $pret_alteSisteme1 + $pret_alteSisteme2]
