# Computes the values of calculated fields

package require java;

java::import org.objectweb.util.monolog.api.BasicLevel
java::import -package ro.kds.erp.data ProductLocal

source "$scripting_root/commons.tcl" ;# includ definitii comune


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

    log "read_prop $varname $objname $propname"
    if {[info exists obj]} {
	set buffer [[java::prop $obj $propname] doubleValue]
	log "$varname read: $buffer"
    } else {
	log "Object $objname not set. The $varname will be 0"
	set buffer 0
    }
}


# set name "$version$subclass$code"

if { $frameType == 1 } {
    set lFrame 90
    
    ;#if {$k == 1 } {set bFrame 35} else {set bFrame 45}

    if {[string match -nocase "*cf" $version]} {
	set bFrame 45
    } else {
	set bFrame 35
    }
    log "versiune <<$version>> => bToc: $bFrame "
    set cFrame 25
}

if { $tresholdType == 1 } {
    set h2Treshold 0
    set h1Treshold 0

    set lTreshold 90
    set hTreshold 15
    set cTreshold 25
}


if { $tresholdType == 2 } {
    set h2Treshold 0
    set h1Treshold 0
}
if { $tresholdType == 3 } {
    if { $tresholdSpace == 2} {
	set h1Treshold 0
	set h2Treshold 5
	set h1Treshold 0
	set hTreshold 0
    } elseif { $tresholdSpace == 1} {
	set h2Treshold 0
	set hTreshold 0
    }
}

log "lg: $lg"
log "hg: $hg"

set le [expr $lg - $lcorrection - 20]
log "le: $le"

set he [expr $hg - $hcorrection - 10 + $h1Treshold]
log "he: $he"

set se [expr ($le * $he)/1000000]
log "se: $se"

set luft 5
set lFoaie [expr $le - (2 * $bFrame + 2 * $luft)]


if {$ieFoil == 1} {
    set extFoil $intFoil
}

if { $k == 2 } {
    if {$kType == 1} {
	set lCurrent [expr ($le - (2*$bFrame + 3*$luft))/2]
	set lFoaie $lCurrent
	set lFoaieSec $lCurrent
    } else {
	set lFoaie $lCurrent
	set lFoaieSec [expr $le - (2*$bFrame + $lFoaie + 3*$luft)]
    }
}

set hFoaie [expr $he - ($bFrame + $hTreshold + $h1Treshold + $h2Treshold + 2*$luft)]


set lUtil [expr $le - 2 * ($bFrame + $cFrame)]
set hUtil [expr $he - ($bFrame + $cFrame + $hTreshold + $cTreshold + $h1Treshold + $h2Treshold)]

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
read_prop montare_broasca broasca price2
read_prop pret_cilindru cilindru price1
read_prop montare_cilindru cilindru price2
read_prop pret_copiatCheie copiatCheie price1
read_prop montare_copiatCheie copiatCheie price2
read_prop pret_sild sild price1
read_prop montare_sild sild price2
read_prop pret_vizor vizor price1
read_prop montare_vizor vizor price2
read_prop pret_rozeta rozeta price1
read_prop montare_rozeta rozeta price2
read_prop pret_maner maner price1
read_prop montare_maner maner price2
read_prop pret_yalla1 yalla1 price1
read_prop montare_yalla1 yalla1 price2
read_prop pret_yalla2 yalla2 price1
read_prop montare_yalla2 yalla2 price2
read_prop pret_baraAntipanica baraAntipanica price1
read_prop montare_baraAntipanica baraAntipanica price2
read_prop pret_selectorOrdine selectorOrdine price1
read_prop montare_selectorOrdine selectorOrdine price2
read_prop pret_amortizor amortizor price1
read_prop montare_amortizor amortizor price2
read_prop pret_decupareSistem decupareSistem price2 ;# manopera
read_prop pret_manerSemicilindru manerSemicilindru price1
read_prop montare_manerSemicilindru manerSemicilindru price2
read_prop pret_alteSisteme1 alteSisteme1 price1
read_prop montare_alteSisteme1 alteSisteme1 price2
read_prop pret_alteSisteme2 alteSisteme2 price1
read_prop montare_alteSisteme2 alteSisteme2 price2

