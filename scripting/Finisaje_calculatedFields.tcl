# Finisaje_calculatedFields.tcl

set description ""
set entryPrice 0
set sellPrice 0
set price1 0

if { $zincare != 0 } {
    set categoryZincare [$categoryHome findByPrimaryKey 11100]
    if { $zincare == 1 } {
	    set description "$description, Zincare"
    } else {
	set description "$description, [[$categoryZincare getProductByCode $zincare] getName]"
    }
    set product [$categoryZincare getProductByCode $zincare]
    set entryPrice [expr $entryPrice + [[$product getEntryPrice] doubleValue]]
    set sellPrice [expr $sellPrice + [[$product getSellPrice] doubleValue]]
    set price1 [expr $price1 + [[$product getPrice1] doubleValue]]
}

if { $furnir != 0 } {
    set categoryFurnir [$categoryHome findByPrimaryKey 11107]
    set description "$description, [[$categoryFurnir getProductByCode $furnir] getName]"
    set product [$categoryFurnir getProductByCode $furnir]
    set entryPrice [expr $entryPrice + [[$product getEntryPrice] doubleValue]]
    set sellPrice [expr $sellPrice + [[$product getSellPrice] doubleValue]]
    set price1 [expr $price1 + [[$product getPrice1] doubleValue]]
}

if { $placare != 0 } {
    set categoryPlacare [$categoryHome findByPrimaryKey 11110]
    if { $placare == 1 } {
	set description "$description, Placare"
    } else {
	set description "$description, [[$categoryPlacare getProductByCode $placare] getName]"
    }
    set product [$categoryPlacare getProductByCode $placare]
    set entryPrice [expr $entryPrice + [[$product getEntryPrice] doubleValue]]
    set sellPrice [expr $sellPrice + [[$product getSellPrice] doubleValue]]
    set price1 [expr $price1 + [[$product getPrice1] doubleValue]]
}

if { $grundId != 0 } {
    set categoryGrund [$categoryHome findByPrimaryKey 11115]
    if { $grundId == 1 } {
	set description "$description, Grund"
    } else {
	set description "$description, [[$categoryGrund getProductByCode $grundId] getName]"
    }
    set product [$categoryGrund getProductByCode $grundId]
    set entryPrice [expr $entryPrice + [[$product getEntryPrice] doubleValue]]
    set sellPrice [expr $sellPrice + [[$product getSellPrice] doubleValue]]
    set price1 [expr $price1 + [[$product getPrice1] doubleValue]]
}

if { $vopsireTip != 0 } {
    set categoryVopsire [$categoryHome findByPrimaryKey 11120]
    set description "$description, [[$categoryVopsire getProductByCode $vopsireTip] getName]"
    set product [$categoryVopsire getProductByCode $vopsireTip]
    set entryPrice [expr $entryPrice + [[$product getEntryPrice] doubleValue]]
    set sellPrice [expr $sellPrice + [[$product getSellPrice] doubleValue]]
    set price1 [expr $price1 + [[$product getPrice1] doubleValue]]
}

if { $ralStasId != 0 } {
    set categoryRal [$categoryHome findByPrimaryKey 11125]
    set description "$description, [[$categoryRal getProductByCode $ralStasId] getName]"
    set product [$categoryRal getProductByCode $ralStasId]
    set entryPrice [expr $entryPrice + [[$product getEntryPrice] doubleValue]]
    set sellPrice [expr $sellPrice + [[$product getSellPrice] doubleValue]]
    set price1 [expr $price1 + [[$product getPrice1] doubleValue]]
}

if { [string length $ralOrder] > 0 } {
    set description "$description, $ralOrder"
}

set entryPrice [expr $entryPrice + $ralOrderValue]
set sellPrice [expr $sellPrice + $ralOrderValue]
set price1 [expr $price1 + $ralOrderValue]

set description [string trim $description ", "]
