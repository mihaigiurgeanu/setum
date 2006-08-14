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

set sellPrice [expr $se * ($pret_subcod + $pret_versiune + $pret_material + $pret_foaieInterioara + $pret_foaieExterioara + $pret_foaieInterioaraSecundara + $pret_foaieExterioaraSecundara + $pret_izolatie) + $pret_deschidere + $pret_parteDeschidere + $pret_pozitionareFoaie]

if {$montareSistem == 2} {
    set val_decupareSistem $pret_decupareSistem
    set sellPrice [expr $sellPrice + $val_decupareSistem]

} else {
    set val_broasca [expr $pret_broasca * $broascaBuc]
    set val_cilindru [expr $pret_cilindru * $cilindruBuc]
    set val_copiatCheie [expr $pret_copiatCheie * $copiatCheieBuc]
    set val_rozeta [expr $pret_rozeta * $rozetaBuc]
    set val_maner [expr $pret_maner * $manerBuc]
    set val_yalla1 [expr $pret_yalla1 * $yalla1Buc]
    set val_yalla2 [expr $pret_yalla2 * $yalla2Buc]
    set val_baraAntipanica [expr $pret_baraAntipanica * $baraAntipanicaBuc]
    set val_selectorOrdine [expr $pret_selectorOrdine * $selectorOrdineBuc]
    set val_amortizor [expr $pret_amortizor * $amortizorBuc]
    set val_manerSemicilindru [expr $pret_manerSemicilindru * $manerSemicilindruBuc]
    set val_alteSisteme1 [expr $pret_alteSisteme1 * $alteSisteme1Buc]
    set val_alteSisteme2 [expr $pret_alteSisteme2 * $alteSisteme2Buc]
    set val_tipBroascaBeneficiar [expr $pret_tipBroascaBeneficiar * $benefBroascaBuc]
    set val_tipCilindruBeneficiar [expr $pret_tipCilindruBeneficiar * $benefCilindruBuc]
    set val_tipSildBeneficiar [expr $pret_tipSildBeneficiar * $benefSildBuc]
    set val_tipYallaBeneficiar [expr $pret_tipYallaBeneficiar * $benefYallaBuc]
    set val_tipBaraAntipanicaBeneficiar [expr $pret_tipBaraAntipanicaBeneficiar * $benefBaraAntipanicaBuc]

    set sellPrice [expr $sellPrice + $val_tipBroascaBeneficiar + $val_tipCilindruBeneficiar + $val_tipSildBeneficiar + $val_tipYallaBeneficiar + $val_tipBaraAntipanicaBeneficiar + $val_broasca + $val_cilindru + $val_copiatCheie + $val_rozeta + $val_maner + $val_yalla1 + $val_yalla2 + $val_baraAntipanica + $val_selectorOrdine + $val_amortizor + $val_manerSemicilindru + $val_alteSisteme1 + $val_alteSisteme2]
}


if { $finisajTocBlat } {
    set intFinisajTocId $intFinisajBlatId
    set intFinisajToc $intFinisajBlat
}

if { $finisajGrilajBlat } {
    set intFinisajGrilajId  $intFinisajBlatId
    set intFinisajGrilaj $intFinisajBlat
}

if { $finisajFereastraBlat } {
    set intFinisajFereastraId $intFinisajBlatId
    set intFinisajFereastra $intFinisajBlat
}

if { $finisajSupraluminaBlat } {
    set intFinisajSupraluminaId $intFinisajBlatId
    set intFinisajSupralumina $intFinisajBlat
}

if { $finisajPanouLateralBlat } {
    set intFinisajPanouLateralId $intFinisajBlatId
    set intFinisajPanouLateral $intFinisajBlat
}

if { $finisajBlatExtInt } {
    set extFinisajBlatId $intFinisajBlatId
    set extFinisajBlat $intFinisajBlat
}
if { $finisajTocExtInt } {
    set extFinisajTocId $intFinisajTocId
    set extFinisajToc $intFinisajToc
}
if { $finisajGrilajExtInt } {
    set extFinisajGrilajId $intFinisajGrilajId
    set extFinisajGrilaj $intFinisajGrilaj
}
if { $finisajFereastraExtInt } {
    set extFinisajFereastraId $intFinisajFereastraId
    set extFinisajFereastra $intFinisajFereastra
}
if { $finisajSupraluminaExtInt } {
    set extFinisajSupraluminaId $intFinisajSupraluminaId
    set extFinisajSupralumina $intFinisajSupralumina
}
if { $finisajPanouLateralExtInt } {
    set extFinisajPanouLateralId $intFinisajPanouLateralId
    set extFinisajPanouLateral $intFinisajPanouLateral
}