set price_expr "$se * ($pret_subcod + $pret_versiune + $pret_material + $pret_foaieInterioara + $pret_foaieExterioara + $pret_foaieInterioaraSecundara + $pret_foaieExterioaraSecundara + $pret_izolatie) + $pret_deschidere + $pret_parteDeschidere + $pret_pozitionareFoaie"
set sellPrice [expr $price_expr]

log "price1: $price_expr"
log "sellPrice = $sellPrice"

set pret_toc  [[product_by_code 11007 $frameType getPrice1] doubleValue]
set pret_prag [[product_by_code 11009 $tresholdType getPrice1] doubleValue]

set price_expr "$sellPrice + $pret_toc + $pret_prag"
set sellPrice [expr $price_expr]

log "price2: $price_expr"
log "sellPrice = $sellPrice"

if {$montareSistem == 2} {
    set val_decupareSistem $pret_decupareSistem
    set price_expr "$sellPrice + $val_decupareSistem"
    set sellPrice [expr $price_expr]

    log "price3 v1: $price_expr"
    log "sellPrice = $sellPrice"

} else {
    set val_broasca [expr ($pret_broasca + $montare_broasca) * $broascaBuc]
    set val_cilindru [expr ($pret_cilindru + $montare_cilindru) * $cilindruBuc]
    set val_copiatCheie [expr ($pret_copiatCheie + $montare_copiatCheie) * $copiatCheieBuc]
    set val_sild [expr ($pret_sild + $montare_sild) * $sildBuc]
    set val_rozeta [expr ($pret_rozeta + $montare_rozeta) * $rozetaBuc]
    set val_maner [expr ($pret_maner + $montare_maner) * $manerBuc]
    set val_yalla1 [expr ($pret_yalla1 + $montare_yalla1) * $yalla1Buc]
    set val_yalla2 [expr ($pret_yalla2 + $montare_yalla2) * $yalla2Buc]
    set val_baraAntipanica [expr ($pret_baraAntipanica + $montare_baraAntipanica) * $baraAntipanicaBuc]
    set val_selectorOrdine [expr ($pret_selectorOrdine + $montare_selectorOrdine) * $selectorOrdineBuc]
    set val_amortizor [expr ($pret_amortizor + $montare_amortizor) * $amortizorBuc]
    set val_manerSemicilindru [expr ($pret_manerSemicilindru + $montare_manerSemicilindru) * $manerSemicilindruBuc]
    set val_vizor [expr ($pret_vizor + $montare_vizor) * $vizorBuc]
    set val_alteSisteme1 [expr ($pret_alteSisteme1 + $montare_alteSisteme1) * $alteSisteme1Buc]
    set val_alteSisteme2 [expr ($pret_alteSisteme2 + $montare_alteSisteme2) * $alteSisteme2Buc]
    
    set val_tipBroascaBeneficiar [expr $pret_tipBroascaBeneficiar * $benefBroascaBuc]
    set val_tipCilindruBeneficiar [expr $pret_tipCilindruBeneficiar * $benefCilindruBuc]
    set val_tipSildBeneficiar [expr $pret_tipSildBeneficiar * $benefSildBuc]
    set val_tipYallaBeneficiar [expr $pret_tipYallaBeneficiar * $benefYallaBuc]
    set val_tipBaraAntipanicaBeneficiar [expr $pret_tipBaraAntipanicaBeneficiar * $benefBaraAntipanicaBuc]

    set price_expr "$sellPrice + $val_tipBroascaBeneficiar + $val_tipCilindruBeneficiar + $val_tipSildBeneficiar + $val_tipYallaBeneficiar + $val_tipBaraAntipanicaBeneficiar + $val_broasca + $val_cilindru + $val_copiatCheie + $val_sild + $val_rozeta + $val_maner + $val_yalla1 + $val_yalla2 + $val_baraAntipanica + $val_selectorOrdine + $val_amortizor + $val_manerSemicilindru + $val_vizor + $val_alteSisteme1 + $val_alteSisteme2"
    set sellPrice [expr $price_expr]

    log "price3 v2: $price_expr"
    log "sellPrice = $sellPrice"
}


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


