# addOption.tcl
# Script apelat la adaugarea unei optiuni (fereastra, panou-lateral, etc.). Are rolul de a valida optiunea in cadrul usi metalice.
# Parametrii:
#	param_optionId
#	param_businessCategory


package require java
java::import -package ro.kds.erp.biz CommonServicesLocalHome CommonServicesLocal ProductNotAvailable
java:: import -package ro.kds.erp.biz.setum.basic Fereastra


set srv [java::cast CommonServicesLocal [$factory local "ejb/CommonServices" [java::field CommonServicesLocalHome class]]]

# # Gasesc produsul dupa id (care este param_optionId) pentru a lua categoria lui.
# # In functi de categorie, stiu ce fel de optiune este (fereastra, panaou lateral, etc)
# set categoryId [[[[srv findProductById $param_optionId] getCategory] getId] intValue]


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
    
}


proc validate_fereastra {option_id} {
    global factory lFoaie hFoaie k lFoaieSec hFoaieSec

    set fereastra [$factory remote "ejb/Fereastra" [java::field Fereastra class]]
    # Lfmax
    set msg ""

    set lfereastra [[$fereastra getLf] doubleValue]
    switch [[$fereastra getCanat] intValue] {
	1 {
	    if {$lfereastra < ($lFoaie - 380)} {
		// este bine
	    } elseif {$lfereastra < ($lFoaie - 340)} {
		set msg "Pentru acest L al ferestrei puteti folosi toate sildurile, mai putin KZS M/M, 6000 M/M, rozeta/maner CISA M/M!\n\n$msg"
	    } elseif {$lfereastra < ($lFoaie - 310)} {
		set msg "Pentru acest L al ferestrei puteti folosi numai pentru silduri URBIS NFP M/M, NP M/M\n\n$msg" 
	    } else {
		set msg "L-ul ferestrei este prea mare. Dimensiunea maxima adminsibila este [expr $lFoaie - 310] mm.\n\n$msg"
	    }
	}

	2 {
	    if {k == 1} {
		set msg "Usa are un singur canat. Nu puteti pune fereastra pe canatul 2!!\n\n$msg"
	    } elseif {$lfereastra > lFoaieSec - 120} {
		set msg "L-ul ferestrei este prea mare. Dimensiunea maxim admisa este [expr lFoaieSec - 120] mm.\n\n$msg"
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

