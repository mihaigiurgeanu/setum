# addOption.tcl
# Script apelat la adaugarea unei optiuni (fereastra, panou-lateral, etc.). Are rolul de a valida optiunea in cadrul usi metalice.
# Parametrii:
#	param_optionId
#	param_businessCategory


source "$scripting_root/commons.tcl" ;# includ definitii comune

package require java
java::import -package ro.kds.erp.biz.setum.basic Fereastra FereastraHome FereastraForm
java::import -package ro.kds.erp.biz.setum.basic PanouLateral PanouLateralHome PanouLateralForm
java::import -package ro.kds.erp.biz.setum.basic Supralumina SupraluminaHome SupraluminaForm

java::import -package ro.kds.erp.biz ResponseBean
java::import -package org.objectweb.util.monolog.api Level BasicLevel



set rule_crt 0
proc rule { rule_no } {
    global logger rule_crt

    set rule_crt [expr $rule_crt + 1]
    $logger {log int Object} [java::field BasicLevel DEBUG] "Regula: $rule_crt. -- $rule_no"
}


# helper function that puts a validation error in the
# synopsis
#   global response
#   set form_class FereastraForm
#   set base_msg_url "http://www.kds.ro/readybeans/rdf/validation/message/Fereastra\#"
#
#   verr deschidere nu_se_executa "mobila cu grilaj"
proc verr { field_name url_msg err_detail } {

    upvar response response form_class form_class base_msg_url base_msg_url
    $response addValidationInfo \
	[java::call $form_class uri $field_name] \
	"$base_msg_url$url_msg" \
	$err_detail
    $response setCode [java::field ResponseBean CODE_ERR_VALIDATION]

}
# helper function that puts an warning message in the response
# is the same as verr, but does not add the error response code
proc vwarn { form_class field_name url_msg warn_detail } {
    upvar response response form_class form_class base_msg_url base_msg_url
    $response addValidationInfo \
	[java::call $form_class uri $field_name] \
	"$base_msg_url$url_msg" \
	$err_detail
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
	$lfereastra != 290
    } {

	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#exact" \
	    "290 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F16
    if { 
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 2 && $extFoil == 2 &&	
	$hfereastra != 430
    } {

	$response addValidationInfo \
	    [java::call FereastraForm uri hf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#exact" \
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
	
	$response addValidationInfo \
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
	
	$response addValidationInfo \
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
	$response addValidationInfo \
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
	$lfereastra > 442
    } {
	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "442 mm"
	$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
    }

    rule F23
    if {
	$deschidere == 1 &&	
	$tip_grilaj == 0 &&	
	$intFoil == 1 &&	 
	$extFoil == 1 &&	
	$canat_fereastra == 2 && 
	$lfereastra > 442
    } {
	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "442 mm"
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
	$response addValidationInfo \
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
	$lfereastra != 262
    } {
	$response addValidationInfo \
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
	$hfereastra != 362
    } {
	$response addValidationInfo \
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
	$response addValidationInfo \
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
	$response addValidationInfo \
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
	$response addValidationInfo \
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
	$response addValidationInfo \
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
	$response addValidationInfo \
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
	$response addValidationInfo \
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
	$lfereastra < 442
    } {
	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#min" \
	    "442 mm"
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
	$response addValidationInfo \
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
	$lfereastra > 442
    } {
	$response addValidationInfo \
	    [java::call FereastraForm uri lf] \
	    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
	    "442 mm"
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
	$response addValidationInfo \
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
	$response addValidationInfo \
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
	$response addValidationInfo \
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

    global logger
    $logger {log int Object} [java::field BasicLevel DEBUG] "Validare Supralumina $option_id"





    global factory lFoaie hFoaie k lFoaieSec hFoaieSec
    global intFoil extFoil
    global bFrame cFrame
    global he le

    global response


    set form_class SupraluminaForm
    set base_msg_url "http://www.kds.ro/readybeans/rdf/validation/message/Supralumina\#"



    set sBean [java::cast Supralumina [$factory remote "ejb/SupraluminaHome" [java::field SupraluminaHome class]]]
    $sBean loadFormData $option_id
    set s [$sBean getForm]


    set hs [$s getHs]
    set ls [$s getLs]

    set deschidere [$s getDeschidere]
    set sensDeschidere [$s getSensDeschidere]
    set pozitionareBalamale [$s getPozitionareBalamale]
    set componenta [$s getComponenta]
    set tipComponenta [$s getTipComponenta]
    set tipGeam [$s getTipGeam]
    set geamSimpluId [$s getGeamSimpluId]
    set geamTermopanId [$s getGeamTermopanId]
    set tipGrilaj [$s getTipGrilaj]
    set grilajStasId [$s getGrilajStasId]
    set valoareGrilajAtipic [$s getValoareGrilajAtipic]
    set tipTabla [$s getTipTabla]
    set tablaId [$s getTablaId]
    set sellPrice [$s getSellPrice]
    set entryPrice [$s getEntryPrice]
    set price1 [$s getPrice1]
    set businessCategory [$s getBusinessCategory]
    set quantity [$s getQuantity]

    set bToc $bFrame
    set cToc $cFrame


    rule S1

    rule S2

    rule S4
    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$ls < (100 + 2 * ($bToc + $cToc))
    } {

	verr ls min "[expr 100 + 2 * ($bToc + $cToc)] mm"
    }


    rule S5
    
    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$hs < (100 + 2 * ($bToc + $cToc))
    } {

	verr hs min "[expr 100 + 2 * ($bToc + $cToc)] mm"
    }


    rule S6
    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$ls > (1100 + 2 * ($bToc + $cToc))
    } {

	verr ls max "[expr 1100 + 2 * ($bToc + $cToc)] mm"
    }


    rule S7
    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$hs > $he
    } {

	verr hs max "$he mm"
    }


    # TODO modificare id tabla cu cel din baza de date pentru
    # tabla Lisa
    set tablaLisa 408624195

    rule S8
    if {
	$deschidere == 1 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$ls < (100 + 2 * ($bToc + $cToc))
    } {
	verr ls min "[expr 100 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule S9
    if {
	$deschidere == 1 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$hs < (100 + 2 * ($bToc + $cToc))
    } {
	verr hs min "[expr 100 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule S10
    if {
	$deschidere == 1 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$ls > (1100 + 2 * ($bToc + $cToc))
    } {
	verr ls max "[expr 1100 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule S11
    if {
	$deschidere == 1 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$hs > $he
    } {
	verr hs min "$he mm"
    }

    

    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj != 0 
    } {



	set lg_min  [attribute_dbl $grilajStasId Lg_min]
	set hs_min [attribute_dbl $grilajStasId Hs_min]
	set lg_max  [attribute_dbl $grilajStasId Lg_max]
	set hg_max  [attribute_dbl $grilajStasId Hg_max]
		    
	rule S12
	if { $ls < ($lg_min + 2 * $bToc) } {
	    verr ls min "[expr $lg_min + 2 * $bToc] mm"
	}

	rule S13
	if { $hs < ($hs_min + 2 * $bToc)} {
	    verr hs min "[expr $hs_min + 2 * $bToc] mm"
	}
		    
	rule S14
	if { $ls > ($lg_max + 2 * $bToc) } {
	    verr ls max "[expr $lg_max + 2 * $bToc] mm"
	}
	
	
	rule S15
	if { $hs > ($hg_max * 8 + 2 * $bToc) } {
	    verr hs max "[expr $hg_max * 8 + 2 * $bToc] mm"
	}


    }






    rule S31
    if {
	$deschidere == 2 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$ls < (270 + 2 * ($bToc + $cToc))
    } {

	verr ls min "[expr 270 + 2 * ($bToc + $cToc)] mm"
    }


    rule S32
    
    if {
	$deschidere == 2 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$hs < (270 + 2 * ($bToc + $cToc))
    } {

	verr hs min "[expr 270 + 2 * ($bToc + $cToc)] mm"
    }


    rule S33
    if {
	$deschidere == 2 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$ls > (1100 + 2 * ($bToc + $cToc))
    } {

	verr ls max "[expr 1100 + 2 * ($bToc + $cToc)] mm"
    }


    rule S34
    if {
	$deschidere == 2 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$hs > $he
    } {

	verr hs max "$he mm"
    }


    rule S35
    if {
	$deschidere == 2 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$ls < (270 + 2 * ($bToc + $cToc))
    } {
	verr ls min "[expr 270 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule S36
    if {
	$deschidere == 2 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$hs < (270 + 2 * ($bToc + $cToc))
    } {
	verr hs min "[expr 100 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule S37
    if {
	$deschidere == 2 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$ls > (1100 + 2 * ($bToc + $cToc))
    } {
	verr ls max "[expr 1100 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule S38
    if {
	$deschidere == 1 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$hs > $he
    } {
	verr hs min "$he mm"
    }

    

    if {
	$deschidere == 2 &&
	$tipGeam != 0 &&
	$tipGrilaj != 0 
    } {



	set lg_min  [attribute_dbl $grilajStasId Lg_min]
	set hs_min [attribute_dbl $grilajStasId Hs_min]
	set lg_max  [attribute_dbl $grilajStasId Lg_max]
	set hg_max  [attribute_dbl $grilajStasId Hg_max]
		    
	rule S39
	if { $ls < ($lg_min + 2 * $bToc) } {
	    verr ls min "[expr $lg_min + 2 * $bToc] mm"
	}

	rule S40
	if { $hs < ($hs_min + 2 * $bToc)} {
	    verr hs min "[expr $hs_min + 2 * $bToc] mm"
	}
		    
	rule S41
	if { $ls > ($lg_max + 2 * $bToc) } {
	    verr ls max "[expr $lg_max + 2 * $bToc] mm"
	}
	
	
	rule S42
	if { $hs > ($hg_max * 8 + 2 * $bToc) } {
	    verr hs max "[expr $hg_max * 8 + 2 * $bToc] mm"
	}


    }



}

proc validate_panouLateral {option_id} {

    global logger
    $logger {log int Object} [java::field BasicLevel DEBUG] "Validare PanouLateral $option_id"





    global factory lFoaie hFoaie k lFoaieSec hFoaieSec
    global intFoil extFoil
    global bFrame cFrame
    global he le

    global response


    set form_class PanouLateralForm
    set base_msg_url "http://www.kds.ro/readybeans/rdf/validation/message/PanouLateral\#"



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

    set bToc $bFrame
    set cToc $cFrame


    rule PL1

    rule PL2

    rule PL4
    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$lpl < (100 + 2 * ($bToc + $cToc))
    } {

	verr lpl min "[expr 100 + 2 * ($bToc + $cToc)] mm"
    }


    rule PL5
    
    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$hpl < (100 + 2 * ($bToc + $cToc))
    } {

	verr hpl min "[expr 100 + 2 * ($bToc + $cToc)] mm"
    }


    rule PL6
    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$lpl > (1100 + 2 * ($bToc + $cToc))
    } {

	verr lpl max "[expr 1100 + 2 * ($bToc + $cToc)] mm"
    }


    rule PL7
    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$hpl > $he
    } {

	verr hpl max "$he mm"
    }


    # TODO modificare id tabla cu cel din baza de date pentru
    # tabla Lisa
    set tablaLisa 408624195

    rule PL8
    if {
	$deschidere == 1 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$lpl < (100 + 2 * ($bToc + $cToc))
    } {
	verr lpl min "[expr 100 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule PL9
    if {
	$deschidere == 1 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$hpl < (100 + 2 * ($bToc + $cToc))
    } {
	verr hpl min "[expr 100 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule PL10
    if {
	$deschidere == 1 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$lpl > (1100 + 2 * ($bToc + $cToc))
    } {
	verr lpl max "[expr 1100 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule PL11
    if {
	$deschidere == 1 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$hpl > $he
    } {
	verr hpl min "$he mm"
    }

    

    if {
	$deschidere == 1 &&
	$tipGeam != 0 &&
	$tipGrilaj != 0 
    } {



	set lg_min  [attribute_dbl $grilajStasId Lg_min]
	set hpl_min [attribute_dbl $grilajStasId Hpl_min]
	set lg_max  [attribute_dbl $grilajStasId Lg_max]
	set hg_max  [attribute_dbl $grilajStasId Hg_max]
		    
	rule PL12
	if { $lpl < ($lg_min + 2 * $bToc) } {
	    verr lpl min "[expr $lg_min + 2 * $bToc] mm"
	}

	rule PL13
	if { $hpl < ($hpl_min + 2 * $bToc)} {
	    verr hpl min "[expr $hpl_min + 2 * $bToc] mm"
	}
		    
	rule PL14
	if { $lpl > ($lg_max + 2 * $bToc) } {
	    verr lpl max "[expr $lg_max + 2 * $bToc] mm"
	}
	
	
	rule PL15
	if { $hpl > ($hg_max * 8 + 2 * $bToc) } {
	    verr hpl max "[expr $hg_max * 8 + 2 * $bToc] mm"
	}


    }






    rule PL31
    if {
	$deschidere == 2 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$lpl < (270 + 2 * ($bToc + $cToc))
    } {

	verr lpl min "[expr 270 + 2 * ($bToc + $cToc)] mm"
    }


    rule PL32
    
    if {
	$deschidere == 2 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$hpl < (270 + 2 * ($bToc + $cToc))
    } {

	verr hpl min "[expr 270 + 2 * ($bToc + $cToc)] mm"
    }


    rule PL33
    if {
	$deschidere == 2 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$lpl > (1100 + 2 * ($bToc + $cToc))
    } {

	verr lpl max "[expr 1100 + 2 * ($bToc + $cToc)] mm"
    }


    rule PL34
    if {
	$deschidere == 2 &&
	$tipGeam != 0 &&
	$tipGrilaj == 0 &&
	$hpl > $he
    } {

	verr hpl max "$he mm"
    }


    rule PL35
    if {
	$deschidere == 2 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$lpl < (270 + 2 * ($bToc + $cToc))
    } {
	verr lpl min "[expr 270 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule PL36
    if {
	$deschidere == 2 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$hpl < (270 + 2 * ($bToc + $cToc))
    } {
	verr hpl min "[expr 100 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule PL37
    if {
	$deschidere == 2 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$lpl > (1100 + 2 * ($bToc + $cToc))
    } {
	verr lpl max "[expr 1100 + 2 * ($bToc + $cToc)] mm"
    }
    
    rule PL38
    if {
	$deschidere == 1 &&
	$tablaId == $tablaLisa &&
	$tipGrilaj == 0 &&
	$hpl > $he
    } {
	verr hpl min "$he mm"
    }

    

    if {
	$deschidere == 2 &&
	$tipGeam != 0 &&
	$tipGrilaj != 0 
    } {



	set lg_min  [attribute_dbl $grilajStasId Lg_min]
	set hpl_min [attribute_dbl $grilajStasId Hpl_min]
	set lg_max  [attribute_dbl $grilajStasId Lg_max]
	set hg_max  [attribute_dbl $grilajStasId Hg_max]
		    
	rule PL39
	if { $lpl < ($lg_min + 2 * $bToc) } {
	    verr lpl min "[expr $lg_min + 2 * $bToc] mm"
	}

	rule PL40
	if { $hpl < ($hpl_min + 2 * $bToc)} {
	    verr hpl min "[expr $hpl_min + 2 * $bToc] mm"
	}
		    
	rule PL41
	if { $lpl > ($lg_max + 2 * $bToc) } {
	    verr lpl max "[expr $lg_max + 2 * $bToc] mm"
	}
	
	
	rule PL42
	if { $hpl > ($hg_max * 8 + 2 * $bToc) } {
	    verr hpl max "[expr $hg_max * 8 + 2 * $bToc] mm"
	}


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


