# Computes the values of calculated fields

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


set suprafataBlat [expr 2 * $lFoaie * $hFoaie / 1000 / 1000]

set ldesfToc [expr ($lFrame + 2 * ($bFrame + $cFrame) + 30)/1000]
set ldesfPrag [expr ($lTreshold + 2 * ($hTreshold + $cTreshold) + 30)/1000]
set suprafatToc [expr ($ldesfToc * 2 * ($he + $le) + $ldesfPrag * $le)/1000]

set suprafataGrilaj 0
set suprafataFereastra 0
set suprafataSupralumina 0
set suprafataPanouLateral 0
set suprafataGrilaVentilatie 0


set lplTotal 0 ;# l pentru panou lateral
set hsTotal 0  ;# h pentru supralumina


;# calcul suprafete optiuni si dimensiuni executie
set optiuni [$logic getOptions]
for {set i [$optiuni iterator]} {[$i hasNext]} {} {
    set optiune [java::cast ProductLocal [$i next]]
    log "optiune este $optiune"

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


