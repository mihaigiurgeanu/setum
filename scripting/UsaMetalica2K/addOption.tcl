# addOption.tcl
# Script apelat la adaugarea unei optiuni (fereastra, panou-lateral, etc.). Are rolul de a valida optiunea in cadrul usi metalice.
# Parametrii:
#	param_optionId
#	param_businessCategory


package require java
java::import -package ro.kds.erp.biz.setum.basic Fereastra FereastraHome FereastraForm
java::import -package ro.kds.erp.biz.setum.basic PanouLateral PanouLateralHome PanouLateralForm

java::import -package ro.kds.erp.biz ResponseBean
java::import -package org.objectweb.util.monolog.api Level BasicLevel



set rule_crt 0
proc rule { rule_no } {
    global logger rule_crt

    set rule_crt [expr $rule_crt + 1]
    $logger {log int Object} [java::field BasicLevel DEBUG] "Regula: $rule_crt. -- $rule_no"
}


proc validate_fereastra {option_id} {
    global logger
    $logger {log int Object} [java::field BasicLevel DEBUG] "Validare fereastra $option_id"





    global factory lFoaie hFoaie k lFoaieSec hFoaieSec
    global intFoil extFoil
    global response




    set fereastraBean [java::cast Fereastra [$factory remote "ejb/FereastraHome" [java::field FereastraHome class]]]
    $fereastraBean loadFormData $option_id
    set fereastra [$fereastraBean getForm]

    # Culeg datele care vor fi folosite in validare
    set lfereastra [$fereastra getLf]
    set hfereastra [$fereastra getHf]
    set canat_fereastra [$fereastra getCanat]
    set deschidere [$fereastra getDeschidere]
    set tip_grilaj [$fereastra getTipGrilaj]

    rule F1
    if { $canat_fereastra == 1 && 
	 $hfereastra >= ($hFoaie - 1120) &&
	 $lfereastra <= ($lFoaie - 380) } {
	
	// este bine
    }


    rule F2
    if { $canat_fereastra == 1 &&
	 $hfereastra >= ($hFoaie - 1120) &&
	 $lfereastra <= ($lFoaie - 340) } {


	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message/Fereastra\#exceptie_silduri" \
	    {KZS M/M, 6000 M/M, rozeta/maner CISA M/M}
    }


    rule F3
    if { $canat_fereastra == 1 && 
	 $hfereastra >= ($hFoaie - 1120) &&
	 $lfereastra > ($lFoaie - 340) &&
	 $lfereastra <= ($lFoaie - 310) } {

	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message/Fereastra\#restrictie_silduri" \
	    {URBIS NFP M/M, NP M/M}
    }

    rule F4
    if { $canat_fereastra == 1 &&
	 $hfereastra >= ($hFoaie - 1120) &&
	 $lfereastra > ($lFoaie - 310) } {

	
	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "[expr $lFoaie - 310] mm"
	
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    
    rule F5
    if { $canat_fereastra == 1 &&
	 $hfereastra < ($hFoaie - 1120) &&
	 $lfereastra > ($lFoaie - 250)} {


	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "[expr $lFoaie - 250] mm"
	
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F6
    if { $canat_fereastra == 2 &&
	 $k == 1 } {

	$response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/Fereastra\#doar_un_canat"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F7
    if { $canat_fereastra == 2 &&
	 $k == 2 &&
	 $lfereastra > $lFoaieSec - 120 } {

	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "[expr $lFoaieSec - 120] mm"
	
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F8
    if {$hfereastra > ($hFoaie - 310) } {
	$response addValidationInfo \
	    [java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "[expr $hFoaie - 310] mm"
	
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }


    rule F9
    if { $deschidere == 1 &&	
	 $tip_grilaj == 0 &&	
	 $intFoil == 1 &&	
	 $extFoil == 1 &&	
	 $lfereastra < 140 } {
	
				    
	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "140 mm"
	
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }


    rule F10
    if { 
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 1 &&	
	$extFoil == 1 &&	
	$hfereastra < 350 
    } {
	
				    
	$response addValidationInfo \
	    [java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "350 mm"
	
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }


    rule F11
    if { 
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 && $extFoil == 2		
    } { 
	$response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/messsage/Fereastra#atentionare_amprente"
    }

    rule F12
    if { 
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 && $extFoil == 2 &&	
	$lfereastra != 140
    } {

	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#exact" \
	    "140 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }


    rule F13
    if { 
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 && $extFoil == 2 &&	
	$hfereastra < 350
    } {

	$response addValidationInfo \
	    [java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "350 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F14
    if { 
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 && $extFoil == 2 &&	
	$hfereastra > 1610
    } {

	$response addValidationInfo \
	    [java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "1610 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F15
    if { 
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 && $extFoil == 2 &&	
	$lfereastra < 290
    } {

	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "290 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F16
    if { 
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 && $extFoil == 2 &&	
	$hfereastra < 430
    } {

	$response addValidationInfo \
	    [java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "430 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F17
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 && $extFoil == 2 &&	
	$canat_fereastra == 1 &&		
	$lfereastra > 640
    } {
	
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "640 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F18
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 && $extFoil == 2 &&	
	$canat_fereastra == 2 &&		
	$lfereastra > 640
    } {
	
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "640 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F19
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 && $extFoil == 2 &&	
	$hfereastra > 1690
    } {
	
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "1690 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }


    rule F20
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$lfereastra < 262
    } {
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "262 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F21
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$hfereastra < 362
    } {
	[java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "362 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F22
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$canat_fereastra == 1 && 
	$lfereastra > 462
    } {
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "462 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F23
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$canat_fereastra == 2 && 
	$lfereastra > 462
    } {
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "462 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F24		
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$hfereastra > 1382
    } {
	[java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "1382 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F25
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 &&	 
	$extFoil == 2 &&	
	$lfereastra <> 262
    } {
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#exact" \
	    "262 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F26
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 &&	 
	$extFoil == 2 &&	
	$hfereastra <> 362
    } {
	[java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#exact" \
	    "362 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F27
    if {
	$deschidere == 2 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$lfereastra < 350
    } {
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "350 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F28
    if {
	$deschidere == 2 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$hfereastra < 350
    } {
	[java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "350 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F29
    if {
	$deschidere == 2 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 &&	 
	$extFoil == 2 &&	
	$lfereastra < 570
    } {
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "570 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F30
    if {
	$deschidere == 2 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 &&	 
	$extFoil == 2 &&	
	$hfereastra < 380
    } {
	[java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "380 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F31
    if {
	$deschidere == 2 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 &&	 
	$extFoil == 2 &&	
	$lfereastra > 570
    } {
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "570 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F32
    if {
	$deschidere == 2 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 &&	 
	$extFoil == 2 &&	
	$hfereastra > 1220
    } {
	[java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "380 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F33
    if {
	$deschidere == 2 &&	
	$tip_grilaj != 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$lfereastra < 462
    } {
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "462 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F34
    if {
	$deschidere == 2 &&	
	$tip_grilaj != 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$hfereastra < 382
    } {
	[java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "382 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F35
    if {
	$deschidere == 2 &&	
	$tip_grilaj != 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$lfereastra > 462
    } {
	[java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "462 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F36
    if {
	$deschidere == 2 &&	
	$tip_grilaj != 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$hfereastra > 1402
    } {
	[java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "1402 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F37
    if {
	$deschidere == 2 &&
	$tip_grilaj != 0 &&
	$intFoil == 2 &&
	$extFoil == 2
    } {
	[java::call FereastraForm uri deschidere] \
	    "http://www.kds.ro/readybeans/rdf/validation/message/Fereastra#nu_se_executa" \
	    "mobila cu grilaj"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F38
    if {
	$deschidere == 2 &&
	$tip_grilaj == 0 &&
	$intFoil == 2 &&
	$extFoil == 2
    } {
	[java::call FereastraForm uri deschidere] \
	    "http://www.kds.ro/readybeans/rdf/validation/message/Fereastra#atentionare_zona_2" \
	    "mobila cu grilaj"
    }







    rule FINISH
}

proc validate_gv {option_id} {
}

proc validate_ga {option_id} {
}

proc validate_supralumina {option_id} {
}

proc validate_panouLateral {option_id} {

    global logger
    $logger {log int Object} [java::field BasicLevel DEBUG] "Validare PanouLateral $option_id"





    global factory lFoaie hFoaie k lFoaieSec hFoaieSec
    global intFoil extFoil
    global bToc cToc

    global response




    set plBean [java::cast PanouLateral [$factory remote "ejb/PanouLateralHome" [java::field PanouLateralHome class]]]
    $plBean loadFormData $option_id
    set pl [$plBean getForm]


    set hpl [$pl getHpl]
    set lpl [$pl getLpl]
    set cells [$pl getCells]
    set deschidere [$pl getDeschidere]
    set sensDeschidere [$pl getSensDeschidere]
    set pozitionareBalamale [$pl getPozitionareBalamale]
    set componenta [$pl getComponenta]
    set tipComponenta [$pl getTipComponenta]
    set tipGeam [$pl getTipGeam]
    set geamSimpluId [$pl getGeamSimpluId]
    set geamTermopanId [$pl getGeamTermopanId]
    set tipGrilaj [$pl getTipGrilaj]
    set grilajStasId [$pl getGrilajStasId]
    set valoareGrilajAtipic [$pl getValoareGrilajAtipic]
    set tipTabla [$pl getTipTabla]
    set tablaId [$pl getTablaId]
    set sellPrice [$pl getSellPrice]
    set entryPrice [$pl getEntryPrice]
    set price1 [$pl getPrice1]
    set businessCategory [$pl getBusinessCategory]
    set quantity [$pl getQuantity]
    



    rule PL1
    # TODO implementare regula PL1

    rule PL2
    # TODO implementare regula PL2

    rule PL4
    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$lpl < (100 + 2 * ($bToc + $cToc))
    } {

	$response addValidationInfo \
	    [java::call PanouLateralForm uri lpl] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "[expr 100 + 2 * ($bToc + $cToc)] mm"
	
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }
	
}






# # Gasesc produsul dupa id (care este param_optionId) pentru a lua categoria lui.
# # In functi de categorie, stiu ce fel de optiune este (fereastra, panaou lateral, etc)
# set categoryId [[[[srv findProductById $param_optionId] getCategory] getId] intValue]


puts "Validare optiune: $param_businessCategory"
switch $param_businessCategory {


    http://www.kds.ro/erp/businessCategory/setum/optiuni/fereastra {
	validate_fereastra	$param_optionId
    }

    http://www.kds.ro/erp/businessCategory/setum/optiuni/gv {
	validate_gv		$param_optionId
    }

    http://www.kds.ro/erp/businessCategory/setum/optiuni/ga {
	validate_ga		$param_optionId
    }

    http://www.kds.ro/erp/businessCategory/setum/optiuni/supralumina {
	validate_supralumina	$param_optionId
    }

    http://www.kds.ro/erp/businessCategory/setum/optiuni/panoulateral {
	validate_panouLateral	$param_optionId
    }
    
    default {
	puts "Categorie de business necunoscuta a produsului"
    }
}


