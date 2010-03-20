# Caclucare pret usa metalica

package require java;

java::import org.objectweb.util.monolog.api.BasicLevel
java::import -package ro.kds.erp.data ProductLocal

source "$scripting_root/commons.tcl" ;# includ definitii comune


proc log {message} {
    global logger
    if [info exists logger] {
	$logger {log int Object} [java::field BasicLevel INFO] $message
    } else {
	puts $message
    }
}

log "----start preturi sisteme----"
set pret_subcod [[product_by_code_safe 11001 $subclass getPrice1] doubleValue]
set pret_versiune [[product_by_code_safe 11002 $version getPrice1] doubleValue]
set pret_material [[product_by_code_safe 11003 $material getPrice1] doubleValue]
set pret_foaieInterioara [[product_by_code_safe 11006 $intFoil getPrice1] doubleValue]
set pret_foaieExterioara [[product_by_code_safe 11006 $extFoil getPrice1] doubleValue]
set pret_foaieInterioaraSecundara [[product_by_code_safe 11006 $intFoilSec getPrice1] doubleValue]
set pret_foaieExterioaraSecundara [[product_by_code_safe 11006 $extFoilSec getPrice1] doubleValue]
set pret_izolatie [[product_by_code_safe 11010 $isolation getPrice1] doubleValue]
set pret_deschidere [[product_by_code_safe 11004 $openingDir getPrice1] doubleValue]
set pret_parteDeschidere [[product_by_code_safe 11005 $openingSide getPrice1] doubleValue]
set pret_pozitionareFoaie [[product_by_code_safe 11008 $foilPosition getPrice1] doubleValue]
set pret_tipBroascaBeneficiar [[product_by_code_safe 11020 $benefBroascaTip getPrice1] doubleValue]
set pret_tipCilindruBeneficiar [[product_by_code_safe 11021 $benefCilindruTip getPrice1] doubleValue]
set pret_tipSildBeneficiar [[product_by_code_safe 11023 $benefSildTip getPrice1] doubleValue]
set pret_tipYallaBeneficiar [[product_by_code_safe 11022 $benefYallaTip getPrice1] doubleValue]
set pret_tipVizorBeneficiar [[product_by_code_safe 11025 $benefVizorTip getPrice1] doubleValue]
set pret_tipBaraAntipanicaBeneficiar [[product_by_code_safe 11024 $benefBaraAntipanicaTip getPrice1] doubleValue]
set pret_tipManerBeneficiar [[product_by_code_safe 11026 $benefManerTip getPrice1] doubleValue]
set pret_tipSelectorOrdineBeneficiar [[product_by_code_safe 11027 $benefSelectorOrdineTip getPrice1] doubleValue]
set pret_tipAmortizorBeneficiar [[product_by_code_safe 11028 $benefAmortizorTip getPrice1] doubleValue]
set pret_tipAlteSisteme1Beneficiar [[product_by_code_safe 11029 $benefAlteSisteme1Tip getPrice1] doubleValue]
set pret_tipAlteSisteme2Beneficiar [[product_by_code_safe 11029 $benefAlteSisteme2Tip getPrice1] doubleValue]

