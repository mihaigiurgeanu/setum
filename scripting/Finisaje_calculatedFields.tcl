# Finisaje_calculatedFields.tcl

set description ""

if { $zincare == 1 } {
    set description "$description Zincare"
}
if { $furnir == 1 } {
    set description "$description Furnir"
}
if { $placare == 1 } {
    set description "$description Placare"
}
if { $grundId != 0 } {
    set categoryGrund [$categoryHome findByPrimaryKey 11115]
    set description "$description [[$categoryGrund getProductByCode $grundId] getName]"
}
if { $vopsireTip != 0 } {
    set categoryVopsire [$categoryHome findByPrimaryKey 11120]
    set description "$description [[$categoryVopsire getProductByCode $vopsireTip] getName]"
}
if { $ralStasId != 0 } {
    set categoryRal [$categoryHome findByPrimaryKey 11125]
    set description "$description [[$categoryRal getProductByCode $ralStasId] getName]"
}