set suprafataBlat [expr 2 * $lFoaie * $hFoaie / 1000 / 1000]

set ldesfToc [expr ($lFrame + 2 * ($bFrame + $cFrame) + 30)/1000]
set ldesfPrag [expr ($lTreshold + 2 * ($hTreshold + $cTreshold) + 30)/1000]
set suprafatToc [expr ($ldesfToc * 2 * ($he + $le) + $ldesfPrag * $le)/1000]

set suprafataGrilaj 0
set suprafataFereastra 0
set suprafataSupralumina 0
set suprafataPanouLateral 0
set suprafataGrilaVentilatie 0

set lplTotal 0
set hsTotal 0

# adaug pretul optiunilor
set optiuni [$logic getOptions]
for {set i [$optiuni iterator]} {[$i hasNext]} {} {
    set optiune [java::cast ProductLocal [$i next]]
    log "optiune este $optiune"
    set o_price [[$optiune getPrice1] doubleValue]
    log "pret optiune: $o_price"
    set sellPrice [expr $sellPrice + $o_price]
    log "sellPrice = $sellPrice"

    ;# calcul suprafete
    set opt_id [$optiune getId]
    log "opt_id $opt_id"
    set category [attribute_str $opt_id businessCategory]
    log "category $category"
    if {[string equal $category "http://www.kds.ro/erp/businessCategory/setum/optiuni/fereastra"]} {
	set lf [expr [attribute_dbl $opt_id lf] / 1000]
	set hf [expr [attribute_dbl $opt_id hf] / 1000]
	set quantity [attribute_int $opt_id quantity]
	set suprafataFereastra [expr $suprafataFereastra + 0.120 * $lf * $hf * $quantity]
	log "suprafataFereastra $suprafataFereastra"

	set tipGrilaj [attribute_int $opt_id tipGrilaj]
	set grilajStasId [attribute_int $opt_id grilajStas]
	if {$tipGrilaj == 2 || $grilajStasId != 0} {
	    set suprafataGrilaj [expr $suprafataGrilaj + $lf * $hf * 1.3 * $quantity]
	}
	log "suprafataGrilaj $suprafataGrilaj"
    } elseif {[string equal $category "http://www.kds.ro/erp/businessCategory/setum/optiuni/gv"]} {
	set lgv [expr [attribute_dbl $opt_id lgv] / 1000]
	set hgv [expr [attribute_dbl $opt_id hgv] / 1000]
	set quantity [attribute_int $opt_id quantity]
	set suprafataGrilaVentilatie [expr $suprafataGrilaVentilatie + $lgv * $hgv * 1.3 * $quantity]
    } elseif {[string equal $category "http://www.kds.ro/erp/businessCategory/setum/optiuni/ga"]} {

    } elseif {[string equal $category "http://www.kds.ro/erp/businessCategory/setum/optiuni/supralumina"]} {
	set ls [expr [attribute_dbl $opt_id ls] / 1000]
	set hsmm [attribute_dbl $opt_id hs]
	set hs [expr $hsmm / 1000]
	set quantity [attribute_int $opt_id quantity]
	set hsTotal [expr $hsTotal + $hsmm * $quantity]
	set suprafataSupralumina [expr $suprafataSupralumina + $ldesfToc * ($ls + $hs) * $quantity]
	set tipTabla [attribute_int $opt_id tipTabla]
	if {$tipTabla != 0} {
	    set suprafataSupralumina [expr $suprafataSupralumina + $ls * $hs * $quantity]
	}

	set tipGrilaj [attribute_int $opt_id tipGrilaj]
	set grilajStasId [attribute_int $opt_id grilajStas]
	if {$tipGrilaj == 2 || $grilajStasId != 0} {
	    set suprafataGrilaj [expr $suprafataGrilaj + $ls * $hs * 1.3 * $quantity]
	}
    } elseif {[string equal $category "http://www.kds.ro/erp/businessCategory/setum/optiuni/panoulateral"]} {
	set lpmm [attribute_dbl $opt_id lpl]
	set lp [expr $lpmm / 1000]
	set hpmm [attribute_dbl $opt_id hpl]
	set hp [expr $hpmm / 1000]
	set quantity [attribute_int $opt_id quantity]
	set lplTotal [expr $lplTotal + $lpmm]
	set tipTabla [attribute_int $opt_id tipTabla]
	set suprafataPanouLateral [expr $suprafataPanouLateral + $ldesfToc * ($lp + $hp) * $quantity]
	if {$tipTabla != 0} {
	    set suprafataPanouLateral [expr $suprafataPanouLateral + $lp*$hp*$quantity]
	}

	set tipGrilaj [attribute_int $opt_id tipGrilaj]
	set grilajStasId [attribute_int $opt_id grilajStas]
	if {$tipGrilaj == 2 || $grilajStasId != 0} {
	    set suprafataGrilaj [expr $suprafataGrilaj + $lp * $hp * 1.3 * $quantity]
	}
    }
}