log "----1----"
set pret_broasca [[product_by_id_safe $broascaId getPrice1] doubleValue]
set montare_broasca [[product_by_id_safe $broascaId getPrice2] doubleValue]
set pret_cilindru [[product_by_id_safe $cilindruId getPrice1] doubleValue]
set montare_cilindru [[product_by_id_safe $cilindruId getPrice2] doubleValue]
set pret_copiatCheie [[product_by_id_safe $copiatCheieId getPrice1] doubleValue]
set montare_copiatCheie [[product_by_id_safe $copiatCheieId getPrice2] doubleValue]
set pret_sild [[product_by_id_safe $sildId getPrice1] doubleValue]
set montare_sild [[product_by_id_safe $sildId getPrice2] doubleValue]
log "----2----"
set pret_vizor [[product_by_id_safe $vizorId getPrice1] doubleValue]
set montare_vizor [[product_by_id_safe $vizorId getPrice2] doubleValue]
log "----2.1----"
set pret_rozeta [[product_by_id_safe $rozetaId getPrice1] doubleValue]
set montare_rozeta [[product_by_id_safe $rozetaId getPrice2] doubleValue]
log "----2.2----"
set pret_maner [[product_by_id_safe $manerId getPrice1] doubleValue]
set montare_maner [[product_by_id_safe $manerId getPrice2] doubleValue]
log "----2.3----"
set pret_yalla1 [[product_by_id_safe $yalla1Id getPrice1] doubleValue]
set montare_yalla1 [[product_by_id_safe $yalla1Id getPrice2] doubleValue]
log "----3----"
set pret_yalla2 [[product_by_id_safe $yalla2Id getPrice1] doubleValue]
set montare_yalla2 [[product_by_id_safe $yalla2Id getPrice2] doubleValue]
set pret_baraAntipanica [[product_by_id_safe $baraAntipanicaId getPrice1] doubleValue]
set montare_baraAntipanica [[product_by_id_safe $baraAntipanicaId getPrice2] doubleValue]
set pret_selectorOrdine [[product_by_id_safe $selectorOrdineId getPrice1] doubleValue]
set montare_selectorOrdine [[product_by_id_safe $selectorOrdineId getPrice2] doubleValue]
set pret_amortizor [[product_by_id_safe $amortizorId getPrice1] doubleValue]
set montare_amortizor [[product_by_id_safe $amortizorId getPrice2] doubleValue]
log "----4----"
set pret_decupareSistem [[product_by_id_safe $decupareSistemId getPrice1] doubleValue]
set montare_decupareSistem [[product_by_id_safe $decupareSistemId getPrice2] doubleValue]
set pret_manerSemicilindru [[product_by_id_safe $manerSemicilindruId getPrice1] doubleValue]
set montare_manerSemicilindru [[product_by_id_safe $manerSemicilindruId getPrice2] doubleValue]
set pret_alteSisteme1 [[product_by_id_safe $alteSisteme1Id getPrice1] doubleValue]
set montare_alteSisteme1 [[product_by_id_safe $alteSisteme1Id getPrice2] doubleValue]
set pret_alteSisteme2 [[product_by_id_safe $alteSisteme2Id getPrice1] doubleValue]
set montare_alteSisteme2 [[product_by_id_safe $alteSisteme2Id getPrice2] doubleValue]

log "----end preturi sisteme----"

# Pentru usile UA STAS folosesc pret fix si nu pret pe suprafata
if {[string equal -nocase $version "UA STAS"]} {
    log "UA STAS, pret fix = $pret_versiune"
    set price_expr "$pret_versiune + $pret_deschidere + $pret_parteDeschidere + $pret_pozitionareFoaie"
} elseif {[string equal -nocase -length 15 $version "UA DISTRIBUITOR"]} {
    log "UA DISTRIBUITOR, pret fix = $pret_versiune"
    set price_expr "$pret_versiune + $se * $pret_material + $pret_deschidere + $pret_parteDeschidere + $pret_pozitionareFoaie"
} else {
    log "Nu e UA STAS, calculez pret pe metrul patrat"
    set price_expr "$se * ($pret_subcod + $pret_versiune + $pret_material + $pret_foaieInterioara + $pret_foaieExterioara + $pret_foaieInterioaraSecundara + $pret_foaieExterioaraSecundara + $pret_izolatie) + $pret_deschidere + $pret_parteDeschidere + $pret_pozitionareFoaie"
}
set sellPrice [expr $price_expr]

log "price1: $price_expr"
log "sellPrice = $sellPrice"

set pret_toc  [[product_by_code_safe 11007 $frameType getPrice1] doubleValue]
set pret_prag [[product_by_code_safe 11009 $tresholdType getPrice1] doubleValue]

set price_expr "$sellPrice + $pret_toc + $pret_prag"
log "price2: $price_expr"

set sellPrice [expr $price_expr]
log "sellPrice = $sellPrice"

set val_decupareSistem [expr $pret_decupareSistem + $montare_decupareSistem]
set price_expr "$sellPrice + $val_decupareSistem"
log "price3 v1: $price_expr"

set sellPrice [expr $price_expr]
log "sellPrice = $sellPrice"

if {$montareSistem == 1} {
    set cmnt 1 ;# se va adauga montajul la pret
} else {
    set cmnt 0 ;# nu adaug montajul la pret
}


