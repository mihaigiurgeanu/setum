# Finisaje_calculatedFields.tcl

set description "Finisaje:"

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
    set description "$description Grund $grundId"
}
if { $vopsireTip == 1 } {
    set description "$description Vopsire normala"
}
if { $vopsireTip == 2 } {
    set description "$description Vopsire VCE"
}
if { $ralStasId != 0 } {
    set description "$description RAL STAS $ralStasId"
}

