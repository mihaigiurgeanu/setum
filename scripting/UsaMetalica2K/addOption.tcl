# addOption.tcl
# Script apelat la adaugarea unei optiuni (fereastra, panou-lateral, etc.). Are rolul de a valida optiunea in cadrul usi metalice.
# Parametrii:
#	param_optionId
#	param_businessCategory


package require java
java::import -package ro.kds.erp.biz.setum.basic Fereastra FereastraHome FereastraForm
java::import -package ro.kds.erp.biz ResponseBean




proc validate_fereastra {option_id} {
    global factory lFoaie hFoaie k lFoaieSec hFoaieSec
    global response

    puts "Validare fereastra $option_id"


    set fereastraBean [java::cast Fereastra [$factory remote "ejb/FereastraHome" [java::field FereastraHome class]]]
    $fereastraBean loadFormData $option_id
    set fereastra [$fereastraBean getForm]

    # Lfmax
    set lfereastra [$fereastra getLf]
    switch [$fereastra getCanat] {
	1 {
	    puts "1 canat"
	    if {$lfereastra <= ($lFoaie - 380)} {
		// este bine
	    } elseif {$lfereastra <= ($lFoaie - 340)} {
		$response addValidationInfo \
		    [java::call FereastraForm uri lf] \
		    "http://www.kds.ro/readybeans/rdf/validation/message/Fereastra\#exceptie_silduri" \
		    {KZS M/M, 6000 M/M, rozeta/maner CISA M/M}
	    } elseif {$lfereastra <= ($lFoaie - 310)} {
		$response addValidationInfo \
		    [java::call FereastraForm uri lf] \
		    "http://www.kds.ro/readybeans/rdf/validation/message/Fereastra\#restrictie_silduri" \
		    {URBIS NFP M/M, NP M/M}
	    } else {
		$response addValidationInfo \
		    [java::call FereastraForm uri lf] \
		    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
		    "[expr $lFoaie - 310] mm"

		$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
	    }
	}

	2 {
	    puts "2 canate"

	    if {k == 1} {
		$response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/Fereastra\#doar_un_canat"
		$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
	    } elseif {$lfereastra > lFoaieSec - 120} {
		$response addValidationInfo \
		    [java::call FereastraForm uri lf] \
		    "http://www.kds.ro/readybeans/rdf/validation/message\#max" \
		    "[expr $lFoaieSec - 120] mm"

		$response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
	    }
	}
	default {
	    puts "numar necunoscut de canate: [[$fereastra getCanat] intValue]"
	    $response addValidationInfo "http://www.kds.ro/readybeans/rdf/validation/message/Fereastra\#canat_necunoscut" \
		[[$fereastra getCanat] intValue]
	    $response setCode [java::field ResponseBean CODE_ERR_VALIDATION]
	}

    }    
}

proc validate_gv {option_id} {
}

proc validate_ga {option_id} {
}

proc validate_supralumina {option_id} {
}

proc validate_panouLateral {option_id} {
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