set val_broasca [expr ($pret_broasca + $cmnt * $montare_broasca) * $broascaBuc]
set val_cilindru [expr ($pret_cilindru + $cmnt* $montare_cilindru) * $cilindruBuc]
set val_copiatCheie [expr ($pret_copiatCheie + $cmnt * $montare_copiatCheie) * $copiatCheieBuc]
set val_sild [expr ($pret_sild + $cmnt * $montare_sild) * $sildBuc]
set val_rozeta [expr ($pret_rozeta + $cmnt * $montare_rozeta) * $rozetaBuc]
set val_maner [expr ($pret_maner + $cmnt * $montare_maner) * $manerBuc]
set val_yalla1 [expr ($pret_yalla1 + $cmnt * $montare_yalla1) * $yalla1Buc]
set val_yalla2 [expr ($pret_yalla2 + $cmnt * $montare_yalla2) * $yalla2Buc]
set val_baraAntipanica [expr ($pret_baraAntipanica + $cmnt * $montare_baraAntipanica) * $baraAntipanicaBuc]
set val_selectorOrdine [expr ($pret_selectorOrdine + $cmnt * $montare_selectorOrdine) * $selectorOrdineBuc]
set val_amortizor [expr ($pret_amortizor + $cmnt * $montare_amortizor) * $amortizorBuc]
set val_manerSemicilindru [expr ($pret_manerSemicilindru + $cmnt * $montare_manerSemicilindru) * $manerSemicilindruBuc]
set val_vizor [expr ($pret_vizor + $cmnt * $montare_vizor) * $vizorBuc]
set val_alteSisteme1 [expr ($pret_alteSisteme1 + $cmnt * $montare_alteSisteme1) * $alteSisteme1Buc]
set val_alteSisteme2 [expr ($pret_alteSisteme2 + $cmnt * $montare_alteSisteme2) * $alteSisteme2Buc]

set val_tipBroascaBeneficiar [expr $pret_tipBroascaBeneficiar * $benefBroascaBuc]
set val_tipCilindruBeneficiar [expr $pret_tipCilindruBeneficiar * $benefCilindruBuc]
set val_tipSildBeneficiar [expr $pret_tipSildBeneficiar * $benefSildBuc]
set val_tipYallaBeneficiar [expr $pret_tipYallaBeneficiar * $benefYallaBuc]
set val_tipVizorBeneficiar  [expr $pret_tipVizorBeneficiar * $benefVizorBuc]
set val_tipBaraAntipanicaBeneficiar [expr $pret_tipBaraAntipanicaBeneficiar * $benefBaraAntipanicaBuc]
set val_tipManerBeneficiar  [expr $pret_tipManerBeneficiar * $benefManerBuc]
set val_tipSelectorOrdineBeneficiar  [expr $pret_tipSelectorOrdineBeneficiar * $benefSelectorOrdineBuc]
set val_tipAmortizorBeneficiar  [expr $pret_tipAmortizorBeneficiar * $benefAmortizorBuc]
set val_tipAlteSisteme1Beneficiar  [expr $pret_tipAlteSisteme1Beneficiar * $benefAlteSisteme1Buc]
set val_tipAlteSisteme2Beneficiar  [expr $pret_tipAlteSisteme2Beneficiar * $benefAlteSisteme2Buc]

set price_expr "$sellPrice + $val_tipBroascaBeneficiar + $val_tipCilindruBeneficiar + $val_tipSildBeneficiar + $val_tipYallaBeneficiar + $val_tipVizorBeneficiar + $val_tipBaraAntipanicaBeneficiar + $val_tipManerBeneficiar + $val_tipSelectorOrdineBeneficiar + $val_tipAmortizorBeneficiar + $val_tipAlteSisteme1Beneficiar + $val_tipAlteSisteme2Beneficiar + $val_broasca + $val_cilindru + $val_copiatCheie + $val_sild + $val_rozeta + $val_maner + $val_yalla1 + $val_yalla2 + $val_baraAntipanica + $val_selectorOrdine + $val_amortizor + $val_manerSemicilindru + $val_vizor + $val_alteSisteme1 + $val_alteSisteme2"
log "price3 v2: $price_expr"

set sellPrice [expr $price_expr]
log "sellPrice = $sellPrice"



# masca, lacrimarul, bolturile, platbanda
set pret_masca [[product_by_code 11140 $masca getPrice1] doubleValue]
set pret_lacrimar [[product_by_code 11145 $lacrimar getPrice1] doubleValue]
if {$k == 1} {
    set pret_bolturi [[product_by_code 11150 $bolturi getPrice3] doubleValue]
} elseif { $k == 2 } {
    set pret_bolturi [[product_by_code 11150 $bolturi getPrice4] doubleValue]
} else {
    set pret_bolturi 0
    log "Warning: numar canate necunoscut $k"
}
set pret_platbanda [[product_by_code 11155 $platbanda getPrice1] doubleValue]