set hexec [expr $he + $hsTotal]
set lexec [expr $le + $lplTotal]



# finisaje
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

log ">>>>>>>>>>>>>>>>>>>>Preturi finisaje"
log "blat $sellPrice"
set sellPrice [expr $sellPrice + $suprafataBlat * [[product_by_id_safe $intFinisajBlatId getPrice1] doubleValue]]

log "toc $sellPrice"
set sellPrice [expr $sellPrice + $suprafataToc * [[product_by_id_safe $intFinisajTocId getPrice1] doubleValue]]

log "grilaj $sellPrice"
set sellPrice [expr $sellPrice + $suprafataGrilaj * [[product_by_id_safe $intFinisajGrilajId getPrice1] doubleValue]]

log "fereastra $sellPrice"
set sellPrice [expr $sellPrice + $suprafataFereastra * [[product_by_id_safe $intFinisajFereastraId getPrice1] doubleValue]]

log supralumina
set sellPrice [expr $sellPrice + $suprafataSupralumina * [[product_by_id_safe $intFinisajSupraluminaId getPrice1] doubleValue]]

log panou
set sellPrice [expr $sellPrice + $suprafataPanouLateral * [[product_by_id_safe $intFinisajPanouLateralId getPrice1] doubleValue]]

log blatExt
set pret_finisajBlatExt [[product_by_id_safe $extFinisajBlatId getPrice1] doubleValue]
set sellPrice [expr $sellPrice + $suprafataBlat * $pret_finisajBlatExt]
log tocExt
set pret_finisajTocExt [[product_by_id_safe $extFinisajTocId getPrice1] doubleValue]
set sellPrice [expr $sellPrice + $suprafataToc * $pret_finisajTocExt]
log grilajExt
set sellPrice [expr $sellPrice + $suprafataGrilaj * [[product_by_id_safe $extFinisajGrilajId getPrice1] doubleValue]]
log fereastraExt
set sellPrice [expr $sellPrice + $suprafataFereastra * [[product_by_id_safe $extFinisajFereastraId getPrice1] doubleValue]]
log supraluminaExt
set sellPrice [expr $sellPrice + $suprafataSupralumina * [[product_by_id_safe $extFinisajSupraluminaId getPrice1] doubleValue]]
log panouExt
set sellPrice [expr $sellPrice + $suprafataPanouLateral * [[product_by_id_safe $extFinisajPanouLateralId getPrice1] doubleValue]]

log "finisaj masca"
set sellPrice [expr $sellPrice + $pret_finisajTocExt * 97 * (2*$he + $le) / 1000 / 1000]

log "finisaj lacrimar"
set sellPrice [expr $sellPrice + $pret_finisajBlatExt * 70 * 2 * ($lFoaie + $lFoaieSec) / 1000 / 1000]

log "finisaj bolturi"
set sellPrice [expr $sellPrice + 0]

log "finisaj platbanda"
set sellPrice [expr $sellPrice + 0]


log ">>>>>>>>>>>>>>>>>>>>>>>UsaMetalica2K_calculatedFields.tcl finished"