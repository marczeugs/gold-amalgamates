package marczeugs.goldamalgamates.mixinextensions

interface ItemStackExtension {
    val stackMaxDamage: Int
    var currentShield: Int
    var shieldRechargeTimer: Int
}