set price_expr "$sellPrice + $pret_masca * (2 * $he + $le) /1000 + $pret_lacrimar * $lFoaie / 1000 + $pret_bolturi + $pret_platbanda"
log "price4: $price_expr"
set sellPrice [expr $price_expr]
log "sellPrice = $sellPrice"




# adaug pretul optiunilor
set optiuni [$logic getOptions]
for {set i [$optiuni iterator]} {[$i hasNext]} {} {
    set optiune [java::cast ProductLocal [$i next]]
    log "optiune este $optiune"
    set o_price [[$optiune getPrice1] doubleValue]
    log "pret optiune: $o_price"
    set sellPrice [expr $sellPrice + $o_price]
    log "sellPrice = $sellPrice"
}



log ">>>>>>>>>>>>>>>>>>>>Preturi finisaje"
log "blat"
set sellPrice [expr $sellPrice + $suprafataBlat * [[product_by_id_safe $intFinisajBlatId getPrice1] doubleValue]]
log "sellPrice = $sellPrice"

log "toc"
set sellPrice [expr $sellPrice + $suprafataToc * [[product_by_id_safe $intFinisajTocId getPrice1] doubleValue]]
log "sellPrice = $sellPrice"

log "grilaj"
set sellPrice [expr $sellPrice + $suprafataGrilaj * [[product_by_id_safe $intFinisajGrilajId getPrice1] doubleValue]]
log "sellPrice = $sellPrice"

log "fereastra"
set sellPrice [expr $sellPrice + $suprafataFereastra * [[product_by_id_safe $intFinisajFereastraId getPrice1] doubleValue]]
log "sellPrice = $sellPrice"

log supralumina
set sellPrice [expr $sellPrice + $suprafataSupralumina * [[product_by_id_safe $intFinisajSupraluminaId getPrice1] doubleValue]]
log "sellPrice = $sellPrice"

log panou
set sellPrice [expr $sellPrice + $suprafataPanouLateral * [[product_by_id_safe $intFinisajPanouLateralId getPrice1] doubleValue]]
log "sellPrice = $sellPrice"

log blatExt
set pret_finisajBlatExt [[product_by_id_safe $extFinisajBlatId getPrice1] doubleValue]
set sellPrice [expr $sellPrice + $suprafataBlat * $pret_finisajBlatExt]
log "sellPrice = $sellPrice"

log tocExt
set pret_finisajTocExt [[product_by_id_safe $extFinisajTocId getPrice1] doubleValue]
set sellPrice [expr $sellPrice + $suprafataToc * $pret_finisajTocExt]
log "sellPrice = $sellPrice"

log grilajExt
set sellPrice [expr $sellPrice + $suprafataGrilaj * [[product_by_id_safe $extFinisajGrilajId getPrice1] doubleValue]]
log "sellPrice = $sellPrice"

log fereastraExt
set sellPrice [expr $sellPrice + $suprafataFereastra * [[product_by_id_safe $extFinisajFereastraId getPrice1] doubleValue]]
log "sellPrice = $sellPrice"

log supraluminaExt
set sellPrice [expr $sellPrice + $suprafataSupralumina * [[product_by_id_safe $extFinisajSupraluminaId getPrice1] doubleValue]]
log "sellPrice = $sellPrice"

log panouExt
set sellPrice [expr $sellPrice + $suprafataPanouLateral * [[product_by_id_safe $extFinisajPanouLateralId getPrice1] doubleValue]]
log "sellPrice = $sellPrice"

log "finisaj masca"
set sellPrice [expr $sellPrice + $pret_finisajTocExt * 97 * (2*$he + $le) / 1000 / 1000]
log "sellPrice = $sellPrice"

log "finisaj lacrimar"
set sellPrice [expr $sellPrice + $pret_finisajBlatExt * 70 * 2 * ($lFoaie + $lFoaieSec) / 1000 / 1000]
log "sellPrice = $sellPrice"

log "finisaj bolturi"
set sellPrice [expr $sellPrice + 0]
log "sellPrice = $sellPrice"

log "finisaj platbanda"
set sellPrice [expr $sellPrice + 0]
log "sellPrice = $sellPrice"


log ">>>>>>>>>>>>>>>>>>>>>>>computePrice.tcl finished"
