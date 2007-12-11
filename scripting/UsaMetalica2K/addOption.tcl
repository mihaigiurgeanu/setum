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


# TODO modificare id tabla cu cel din baza de date pentru
# tabla Lisa
set tablaLisa 408624195


proc validate_fereastra {option_id} {
    global logger
    $logger {log int Object} [java::field BasicLevel DEBUG] "Validare fereastra $option_id"

    global factory lFoaie hFoaie k lFoaieSec hFoaieSec
    global intFoil extFoil
    global response
    global tablaLisa


    # Incarc datele corespunzatoare ferestrei
    set fereastraBean [java::cast Fereastra [$factory remote "ejb/FereastraHome" [java::field FereastraHome class]]]
    $fereastraBean loadFormData $option_id
    set fereastra [$fereastraBean getForm]

    # Culeg datele care vor fi folosite in validare
    set lfereastra [$fereastra getLf]
    set hfereastra [$fereastra getHf]
    set canat_fereastra [$fereastra getCanat]
    set deschidere [$fereastra getDeschidere]
    set tip_grilaj [$fereastra getTipGrilaj]

    # Rulez validarea
    eval_rules Fereastra    ;# eval_rules este in commons.tcl
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
    global tablaLisa

    global response


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


    set lg_min  [attribute_dbl $grilajStasId Lg_min]
    set hs_min [attribute_dbl $grilajStasId Hs_min]
    set lg_max  [attribute_dbl $grilajStasId Lg_max]
    set hg_max  [attribute_dbl $grilajStasId Hg_max]
		    

    eval_rules Supralumina	;# din commons.tcl
}



proc validate_panouLateral {option_id} {

    global logger
    $logger {log int Object} [java::field BasicLevel DEBUG] "Validare PanouLateral $option_id"

    global factory lFoaie hFoaie k lFoaieSec hFoaieSec
    global intFoil extFoil
    global bFrame cFrame
    global he le
    global tablaLisa

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

    set bToc $bFrame
    set cToc $cFrame

    set lg_min  [attribute_dbl $grilajStasId Lg_min]
    set hpl_min [attribute_dbl $grilajStasId Hpl_min]
    set lg_max  [attribute_dbl $grilajStasId Lg_max]
    set hg_max  [attribute_dbl $grilajStasId Hg_max]


    eval_rules {Panou lateral}	;# din commons.tcl

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


